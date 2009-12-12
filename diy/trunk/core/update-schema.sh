#creates the schema and the database
mvn hibernate3:hbm2ddl 
#uploads the test data
mvn test-compile 
