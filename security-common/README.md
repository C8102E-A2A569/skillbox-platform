# security-common

## Quickstart

1) import this common Spring Security configuration (spring-starter) to your app (service)

```xml

<dependency>
    <groupId>com.skillbox.security</groupId>
    <artifactId>security-common</artifactId>
    <version>1.0.0</version>
</dependency>
```

2) implement [CommonSecurityService](src%2Fmain%2Fjava%2Fcom%2Fskillbox%2Fsecurity%2Fservice%2FCommonSecurityService.java) so it retrieves your own entity (of type "? extends UserDetails")

3) protect your URLs or permitAll in [SecurityConfig](src%2Fmain%2Fjava%2Fcom%2Fskillbox%2Fsecurity%2Fconfig%2FSecurityConfig.java)

**Done!**