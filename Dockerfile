### Byggeprocesen navngives 'builder'
FROM gradle:5.5.1-jdk11 as builder

COPY build.gradle .
COPY settings.gradle .
COPY faelles faelles
COPY klient klient

# Spring over tests ( -x test)
RUN gradle clean build -x test --no-daemon

# Under kørsel
FROM adoptopenjdk/openjdk11:jre


#RUN apt update && apt install -y <diverse linux-pakker som I har brug for>
#RUN curl https://daisy.ku.dk/download/daisy_5.88_amd64.deb > daisy.deb && apt install ./daisy.deb && rm -f daisy.deb

# Kopier JAR fra builder-fasen
COPY --from=builder /home/gradle/klient/build/libs/klient.jar /klient.jar
# faelles/build/libs/faelles.jar

# Websiden skal med over
COPY klient/src/main/resources/webside webside



# Start serveren
#CMD [ "java", "-jar", "/daisy.jar", "testkørsel" ]
CMD [ "java", "-cp", "/klient.jar", "restserver_javalin.Server" ]


# docker run -p 8080:8080 -t -i daisykoersel