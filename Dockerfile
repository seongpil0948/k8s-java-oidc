FROM maven:3.9.5 as MAVEN_BUILD
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn package -Dmaven.test.skip=true
# TODO: 이걸로 대체 가능하지 않을까?..
# RUN chmod 700 mvnw
# RUN ./mvnw clean package
# RUN mv target/app-in-host.jar /opt/app-in-image.jar

FROM openjdk:17
WORKDIR /app
ARG JAR_FILE=*.jar
COPY --from=MAVEN_BUILD /build/target/${JAR_FILE} ./app.jar
EXPOSE 8080
# 컨테이너 구동시 시작되는 스크립트, 구동후 첫번째로 실행되는 스크립트로 pid는 1이 된다.
ENTRYPOINT ["java", "-jar", "app.jar"]