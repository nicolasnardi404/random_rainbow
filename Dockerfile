FROM bellsoft/liberica-openjdk-alpine-musl:17
# Copy the MySQL JDBC driver to the container
# ADD https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.28/mysql-connector-java-8.0.28.jar /usr/src/main/
ADD ./target/demosecurity-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
