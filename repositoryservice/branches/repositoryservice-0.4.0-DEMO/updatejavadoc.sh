javadoc -d apidocs -linksource -sourcepath src -classpath ./bin:. -package org.imirsel.nema.repository
scp -r apidocs root@nema.lis.uiuc.edu:/var/www/html/developer/nema-temp-repository

