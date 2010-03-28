CWD=`pwd`
POLICY="-Djava.security.policy=$CWD/config/reggie.policy"
CONFIG_FILE=config/reggie.config
MAIN_CLASS=org.imirsel.service.lookup.RunReggieLookup
java -classpath .:${project.build.finalName}.jar $FLAG $POLICY  $MAIN_CLASS $CONFIG_FILE