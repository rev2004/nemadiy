mvn process-test-resources -Denv=development
#creates the schema and the database
mvn -Denv=development hibernate3:hbm2ddl 
#uploads the test data
mvn -Denv=development test-compile 
