# notification-adapter
модуль для демонстрации JCA - подключается к Spring Boot приложению, сделан как starter
2 конфига - один для rabbit, второй для JCA сущностей 

## Configuration
- [notification-adapter.example.properties](src%2Fmain%2Fresources%2Fnotification-adapter.example.properties) - пример возможных пропертей и их default значения

**Configuration классы:**
- [JcaConfiguration](src%2Fmain%2Fjava%2Fcom%2Fskillbox%2Fnotification%2Fadapter%2Fjca%2Fconfig%2FJcaConfiguration.java)
- [RabbitMQConfig](src%2Fmain%2Fjava%2Fcom%2Fskillbox%2Fnotification%2Fadapter%2Frabbitmq%2Fconfig%2FRabbitMQConfig.java)
