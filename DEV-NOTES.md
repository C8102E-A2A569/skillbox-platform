# Dev notes 😉

## Services:
* `catalog-service` - skillbox service for **courses**, users 
* `payment-service` - **payment** only
* `notification-service` - external mail service (to send notification to users via email)

## Modules (not runnable)
* `skillbox-common` - module with common **entities**
  * `security` entities [UserAccount, Role, Privilege]
  * `jms` common entities 
* `security-common` - spring-starter for common **spring security** configuration
* `notification-adapter` - module for JCA example (imported in payment-service)

## Notes
### skillbox-common
**packages** of security entities **MUST BE STATED** in **entityManager bean** in Configuration of Spring boot apps

```java
entityManager.setPackagesToScan("com.skillbox.common.security.entity");
```

always check:
* `com.skillbox.config.CatalogPostgresSpringDataConfig`
* `com.skillbox.config.PaymentPostgresSpringDataConfig`

### payment-service
On startup sends http request to get admin user from catalog-service (to kinda ping catalog-service)

will se something like that if connection is OK
```text
Test reach Catalog-service
UserDto(id=0, name=admin, password=null, email=example@mail.ru, enrolledCourses=[], roles=null)
```

Otherwise:
```text
WARNING: Catalog-service unreachable!
```

### security-common
check README.md in `security-common/README.md` to find what needs to be done  

## Testing
http://localhost:15672/ - address for rabbitMQ web

## Problems
* jms вопреки всем ожиданиям нормально настроил сериализацию и все из коробки, а вот с RabbitTemplate какая-то фигня
сериализация не работает почему-то хз почему

* `public HashMap<String, Exchange> createExchanges(AmqpAdmin amqpAdmin)` - в этом бине поставлен цикл while 
на его старте потому что он падает если не запущен rabbitMQ, 
так что мы ждем запуска rabbitMQ



