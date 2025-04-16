# Используем официальный образ Java (например, OpenJDK 21)
FROM openjdk:21-jdk-slim

# Устанавливаем переменные окружения для базы данных
ENV DATABASE_URL=postgresql://your-db-user:your-db-password@your-db-host:5432/your-db-name
ENV DB_USER=your-db-user
ENV DB_PASSWORD=your-db-password

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR файл из папки target в контейнер
COPY target/nodes-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт для приложения (Spring Boot по умолчанию работает на 8080)
EXPOSE 8080

# Команда для запуска Spring Boot приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
