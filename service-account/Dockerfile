FROM java:8-jre
MAINTAINER Imre Nagi <imre.nagi2812@gmail.com>

ADD ./target/service-account.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/service-account.jar"]

EXPOSE 6000