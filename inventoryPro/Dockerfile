# ==========================================
# Stage 1: Build stage (Java 21 + Maven Multi-stage)
# ==========================================
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copiar manifiestos Maven y wrappers
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw.cmd ./

# Asegurar saltos de línea LF en mvnw y dar permisos de ejecución
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Descargar dependencias para optimizar la caché de capas
RUN ./mvnw dependency:go-offline -B || mvn dependency:go-offline -B || true

# Copiar código fuente y construir el ejecutable JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# ==========================================
# Stage 2: Production runtime stage (JRE 21 Alpine)
# ==========================================
FROM eclipse-temurin:21-jre-alpine AS runner

WORKDIR /app

# Crear usuario y grupo no-root por seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser:appgroup

# Copiar el artefacto JAR compilado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto por defecto de Spring Boot
EXPOSE 8080

# Ejecución del contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]
