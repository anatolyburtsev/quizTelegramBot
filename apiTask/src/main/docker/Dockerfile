FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER anatolyburtsev
VOLUME /tmp
ADD api_task-0.0.\*-snapshot.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]