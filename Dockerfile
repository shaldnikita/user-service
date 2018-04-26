FROM java:8-jre

ADD ./target/userservice-2.0.1.RELEASE.jar /app/

CMD ["java", "-Xmx200m", "-jar", "/app/userservice-2.0.1.RELEASE.jar"]

EXPOSE 8080