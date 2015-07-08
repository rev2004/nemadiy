## Using Profiles to install Flowservice for development and production ##

To install as development build
mvn install compile -Denv=res -Dmaven.test.skip=true

To install as production build.
mvn install -Denv=nema -Dmaven.test.skip=true


## Example ##
To run NEMA DIY application in the nema-webapp directory
mvn jetty:run
mvn jetty:run -Dmaven.test.skip=true to omit the tests
mvn jetty:run -Djetty.port=9999  to run jetty on port 9999

To run a application
```
mvn exec:java -Dexec.mainClass=org.imirsel.sample.MainClass -Dexec.args="arg1"
```
To run tests
```
mvn test
```

The above runs all the tests `**/Test*.java **/*Test.java **/*TestCase.java`. The reports are in `target/sunfire-reports`.

## Flags ##
Use -o flag if you are not connected to web -maven will use only local repository
Use -X flag to debug what maven is doing in the background
Use -U flag to tell maven to update the dependency i.e to look the servers for new poms. By default maven checks for new versions once a day, unless a dependency is a snapshot.
Use `-Dmaven.test.skip=true` to bypass all the tests

## Profiles ##
By default the nema-core and nema-webapp modules use the mysql datastore for unit tests, and deployment. You can use an embedded database for the ease of setup and zero configuration by selecting the hsqldb profile as below
```
%> mvn -P hsqldb -Dmaven.test.skip=true jetty:run
```
There are other options -oracle and postgresql if you have access to use those databases, just change the profile value.  For a list of profiles, open the pom.xml under the base project.