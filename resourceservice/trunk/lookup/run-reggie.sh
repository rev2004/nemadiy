POLICY="-Djava.security.policy=$CWD/reggie.policy"
CONFIG_FILE=reggie.conf
MAIN_CLASS=org.imirsel.service.lookup.RunReggieLookup
java -classpath .:${project.build.finalName}.jar $FLAG $POLICY  $MAIN_CLASS $CONFIG_FILE