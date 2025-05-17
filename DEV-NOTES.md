# Dev notes 😉

## Services:
* `catalog-service` - skillbox service for **courses**, users 
* `payment-service` - **payment** only

## Modules (not runnable)
* `skillbox-common` - module with common **entities**
  * `security` entities [UserAccount, Role, Privilege]
  * `jms` common entities 
* `security-common` - spring-starter for common **spring security** configuration


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





