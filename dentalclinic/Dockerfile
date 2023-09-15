FROM amazoncorretto:20-alpine-jdk

ARG JAR_FILE=target/dentalclinic-0.0.1-SNAPSHOT.jar

WORKDIR /app

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar", "com/DentalClinic/dentalclinic/DentalClinicApplication"]

EXPOSE 8080