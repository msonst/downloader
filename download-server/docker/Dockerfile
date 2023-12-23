FROM msonst/docker-base-java:latest

#COPY entrypoint.sh ./

ADD download-server-2.0.0.tar ./
COPY config ./download-server-2.0.0/lib/config

VOLUME ["/volume"]

#RUN chmod 0755 entrypoint.sh 
#ENTRYPOINT ["./entrypoint.sh"] 

#RUN addgroup -S app && adduser -S app -G app
#USER app

EXPOSE 9091

CMD /download-server-2.0.0/bin/download-server