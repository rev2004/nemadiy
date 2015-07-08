## Details ##

1) Check mysql server running: mysql -u nema\_user -p
if not: /etc/init.d/mysqld restart

2) httpd is running ?
if not: /etc/init.d/httpd restart

3) if meandre servers  not running.  /home/meandre/projects/Meandre-Infrastructure-1.4.8/runAll.sh

4) check flow service: ps -aef | grep flow
if not:  /home/meandre/projects/flowservice/1.0.2/run.sh

5) jetty (front end) running?  ps -aef | grep start.jar
if not: /home/meandre/apps/jetty/jetty-6.1.22 /run.sh

///////////////////

6) check workbench:  ps -aef | grep workbench
/home/meandre/apps/Meandre-Workbench-1.4.8/Start-Workbench.sh