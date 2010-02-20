function bbReport(results_file, report_dir)

% bbReport(results_file, report_dir)
%
% Analyze the classification results in results_file using the
% beta-binomial model and create reports of the analysis in the
% specified directory.  Reports take the form of text files,
% mediawiki table fragments, and pictures.  The results file should
% be tab-separated with one header line and the following fields:
% tag, fold, # positive, # negative, system1, system2, ...,
% systemN.  Where the system results are fractional accuracies.

% Horrible: read the file once to determine the number of lines
%dummy = textread(results_file, '\t');
%Nlines = length(dummy);

% Really read it, and reshape it to be the right size
strings = readtext(results_file, '\t');
%strings = reshape(strings, [], Nlines)';
headers = strings(1,:);
systems = headers(5:end);
strings = strings(2:end,:);

% Process the loaded strings
tags = strings(:,1);
strings = strings(:,2:end);

M = reshape(cat(1,strings{:}), size(strings));
folds = M(:,1);
np = M(:,2);
nn = M(:,3);
acc = M(:,4:end);

n = repmat(np + nn, 1, size(acc,2));
y = round(acc .* n);

% Group by tag
[utags, dummy, ui] = unique(tags);
for i=1:max(ui)
  idx = find(ui == i);
  un(i,:) = sum(n(idx,:),1);
  uy(i,:) = sum(y(idx,:),1);
end

% Put each system through the beta-binomial model
for i=1:size(uy,2)
  [th(i,:),in95(i,:,:),a(i),b(i)] = betaBinomEB(uy(:,i), un(:,i));
  drawnow
end

% Make one plot comparing the means and 95% confidence intervals for
% the hyperparameters of each system
olb  = icdf('beta', .025, a, b);
omed = icdf('beta', .5, a, b);
oub  = icdf('beta', .975, a, b);
horizErrorBar(1:length(a), omed, olb, oub, systems);
title('Overall performance')
myPrt('overall', report_dir)

% Write the same data to file
f = fopen([report_dir '/overall.txt'], 'w');
fprintf(f,'Tag\tMedian\tLow\tHigh\n');
for i=1:length(omed)
  fprintf(f,'%s\t%f\t%f\t%f\n', systems{i},omed(i),olb(i),oub(i));
end
fclose(f);


% Make one plot per tag of means and 95% confidence intervals
% comparing the systems
for i=1:size(th,2)
  horizErrorBar(1:size(th,1), th(:,i)', in95(:,1,i)', in95(:,2,i)', ...
                systems)
  title(['Performance on "' utags{i} '"'])
  myPrt(utags{i}, report_dir)
end

% Could write to file, but probably don't want to
%...



%%%%%%%%%%%%%%%%%%%%%%%% Helpers %%%%%%%%%%%%%%%%%%%%%%%%%%%

function horizErrorBar(y, x, xm, xp, labels)

% Make a horizontal errorbar plot.  Y, x, xm, and xp should all be
% vectors of the same length, labels is a cell array of the same
% length.  Xm is the lower bound of the error bar interval, xp is
% the upper bound, note that these are absolute coordinates, not
% relative to x.

plot(x,y,'.',  [xm; xp], [y; y], '-');
ylim([min(y)-1 max(y)+1]);
set(gca, 'YTick', y);
set(gca, 'YTickLabel', labels)


function myPrt(name, pic_dir)

% Simple print function.  Set these options by hand
to_file = 1;

if to_file
  print('-dpng', [pic_dir '/' name])
else
  pause
end
