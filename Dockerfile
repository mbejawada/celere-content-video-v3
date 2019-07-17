FROM maven:3-jdk-8-alpine AS Build
ENV HOME=/usr/app/video-api
RUN mkdir -p $HOME
WORKDIR $HOME
#1. Add pom.xml only here.  And  #2. Start downloading dependencies
ADD pom.xml $HOME
COPY settings.xml /usr/share/maven/ref/
RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "clean", "--fail-never"]
#3. now add all source code and start compiling
ADD . $HOME
RUN mvn -B clean install

FROM tomcat:8.0.51-jre8-alpine
ARG MONGO_SERVERS
ENV HOME=/usr/app/video-api
WORKDIR $HOME
COPY --from=Build $HOME/celere-content-services-video-v3/target/videoapi.war $CATALINA_HOME/webapps/videoapi#3.0.war
COPY logback.xml $CATALINA_HOME/ops/videoapi_3.0/conf/
COPY config.properties $CATALINA_HOME/ops/videoapi_3.0/conf/
RUN sed -i "s/MONGODB_SERVER_URL/${MONGO_SERVERS}/g" $CATALINA_HOME/ops/videoapi_3.0/conf/config.properties
EXPOSE 8080
CMD ["catalina.sh","run"]