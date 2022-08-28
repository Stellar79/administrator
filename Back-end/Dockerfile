FROM java:8
EXPOSE 8081
ADD adminVue-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=pro"]