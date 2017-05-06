FROM java:8-jre
MAINTAINER Imre Nagi <imre.nagi2812@gmail.com>

ADD ./target/gateway.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/gateway.jar"]

EXPOSE 4000