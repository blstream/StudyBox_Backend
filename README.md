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

# H2 database

## Create db

	java -jar backend/app/target/app-1.0-SNAPSHOT.jar db migrate backend/app/studybox-h2.yml

## Run

        java -jar backend/app/target/app-1.0-SNAPSHOT.jar server backend/app/studybox-h2.yml

# PostgreSQL

## Create db

	java -jar backend/app/target/app-1.0-SNAPSHOT.jar db migrate backend/app/studybox-pg.yml

## Run

        java -jar backend/app/target/app-1.0-SNAPSHOT.jar server backend/app/studybox-pg.yml

## Sending a file with curl:

    curl -F file=@myFooFileName.jpg http://localhost:2000/decks/cv?fileType=image


[Oracle jdk download link]:http://www.oracle.com/technetwork/java/javase/downloads/index.html
[Maven download link]: http://maven.apache.org/download.cgi?Preferred=ftp://mirror.reverse.net/pub/apache
