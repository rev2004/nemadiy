ant clean dist
mvn deploy:deploy-file -Durl=scp://monk.lis.uiuc.edu:/content/web/root/snapshot -DrepositoryId=MonkSnapshotRepository -Dfile=dist/nema-repository-temp-0.1-SNAPSHOT.jar -DpomFile=pom.xml -Dversion=0.1-SNAPSHOT
