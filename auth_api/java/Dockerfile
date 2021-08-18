# Add your instructions to dockerize the application
# App must run on port 8000

FROM anapsix/alpine-java
MAINTAINER myNAME
COPY auth_api/target/auth-api-1.0-SNAPSHOT-jar-with-dependencies.jar /home/app.jar
EXPOSE 8000
CMD ["java","-jar","/home/app.jar"]