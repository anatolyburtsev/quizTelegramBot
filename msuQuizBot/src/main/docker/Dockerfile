FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER anatolyburtsev
COPY *.jar /middleTelegram.jar
RUN sh -c 'touch /middleTelegram.jar'
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /middleTelegram.jar"]
