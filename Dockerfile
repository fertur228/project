# Используем официальный образ Java (например, OpenJDK 21)
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR файл из папки target в контейнер
COPY target/nodes-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт для приложения (Spring Boot по умолчанию работает на 8080)
EXPOSE 8080

# Команда для запуска Spring Boot приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
