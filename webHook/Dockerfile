# Usar una imagen base de JDK
FROM eclipse-temurin:17-jdk-jammy AS builder

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivo JAR (reemplaza `microservice.jar` con el nombre de tu microservicio)
COPY target/batchOrq-0.0.1-SNAPSHOT.jar /app/batchOrq-0.0.1-SNAPSHOT.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/batchOrq-0.0.1-SNAPSHOT.jar"]
