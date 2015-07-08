### Meandre Server Setup: ###

The user account where the meandre server will run should use bash.
matlab should be in the path /share/apps/matlab/bin




### Testing ###

> Test individual flow
> > These flows should run with the default component properties.
> > Ask Mert/Andy and Kris to confirm that the flows run correctly.
> > Ask Mert/Andy and Kris to check the component tags are correct.


### Database Dependencies ###


> username: nema\_user
> password: xxxxx

  1. meandrestore050
  1. nema plugin: flowservice

### Flow List ###

> The List of flows should match the list below

  1. classification\_ehmann http://imirsel.org/classification/classification_ehmann/classification_ehmann/
  1. classification\_marsyas http://imirsel.org/classification/classification_marsyas/classification_marsyas/
  1. classification\_seyerlehner http://imirsel.org/classification/classification_seyerlehner/classification_seyerlehner/
  1. jaudio\_feature\_extractor http://imirsel.org/feature_extraction/jaudio_feature_extractor/jaudio_feature_extractor/
  1. melody\_dressler http://imirsel.org/analysis/melody/melody_dressler/melody_dressler/
  1. phase\_vocoder http://imirsel.org/analysis/phase_vocoder/phase_vocoder/phase_vocoder/
  1. structure\_mauch http://imirsel.org/analysis/structure/structure_mauch/structure_mauch/
  1. mirex\_evaluation http://imirsel.org/evaluation/mirex_evaluation/mirex_evaluation/



### Database Dependencies ###

  1. nemadatarepository\_dev





### New Release ###

-Backup the development release
> cd $MEANDRE\_HOME
> -Cleanup the log
> rm -rf MeandreInfrastructure-shutdown-security-key.txt
> rm -rf log/**> -Cleanup the published jobs
> rm -rf published\_results/nema/**
> mysql -u -p meandrestore$release;
> truncate meandre\_job\_console;
> truncate meandre\_job\_log;
> truncate meandre\_job\_status;
> truncate meandre\_server\_status;
> truncate meandre\_server\_info;
> truncate meandre\_server\_properties;
> truncate meandre\_server\_log


> mkdir db-backup
> mysqldump -u nema\_user -preduxer101 --database meandrestore050 > meandrestore050.sql
> gzip meandrestore050.sql
> mysqldump -u nema\_user -preduxer101 --database nemadatarepository\_dev > nemadatarepository\_dev.sql
> gzip nemadatarepository\_dev.sql
> mv meandrestore050.sql.gz  nemadatarepository\_dev.sql.gz  db-backup
> cd ..

  1. ar gzip the meandre server and the external modules directory
> tar -cvzf $MEANDRE\_HOME-$release.tar.gz $MEANDRE\_HOME
> tar -cvzf ext\_modules-$release.tar.gz ext\_modules


Copy the zip files to the production server

Deployment on production server

$version = 050
create database IF NOT EXISTS meandrestore$version;
create database IF NOT EXISTS flowservice$version;
create database IF NOT EXISTS nemadatarepository\_dev;
create database IF NOT EXISTS nema\_flowresults$version;
create database diy$version;


create user 'nema\_user'@'localhost' identified by 'xxxxx';

grant all privileges on  meandrestore060.**to 'nema\_user'@'%' identified by 'xxxx';
grant all privileges on  meandrestore060.** to 'nema\_user'@'localhost' identified by 'xxxx';
grant all privileges on  flowservice060.**to 'nema\_user'@'%' identified by 'xxxx;
grant all privileges on  flowservice060.** to 'nema\_user'@'localhost' identified by 'xxxx;
grant all privileges on  nemadatarepository\_dev.**to 'nema\_user'@'localhost' identified by 'xxxx';
grant all privileges on  nemadatarepository\_dev.** to 'nema\_user'@'%' identified by 'xxxx';
grant all privileges on  nema\_flowresults$version.**to 'nema\_user'@'localhost' identified by 'xxxx';
grant all privileges on  nema\_flowresults$version.** to 'nema\_user'@'%' identified by 'xxxx';

grant all privileges on  diy$version.**to 'nema\_user'@'localhost' identified by 'xxxx';
grant all privileges on  diy$version.** to 'nema\_user'@'%' identified by 'xxxx';


mysql -u nema\_user -pxxxx < nemadatarepository\_dev.sql
mysql -u nema\_user -pxxxx < meandrestore$version.sql

Edit flowresults.properties  to point to correct database -correct the jdbc url
Edit nemaflowservice.properties  to point to correct database -correct the jdbc properties




- create new database meandrestore060
- create new database flowservice060

- update meandre-config-store.xml server DB\_URL to point to the release database









