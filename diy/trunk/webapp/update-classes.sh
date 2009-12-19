mvn clean compile -Dmaven.test.skip=true
scp -r target/classes/org meandre@nema.lis.uiuc.edu:/home/meandre/apps/jetty/jetty-6.1.22/webapps/diy/WEB-INF/classes
#scp -r ./target/nema-webapp-1.0.2-SNAPSHOT/WEB-INF/pages/flow meandre@nema.lis.uiuc.edu:/home/meandre/apps/jetty/jetty-6.1.22/webapps/diy/WEB-INF/pages/flow
#scp -r ./target/nema-webapp-1.0.2-SNAPSHOT/WEB-INF/pages/job meandre@nema.lis.uiuc.edu:/home/meandre/apps/jetty/jetty-6.1.22/webapps/diy/WEB-INF/pages/job
#scp -r ./target/nema-webapp-1.0.2-SNAPSHOT/WEB-INF/pages/submission meandre@nema.lis.uiuc.edu:/home/meandre/apps/jetty/jetty-6.1.22/webapps/diy/WEB-INF/pages/submission
mvn war:war -Dmaven.test.skip=true
scp -r ./target/nema-webapp-1.0.2-SNAPSHOT/WEB-INF/pages meandre@nema.lis.uiuc.edu:/home/meandre/apps/jetty/jetty-6.1.22/webapps/diy/WEB-INF
scp  ./target/nema-webapp-1.0.2-SNAPSHOT/WEB-INF/classes/ApplicationResources.properties meandre@nema.lis.uiuc.edu:/home/meandre/apps/jetty/jetty-6.1.22/webapps/diy/WEB-INF/classes/ApplicationResources.properties
