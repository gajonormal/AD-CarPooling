FROM eclipse-temurin:17-jdk-alpine

# 2. Definir uma variável para o ficheiro JAR
ARG JAR_FILE=target/*.jar

# 3. Copiar o JAR gerado para dentro do container com o nome app.jar
COPY ${JAR_FILE} app.jar

# 4. O comando que arranca a aplicação
ENTRYPOINT ["java", "-jar", "/app.jar"]