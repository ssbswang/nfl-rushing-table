FROM csanchez/maven:adoptopenjdk-11-openj9

USER root

COPY . /backend

RUN cd backend && mvn clean install

ENTRYPOINT ["java","-jar","/backend/target/nfl-rushing-server-0.0.1-SNAPSHOT.jar"]