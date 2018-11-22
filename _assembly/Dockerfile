FROM openjdk:8u151-jre-alpine
LABEL maintainer="Your Name"

ADD target/application.jar /usr/share/server/application.jar
ENTRYPOINT [ "/usr/bin/java", "-jar", "/usr/share/server/application.jar" ]