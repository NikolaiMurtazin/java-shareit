# Используем JDK 21
FROM openjdk:21-jdk-slim

# Устанавливаем рабочий каталог внутри контейнера
WORKDIR /app

# Копируем собранный jar-файл в контейнер
COPY target/shareit-0.0.1-SNAPSHOT.jar app.jar

# Устанавливаем команду для выполнения при запуске контейнера
ENTRYPOINT ["java", "-jar", "app.jar"]