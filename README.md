
[![Build Status](https://travis-ci.org/imrenagi/microservice-skeleton.svg?branch=master)](https://travis-ci.org/imrenagi/microservice-skeleton)  [![codecov](https://codecov.io/gh/imrenagi/microservice-skeleton/branch/master/graph/badge.svg)](https://codecov.io/gh/imrenagi/microservice-skeleton) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) 

# Microservice Skeleton


This project is a simple skeleton code for microservice architecture pattern using Spring Cloud, Spring Boot, and Docker.
 Thanks to [Piggy Metrics](https://github.com/sqshq/PiggyMetrics) which has inspired and given really good knowledge for setting up
 this project.

## Services

By now, the functional services are still decomposed into three core services. Each of them can be tested, built, and deployed independently.

![Infrastructure plan](http://res.cloudinary.com/imrenagi-com/image/upload/v1494871114/Untitled_cwhlwy.png)


### Auth service
Provides several API for user authentication and authorization with OAuth 2.0.

| Method | Path              | Description                                   | Scope |
|--------|-------------------|-----------------------------------------------|-------|
| POST   | /uaa/oauth/token  | Get new access token and refresh access token | ui    |
| POST   | /uaa/oauth/logout | Logout to revoke access token                 | ui    |


### Account service
Contains API related to creating and retrieving all user information. In this service, we are also demonstrating how we use 
user's privilege in accessing the API. For example, the access to `GET ` `/accounts` endpoint will only be allowed for user whose `READ_BASIC_INFORMATION` privilege, but the access
 to other endpoints don't require any special privilege as long as it has correct scope. Please refer to spring security docs [here](http://projects.spring.io/spring-security-oauth/docs/oauth2.html) for more details.

| Method | Path              | Description                                   | Scope |  Privilege |
|--------|-------------------|-----------------------------------------------|-------|------------|
| POST   | /accounts  | Create new account | ui    | ALL_ACCESS |
| GET    | /accounts | Get All user informations                | ui    | READ_BASIC_INFORMATION |
| GET    | /accounts/{username} | Get account with username | server | ALL_ACCESS |
| GET    | /accounts/current  | Get current account data | ui | ALL_ACCESS |

### Notification service
[WIP aka Work In Progress]

#### Notes
* Each microservice has it's own database and there is no way to access the database directly from other services.
* The services in this project are using MySQL for the persistent storage. In other case, it is also possible for one service 
to use any type of database (SQL or NoSQL).
* Service-to-service communiation is done by using REST API. It is pretty convenient to use HTTP call in Spring
since it provides a simplify HTTP layer service called Feign (discussed later). For the next iteration, I'm also planningto use
 asyncronous message transfer using Apache Kafka since Kafka will allow us to send message to several service easily.

## Infrastructure
Spring Cloud is a really good web framework that we can use for building a microservice infrastructure since it provides 
broad supporting tools such as Load Balancer, Service registry, Monitoring, and Configuration.

![This image is taken from PiggyMetrics](https://cloud.githubusercontent.com/assets/6069066/13906840/365c0d94-eefa-11e5-90ad-9d74804ca412.png)
Image source: [PiggyMetrics](https://github.com/sqshq/PiggyMetrics)

### Config *
[Spring Cloud Config](http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html) is horizontally scalable centralized configuration service for distributed systems. It uses a pluggable repository layer that currently supports local storage, Git, and Subversion. 

For the purpose of proof-of-concept, we just use spring `native profile`, which simply loads config file from local classpath. You can see that we have `shared` diretory in [Config service resource](https://github.com/imrenagi/microservice-skeleton/tree/master/config/src/main/resources). When a service (e.g. Account-service) wants to request it's configuration, the config service will response with `shared/service-account.yml` and `shared/application.yml` (which is shared among all services).
    
##### Client side usage
Just build Spring Boot application with `spring-cloud-starter-config` dependency, autoconfiguration will do the rest.

Now you don't need any embedded properties in your application. Just provide `bootstrap.yml` with application name and Config service url:
```yml
spring:
  application:
    name: notification-service
  cloud:
    config:
      uri: http://config:8888
      fail-fast: true
```

### Auth Service

Authorization responsibilities are completely extracted to separate server, which grants [OAuth2 tokens](https://tools.ietf.org/html/rfc6749) for the backend resource services. Auth Server is used for user authorization as well as for secure machine-to-machine communication inside a perimeter.

This project only uses two type of authorizations, they are [`Password credentials`](https://tools.ietf.org/html/rfc6749#section-4.3) grant type for users authorization and [`Client Credentials`](https://tools.ietf.org/html/rfc6749#section-4.4) grant for microservices authorization.

There are two ways of storing the authorization token granted for users. First, we can us in memory database provided by Spring security oauth2 library. Second, we can use persistent storage such as (MySQL, MongoDB, PostgresSQL, etc) to keep access tokens, refresh tokens and client credentials. In this project, I'm using MySQL database to keep all authorization information along with the database schema that will be executed by flyway. To change the implementation into in-memory database, simply:
* Comment `@Configuration`, `@EnableAuthorizationServer` and `@EnableWebSecurity` in `JdbcOAuth2Config.java` and `JdbcServerSecurityConfig.java`.
* Comment out `@Configuration`, `@EnableAuthorizationServer` and `@EnableWebSecurity` in `InMemoryOAuth2Config.java` and `InMemoryWebSecurityConfig.java`
* Build the program.

Spring Cloud Security provides convenient annotations and autoconfiguration to make this really easy to implement from both server and client side. You can learn more about it in [documentation](http://cloud.spring.io/spring-cloud-security/spring-cloud-security.html) and check configuration details in Auth Server code.

From the client side, everything works exactly the same as with traditional session-based authorization. You can retrieve `Principal` object from request, check user's roles and other stuff with expression-based access control and `@PreAuthorize` annotation.

Each client in this application (service-account, service-auth, and service-notification) has a scope: `server` for backend services, and `ui` - for the browser. So we can also protect controllers from external access, for example:

``` java
@PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(method = RequestMethod.POST)
    public void createUser(@Valid @RequestBody User user) {
        userService.create(user);
    }
```

In addition, I also use role and privilege authorization for several endpoints in this project to protect the controller from unauthorized access. To add specific authorization checking, we can use `hasAuthority()` or `hasRole()` as the parameter of the `@PreAuthorize` annotation. For example:
``` java
    @PreAuthorize("hasAuthority('READ_BASIC_INFORMATION')")
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity<AccountResponse> getAllAccounts() {
        AccountResponse response = new AccountResponse();
        response.accounts = accountService.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
```

### Database

I use MySQL for persistent data storage for several services in this application. To help me in doing some database schema migration, I use [Flyway by boxfuse](https://flywaydb.org/) to help me creating all tables and updating the schema. 

To use flyway in Spring application, simply define this configuration in service config file and put all migration scripts in `db/migration` under resources folder.
```yml
flyway:
  url: jdbc:mysql://service-auth-db:3306/auth
  user: ${SERVICE_AUTH_DB_USER}
  password: ${SERVICE_AUTH_DB_PASSWORD}
  locations: classpath:db/migration
  enabled: true
```
Using flyway in this project is really simple because we don't have to explicitly run migration command. Spring will handle this process for us.
 
### API Gateway *

As you can see, there are three core services, which expose external API to client. In a real-world systems, this number can grow very quickly as well as whole system complexity. Actualy, hundreds of services might be involved in rendering one complex webpage. 

In theory, a client could make requests to each of the microservices directly. But obviously, there are challenges and limitations with this option, like necessity to know all endpoints addresses, perform http request for each peace of information separately, merge the result on a client side. Another problem is non web-friendly protocols, which might be used on the backend.

Usually a much better approach is to use API Gateway. It is a single entry point into the system, used to handle requests by routing them to the appropriate backend service or by invoking multiple backend services and [aggregating the results](http://techblog.netflix.com/2013/01/optimizing-netflix-api.html). Also, it can be used for authentication, insights, stress and canary testing, service migration, static response handling, active traffic management.

Netflix opensourced [such an edge service](http://techblog.netflix.com/2013/06/announcing-zuul-edge-service-in-cloud.html), and now with Spring Cloud we can enable it with one `@EnableZuulProxy` annotation. In this project, I use Zuul to store static content (ui application) and to route requests to appropriate microservices. Here's a simple prefix-based routing configuration for Notification service:

```yml

zuul:
  routes:
    service-auth:
      path: /uaa/**
      url: http://service-auth:5000
      stripPrefix: false
      sensitiveHeaders:

    service-account:
      path: /accounts/**
      serviceId: service-account
      stripPrefix: false
      sensitiveHeaders:

```

That means all requests starting with `/uaa` will be routed to Authentication service and `/accounts` will be forwarded to account service. There is no hardcoded address, as you can see. Zuul uses Service discovery  mechanism to locate Notification service instances and also Circuit Breaker and Load Balancer, described below.

### Service Discovery *
Another commonly known architecture pattern is Service discovery. It allows automatic detection of network locations for service instances, which could have dynamically assigned addresses because of auto-scaling, failures and upgrades.

The key part of Service discovery is Registry. I use Netflix Eureka in this project. Eureka is a good example of the client-side discovery pattern, when client is responsible for determining locations of available service instances (using Registry server) and load balancing requests across them.

With Spring Boot, you can easily build Eureka Registry with `spring-cloud-starter-eureka-server` dependency, `@EnableEurekaServer` annotation and simple configuration properties.

Client support enabled with `@EnableDiscoveryClient` annotation an `bootstrap.yml` with application name:
``` yml
spring:
  application:
    name: servuce-account
```

Now, on application startup, it will register with Eureka Server and provide meta-data, such as host and port, health indicator URL, home page etc. Eureka receives heartbeat messages from each instance belonging to a service. If the heartbeat fails over a configurable timetable, the instance will be removed from the registry.

Also, Eureka provides a simple interface, where you can track running services and number of available instances: `http://localhost:8761`

### Load balancer, Circuit Breaker and Http Client *

#### Ribbon
Ribbon is a client side load balancer which gives you a lot of control over the behaviour of HTTP and TCP clients. Compared to a traditional load balancer, there is no need in additional hop for every over-the-wire invocation - you can contact desired service directly.

Out of the box, it natively integrates with Spring Cloud and Service Discovery. Eureka Client provides a dynamic list of available servers so Ribbon could balance between them.

#### Hystrix
Hystrix is the implementation of [Circuit Breaker pattern](http://martinfowler.com/bliki/CircuitBreaker.html), which gives a control over latency and failure from dependencies accessed over the network. The main idea is to stop cascading failures in a distributed environment with a large number of microservices. That helps to fail fast and recover as soon as possible - important aspects of fault-tolerant systems that self-heal.

Besides circuit breaker control, with Hystrix you can add a fallback method that will be called to obtain a default value in case the main command fails.

Moreover, Hystrix generates metrics on execution outcomes and latency for each command, that we can use to monitor system behavior.

#### Feign
Feign is a declarative Http client, which seamlessly integrates with Ribbon and Hystrix. Actually, with one `spring-cloud-starter-feign` dependency and `@EnableFeignClients` annotation you have a full set of Load balancer, Circuit breaker and Http client with sensible ready-to-go default configuration.

Here is an example from Account Service:

``` java
@FeignClient(name = "service-auth")
public interface AuthServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "/uaa/users", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    void createUser(User user);

}
```

- Everything you need is just an interface
- You can share `@RequestMapping` part between Spring MVC controller and Feign methods
- Above example specifies just desired service id - `service-auth`, thanks to autodiscovery through Eureka (but obviously you can access any resource with a specific url)

### Monitoring Dashboard *

In this project configuration, each microservice with Hystrix on board pushes metrics to Turbine via Spring Cloud Bus (with AMQP broker). The Monitoring project is just a small Spring boot application with [Turbine](https://github.com/Netflix/Turbine) and [Hystrix Dashboard](https://github.com/Netflix/Hystrix/tree/master/hystrix-dashboard).

See below [how to get it up and running](https://github.com/imrenagi/microservice-skeleton/tree/master/readme/README.md#how-to-run-all-things).

Let's see our system behavior under load: A service calls another service and it responses with a vary imitation delay. Response timeout threshold is set to 1 second.

<img width="880" src="https://cloud.githubusercontent.com/assets/6069066/14194375/d9a2dd80-f7be-11e5-8bcc-9a2fce753cfe.png">

<img width="212" src="https://cloud.githubusercontent.com/assets/6069066/14127349/21e90026-f628-11e5-83f1-60108cb33490.gif">	| <img width="212" src="https://cloud.githubusercontent.com/assets/6069066/14127348/21e6ed40-f628-11e5-9fa4-ed527bf35129.gif"> | <img width="212" src="https://cloud.githubusercontent.com/assets/6069066/14127346/21b9aaa6-f628-11e5-9bba-aaccab60fd69.gif"> | <img width="212" src="https://cloud.githubusercontent.com/assets/6069066/14127350/21eafe1c-f628-11e5-8ccd-a6b6873c046a.gif">
--- |--- |--- |--- |
| `0 ms delay` | `500 ms delay` | `800 ms delay` | `1100 ms delay`
| Well behaving system. The throughput is about 22 requests/second. Small number of active threads in Statistics service. The median service time is about 50 ms. | The number of active threads is growing. We can see purple number of thread-pool rejections and therefore about 30-40% of errors, but circuit is still closed. | Half-open state: the ratio of failed commands is more than 50%, the circuit breaker kicks in. After sleep window amount of time, the next request is let through. | 100 percent of the requests fail. The circuit is now permanently open. Retry after sleep time won't close circuit again, because the single request is too slow.



## Infrastructure Automation
[WIP]

## How to run all things

### Before you start
* Install docker and docker compose
* Export environment variables: `SW_CONFIG_SERVICE_PASSWORD`, `MSW_ROOT_PASSWORD`,  `MSW_DB_USER`, `MSW_DB_PASSWORD`, `MSW_SERVICE_ACCOUNT_PASSOWRD`, `MSW_DB_TEST_PASSWORD`.

### Production
In production mode, all images will be pulled from docker hub. 
```bash
docker-compose up -d
```

### Development 
For development mode, all source code will be compiled and packaged as a jar. These jar files will be used later for 
creating the image for every service. To build, use this command:
```$xslt
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up
```

### Important Endpoint *
* [http://localhost:80](http://localhost:80) - Gateway
* [http://localhost:8761](http://localhost:8761) - Eureka Dashboard
* [http://localhost:9000/hystrix](http://localhost:9000/hystrix) - Hystrix Dashboard (paste Turbine stream link on the form)
* [http://localhost:8989](http://localhost:8989) - Turbine stream (source for the Hystrix Dashboard)
* [http://localhost:15672](http://localhost:15672) - RabbitMq management (default login/password: guest/guest)

### Kubernetes Deployment
[TBD]

## Contributing
[TBD]


*_This part is taken from [PiggyMetrics](https://github.com/sqshq/PiggyMetrics) with some adjustment because there is no significant differences in the way I use it_



