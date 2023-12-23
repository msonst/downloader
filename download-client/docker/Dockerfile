FROM msonst/docker-base-java:latest

#COPY entrypoint.sh ./

ADD download-client-2.0.0.tar ./
#RUN chmod 0755 /download-client-2.0.0/download-client 

VOLUME ["/volume"]

#RUN chmod 0755 entrypoint.sh 
#ENTRYPOINT ["./entrypoint.sh"] 

#RUN addgroup -S app && adduser -S app -G app
#USER app

CMD /download-client-2.0.0/bin/download-client