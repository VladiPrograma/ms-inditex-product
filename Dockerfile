FROM registry.global.ccc.srvb.bo.paas.cloudcenter.corp/produban/javase-17-ubi8:1.2.25.RELEASE
ARG ARTIFACT
COPY ${ARTIFACT} /opt/app/application.jar
EXPOSE 8080
ENV JAR_NAME "/application.jar"
ENV JAVA_OPTIONS ""
USER java
