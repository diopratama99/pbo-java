# Tahap 1: build file .war dengan Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Cache ~/.m2 supaya build berikutnya tidak download ulang semua dependency
RUN --mount=type=cache,target=/root/.m2 mvn -B -q package

# Tahap 2: jalankan di Tomcat 10.1 (Jakarta Servlet 6.0)
FROM tomcat:10.1-jre17-temurin
# Dipasang sebagai ROOT, jadi aplikasi terbuka di "/" (bukan "/pbo-java")
COPY --from=build /app/target/pbo-java.war /usr/local/tomcat/webapps/ROOT.war
ENV CATALINA_OPTS="-Xms128m -Xmx512m -Duser.timezone=Asia/Jakarta"
EXPOSE 8080
