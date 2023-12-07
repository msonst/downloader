FROM eclipse-temurin:20.0.1_9-jre-alpine
COPY build/libs/download-service-server-2.0.0.jar .
CMD java -Xmx300m -Xms300m -XX:TieredStopAtLevel=1 -noverify -jar download-service-server-2.0.0.jar
EXPOSE 8083