function [theta, in95, agy, bgy] = betaBinomEB(y, n, meangrid, sizegrid)

% [theta, in95, agy, bgy] = betaBinomEB(y, n [, meangrid, sizegrid])
%
% Performs empirical Bayes estimate for a beta-binomial model.  The
% data are counts of successes in y and total trials in n for each
% experiment.  These data are assumed to be binomial, with parameters
% theta, which are themselves distributed according to a single beta
% distribution.  The parameters for this beta distribution are modeled
% with an uninformative prior.  For more information, see section 5.3
% in Bayesian Data Analysis.

% Calculate point estimates for parameters and ranges around them
th_hat = mean(y./n)
s_hat  = std(y./n)
apb = th_hat * (1-th_hat) / s_hat.^2 - 1;
a_hat = apb * th_hat
b_hat = apb * (1-th_hat)

if nargin < 3
  meangrid = log(a_hat ./ b_hat) + linspace(-.5, .5, 100); 
end
if nargin < 4
  sizegrid = log(a_hat + b_hat)  + linspace(-1.5, 2, 100);
end

[logmean, logsample] = meshgrid(meangrid, sizegrid);

a = exp(logmean+logsample) ./ (1 + exp(logmean));
b = exp(logsample) ./ (1 + exp(logmean));

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Compute posterior probability of hyperparameters
logpost = log(a) + log(b) - 5/2*log(a+b);
for j=1:length(n)
  logpost = logpost+gammaln(a+b)+gammaln(a+y(j))+gammaln(b+n(j)-y(j));
  logpost = logpost-gammaln(a)-gammaln(b)-gammaln(a+b+n(j));
end

post = exp(logpost - max(logpost(:)));
post = post / sum(post(:));
subplot 131, imagesc(meangrid, sizegrid, post), axis xy, drawnow
title('Posterior hyperparameters')
xlabel('log(alpha+beta)')
ylabel('log(alpha/beta)')

% Conditional mean of a and b given y
agy = sum(sum(a .* post));
bgy = sum(sum(b .* post));
fprintf('conditional mean of alpha = %f\n', agy)
fprintf('conditional mean of beta = %f\n', bgy)


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Simulate posterior thetas
chunk = 20;
p = repmat(post(:), 1, chunk);
for i=1:1000/chunk
  hypsamp((i-1)*chunk+1:i*chunk) = drawMultinom(p);
end

meansamp = logmean(hypsamp)   + mean(diff(meangrid))*randn(size(hypsamp));
sizesamp = logsample(hypsamp) + mean(diff(sizegrid))*randn(size(hypsamp));

subplot 132, plot(meansamp, sizesamp, '.');
axis([min(meangrid) max(meangrid) min(sizegrid) max(sizegrid)])
title('Hyperparameter samples')
xlabel('log(alpha+beta)')
ylabel('log(alpha/beta)')
drawnow

asamp = exp(meansamp+sizesamp) ./ (1 + exp(meansamp));
bsamp = exp(sizesamp) ./ (1 + exp(meansamp));

r = zeros(length(y), length(meansamp));
for j=1:length(y)
%  r(j,:) = beta_sample(asamp + y(j), bsamp + n(j) - y(j));
  r(j,:) = random('beta', asamp + y(j), bsamp + n(j) - y(j));
end

q = quantile(r', [.025 .5 .975]);
theta = q(2,:);
in95 = q([1 3],:);

subplot 133, errorbar(y./n, theta, theta-in95(1,:), in95(2,:)-theta, '.')
hold on
plot(y./n, y./n, 'r')
hold off
title('Posterior estimated accuracy with 95% confidence intervals')
xlabel('observed accuracy')
ylabel('estimated accuracy')

subplot 111
