# Brugerautorisation
Repo til brugerautorisationsmodulet på kurset 62597 Backendudvikling, drift og distribuerede systemer

Dette repo understøtter kurset http://kurser.dtu.dk/course/62597

Brug som server: Start BenytBrugerdatabase - dette skal I normalt IKKE gøre

Brug som klient: 
Start med at køre Brugeradminklient i RMI eller SOAP pakkerne




## Lokal kommandolinjetest uden for Docker

./gradlew build; 


java -jar klient/build/libs/klient.jar

java -cp klient/build/libs/klient.jar restserver_javalin.Server
Åbn http://localhost:8080


## Lokal test i Docker
Tjek Dockerfile

docker build  --tag=brugeraut .
docker run -p 8080:8080 -t -i brugeraut
Åbn http://localhost:8080


## Læg op på Google Cloud Run

### Førstegangs forberedelse
Følg https://cloud.google.com/run/docs/quickstarts/build-and-deploy

### Engangskald til at sætte hvor kørslerne udføres
gcloud config set run/region europe-north1

### Læg i drift / upload til Dockerbillede til Google Cloud Run
gcloud builds submit --tag gcr.io/MITPROJEKT/brugeraut
gcloud run deploy --image gcr.io/MITPROJEKT/brugeraut --platform managed --allow-unauthenticated brugeraut


F.eks.
gcloud builds submit --tag gcr.io/nitrogensensor/brugeraut && \
gcloud run deploy --image gcr.io/nitrogensensor/brugeraut --platform managed --allow-unauthenticated brugeraut
