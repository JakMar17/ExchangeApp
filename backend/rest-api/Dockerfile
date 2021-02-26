FROM  maven:3.6.3-openjdk-15 AS maven
COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/
RUN mvn package -Dmaven.test.skip=true

FROM openjdk:15.0.1-oraclelinux7
ENV EMAIL_SERVER_BASE_URL=http://localhost:8082/
ENV FILESERVER_PATH=/mnt/uploads
EXPOSE 80

COPY --from=maven /tmp/target/exchange-app-0.0.1-SNAPSHOT.jar /data/jarJarBeans.jar
CMD java -jar /data/jarJarBeans.jar --server.port=80 --email-server.base.url=${EMAIL_SERVER_BASE_URL} --fileserver.path=${FILESERVER_PATH}