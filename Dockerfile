# Etapa de build: compila y empaqueta la aplicación
FROM openjdk:17-jdk-slim AS builder

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo pom.xml y los archivos de configuración de Maven
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Descarga las dependencias (capa de cacheo)
RUN ./mvnw dependency:go-offline -B

# Copia el resto del código fuente
COPY src ./src

# Compila y empaqueta la aplicación (produciendo el jar ejecutable)
RUN ./mvnw clean package -DskipTests -B

# Etapa final: imagen runtime
FROM openjdk:17-jdk-slim

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copia el jar empaquetado desde la etapa builder
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto de la aplicación (por ejemplo, 8080)
EXPOSE 8080

# Define el entrypoint para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
