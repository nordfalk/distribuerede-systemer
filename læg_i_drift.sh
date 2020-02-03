
# ant -q; rsync -a dist/* Deltagerliste.html  gmail-adgangskode.txt  javabog.dk:Brugerautorisation/
./gradlew jar; cp server/build/libs/server-1.0-SNAPSHOT.jar Brugerautorisation.jar; rsync -a Brugerautorisation.jar Deltagerliste.html  gmail-adgangskode.txt  javabog.dk:Brugerautorisation/

