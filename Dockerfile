FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw -s .mvn/settings-public.xml -Dmaven.repo.local=.mvn/repository clean package -DskipTests


FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/buglab-shop-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080

EXPOSE 8080

CMD ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]