FROM openjdk:18-jdk-alpine

COPY download-server-1.0.0-SNAPSHOT.tar ./

RUN mkdir /download-server && tar -xf download-server-1.0.0-SNAPSHOT.tar -C /download-server && chmod +x /download-server/download-server-1.0.0-SNAPSHOT/bin/download-server

EXPOSE 4242/TCP 9010/TCP

CMD ["/download-server-1.0.0-SNAPSHOT/bin/download-server"]