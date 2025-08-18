# Utiliza una imagen base con Java (OpenJDK 17 en este ejemplo)
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo .jar de tu microservicio al directorio /app dentro del contenedor
# Asegúrate de reemplazar 'microservice-manager-0.0.1-SNAPSHOT.jar' con el nombre real de tu .jar
COPY target/microservice-project-0.0.1-SNAPSHOT.jar app.jar

# Define el comando para ejecutar la aplicación cuando el contenedor se inicie
ENTRYPOINT ["java", "-jar", "app.jar"]