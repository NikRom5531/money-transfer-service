# Базовый образ с JDK 22
FROM openjdk:22-jdk-slim AS builder

# Установка директории рабочей области
WORKDIR /app

# Копируем файлы pom.xml, mvnw и директорию .mvn
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Копируем исходный код в контейнер
COPY src ./src

# Делаем mvnw исполняемым и собираем проект
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Определяем переменную окружения для файла jar
FROM openjdk:22-jdk-slim
WORKDIR /app
ARG JAR_FILE=/app/target/*.jar

# Копируем скомпилированный jar файл в контейнер
COPY --from=builder ${JAR_FILE} app.jar

# Пробрасываем порт, который использует приложение
EXPOSE 8082

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
