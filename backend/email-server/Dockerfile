FROM  maven:3.6.3-openjdk-15 AS maven
COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/
RUN mvn package -Dmaven.test.skip=true

FROM openjdk:15.0.1-oraclelinux7
ENV EMAIL_USERNAME=
ENV EMAIL_PASSWORD=
ENV STATIC_SITES_BASE_URL=

EXPOSE 80
COPY --from=maven /tmp/target/exchangeapp-emails-0.0.1-SNAPSHOT.jar /data/jarJarBeans.jar
CMD java -jar /data/jarJarBeans.jar --spring.mail.username=${EMAIL_USERNAME} --spring.mail.password=${EMAIL_PASSWORD} --server.port=80 --static-sites.base.url=${STATIC_SITES_BASE_URL}