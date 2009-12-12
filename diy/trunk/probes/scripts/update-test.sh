cd ..
mvn deploy -Dmaven.test.skip=true
scp target/probes-1.0.2-SNAPSHOT.jar amit@128.174.154.145:/home/amit/projects/Meandre-Infrastructure-1.4.8/nema/lib
