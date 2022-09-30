FROM openjdk:11
WORKDIR /
ADD build/libs/hypnobook-0.0.1-SNAPSHOT.jar hypnobook.jar
RUN useradd -m hbuser
USER hbuser
EXPOSE 8181
CMD java -jar -Dspring.profiles.active=prod hypnobook.jar