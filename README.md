# StudyBox_Backend - BLStream Patronage 2016 application

## REST API documentation and mock: http://docs.studybox.apiary.io/#

## Content
- backend - dropwizard maven/gradle java application
- scripts - helper shell scripts, CD, etc.

Prerequisites:
- install: JDK 1.8 [Oracle jdk download link]
- install: Maven 3.x [Maven download link]

## Building with maven

        (cd backend && mvn -T4 clean package)

## Running on H2

        (java -jar backend/app/target/app-1.0-SNAPSHOT.jar server backend/app/studybox-h2.yml)

## Running on PostgreSQL

        (java -jar backend/app/target/app-1.0-SNAPSHOT.jar server backend/app/studybox-pg.yml)


[Oracle jdk download link]:http://www.oracle.com/technetwork/java/javase/downloads/index.html
[Maven download link]: http://maven.apache.org/download.cgi?Preferred=ftp://mirror.reverse.net/pub/apache
