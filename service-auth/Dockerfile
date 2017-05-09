FROM java:8-jre
MAINTAINER Imre Nagi <imre.nagi2812@gmail.com>

ADD ./target/service-auth.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/service-auth.jar"]

EXPOSE 5000