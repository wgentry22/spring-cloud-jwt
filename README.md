# Spring Cloud Jwt 
* The purpose of this project is a _Spring Boot inspired_ autoconfiguration for security in a microservice ecosystem, including the generation of JWT's for authentication and downstream authorization
* This project (and the corresponding _Spring Cloud Jwt Client_ project) utilizes [Java Key Store](https://docs.oracle.com/cd/E19509-01/820-3503/ggfen/index.html) in order to sign each JWT at the Gateway, and establish a ```Principal``` in the ```SecurityContextHolder``` for downstream microservices
* To use, simply annotate your gateway server with ```@EnableSpringCloudJwtServer```, annotate each of your downstream microservices with ```@EnableSpringCloudJwtClient```, and set the following properties in your configuration server's application.properties/application.yml file
```java 
@SpringBootApplication
@EnableZuulProxy
...
@EnableSpringCloudJwtServer
public class GatewayServerApplication {

    public static void main(String... args) {
        SpringApplication.run(GatewayServerApplciation.class, args);
    }
}
```
```java
@SpringBootApplication
@EnableDiscoveryClient
...
@EnableSpringCloudJwtClient
public class FooServiceApplication {

    public static void main(String... args) {
        SpringApplication.run(FooServiceApplication.class, args);
    }
}
```
```yml
# Set these properties in the config-server application.yml
spring:
    cloud:
        jwt:
            server:
                keystore-location: <path-to-.jks-file>
                keystore-alias: <alias-of-.jks-file>
                keystore-pass: <password-of-.jks-file>
                use-generated-keystore: false # A feature to be implemented in the future, but not quite ready yet
```

## Project Defaults
* Authentication begins by creating a ```POST``` request to ```/authenticate``` of type _application/x-www-url-formencoded_, but can be changed by modifying the constructor argument in ```com.revature.security.CustomAuthenticationFilter```
* The ```Principal``` object is established in the ```SecurityContextHolder``` through the ```com.revature.security.JwtUserDetailsService``` interface, which is an extension of Spring Security's ```UserDetailsService```. If you would like to instead use your own, simply extend the ```JwtUserDetailsService``` and modify the constructor argument in the ```com.revature.security.CustomAuthenticationProvider``` class.
* The ```Principal``` object is determined from the model classes ```com.revature.model.AuthenticatedUser``` and ```com.revature.model.Authorities```
* There are 3 roles in the application: Admin, Manager, and User. Admin's have full access. Manager's can see manager and user resources. To customize this, go to the ```com.revature.security.JwtWebSecurityConfig``` class and modify the ```RoleHierarchyImpl``` bean. More information [here](https://docs.spring.io/spring-security/site/docs/4.2.7.RELEASE/apidocs/org/springframework/security/access/hierarchicalroles/RoleHierarchyImpl.html)