FROM maven:3.8-openjdk-16
COPY . /tmp/build
WORKDIR /tmp/build
RUN ["mvn", "package"]

FROM openjdk:16-alpine
RUN mkdir /home/container &&\
    mkdir /home/container/plugins &&\
    wget https://papermc.io/api/v2/projects/paper/versions/1.16.5/builds/742/downloads/paper-1.16.5-742.jar -O /home/container/server.jar &&\
    wget https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar -O /home/container/plugins/Vault.jar &&\
    echo "eula=true" >> /home/container/eula.txt
COPY --from=0 /tmp/build/SpigotCore/target/core-*-SNAPSHOT.jar /home/container/plugins/core.jar

WORKDIR /home/container

CMD ["java","-jar", "/home/container/server.jar"]