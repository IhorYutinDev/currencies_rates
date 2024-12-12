# Используем официальный образ с OpenJDK 17
FROM openjdk:17-jdk

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR файл в контейнер
COPY target/CurrencyRates-0.0.1-SNAPSHOT.jar /app/CurrencyRates-0.0.1-SNAPSHOT.jar

# Копируем файл конфигурации (если используется)
COPY src/main/resources/application.properties /app/application.properties

# Проверяем содержимое директории, чтобы убедиться, что файлы скопированы
RUN ls -l /app

# Устанавливаем переменные окружения для конфигурации
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/currency_rates
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV EXCHANGE_ACCESS_KEY=5118da45dc294616f98a369f913990f3
ENV EXCHANGE_API_URL=http://data.fixer.io
ENV EXCHANGE_BASE_CURRENCY=EUR
ENV EXCHANGE_CRON_EXPRESSION="0 0 * ? * * *"

# Указываем команду для запуска JAR файла
ENTRYPOINT ["java", "-jar", "/app/CurrencyRates-0.0.1-SNAPSHOT.jar"]