FROM  maven:3.6.3-openjdk-15 AS maven
COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/
RUN mvn package -Dmaven.test.skip=true

FROM alpine:3.12
RUN apk add bash
RUN apk add diffutils
#RUN apk add openjdk11
RUN apk add openjdk15-jdk --repository=http://dl-cdn.alpinelinux.org/alpine/edge/testing

ENV JAVA_HOME=/usr/lib/jvm/java-15-openjdk
ENV PATH="$JAVA_HOME/bin:${PATH}"

EXPOSE 80
COPY --from=maven /tmp/target/testingutility-0.0.1-SNAPSHOT.jar /data/jarJarBeans.jar
CMD java -jar /data/jarJarBeans.jar --server.port=80 --spring.profiles.active=prod