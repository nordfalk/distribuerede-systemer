
# ant -q; rsync -a dist/* Deltagerliste.html  gmail-adgangskode.txt  javabog.dk:Brugerautorisation/

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/

./gradlew jar; cp server/build/libs/server-1.0-SNAPSHOT.jar Brugerautorisation.jar; rsync -a Brugerautorisation.jar Deltagerliste.html  gmail-adgangskode.txt  javabog.dk:Brugerautorisation/

