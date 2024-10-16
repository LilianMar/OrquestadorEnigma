# Usar una imagen base de JDK
FROM eclipse-temurin:17-jdk-jammy AS builder

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivo JAR (reemplaza `microservice.jar` con el nombre de tu microservicio)
COPY target/resolve-enigma-api-1.0.0.jar /app/resolve-enigma-api-1.0.0.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/resolve-enigma-api-1.0.0.jar"]
