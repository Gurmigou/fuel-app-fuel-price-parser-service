FROM openjdk:17-alpine3.14
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} fuel_app_price_parser.jar
ENTRYPOINT ["java","-jar","/fuel_app_price_parser.jar"]