# graphql-spring-annotations

[graphql-java-annotations](https://github.com/Enigmatis/graphql-java-annotations) is a great library for creating a graphql schema based on POJOs and java annotations.
It simplifies the graphql schema creation and does not require creating a separated graphql schema and java types.

graphql-spring-annotations is based on **graphql-java-annotations** and [graphql-spring-boot](https://github.com/graphql-java-kickstart/graphql-spring-boot), and let
you create a spring server for you graphql application, using spring annotations to create your schema.

## How to use

first, add the following dependecies to your build.gradle:

```$xslt
dependencies {
    compile "io.github.enigmatis:graphql-spring-annotations:0.1.0"
}
``` 

Then, create your main ``Application`` class:

```java
package myAppPckg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"springAnno",
"myAppPckg"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

Notice that in your ``scanBasePackages`` you must supply `springAnno` package, which comes from the ``graphql-spring-boot`` project.
You also have to scan your package if it contains spring components.

Now you have to create your schema types.

Lets begin with the Query type definition (via graphql-java-annotations syntax):

```java
@Component
public class Query implements QueryRoot {
     @GraphQLField
     @GraphQLInvokeDetached
     public String helloWorld(){
         return "helloWorld";
     }   
}
```

This is a standard type definition using graphql-java-annotations syntax. The only difference is that we add the `@Component` annotation,
and implement `QueryRoot` interface (then it will be injected automatically into the schema).

You can create your `Mutation` and `Subscription` type in the same way: a graphql-java-annotations syntax, `@Component` annotation on top of the class, and implementing the `MutationRoot` or `SubscriptionRoot` interfaces accordingly.

If you would like to create directives, you can implement the `DirectiveDeclaration` interface in your directive definition class.

For example:

```java
@GraphQLName("upper")
@GraphQLDescription("upper")
@GraphQLDirectiveDefinition(wiring= UpperDirectiveWiring.class)
@DirectiveLocations({Introspection.DirectiveLocation.FIELD_DEFINITION, Introspection.DirectiveLocation.ARGUMENT_DEFINITION})
@Component
public class UpperDirectiveDefinition implements DirectiveDeclaration {
    private boolean isActive = true;
}
```

Notice that we also put the `@Component` annotation on top of the class and implemented `DirectiveDeclaration` interface. All the other things are required annotations of graphql-java-annotations (read in their docs).

Add a properties file (using the graphql-spring-boot settings), for example:

```yaml
spring:
  application:
    name: graphql-todo-app
  servlet:
    multipart:
      enabled: true
      location: /tmp
server:
  port: 8080
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
graphql:
  servlet:
    tracing-enabled: false
    actuator-metrics: false
    contextSetting: PER_REQUEST_WITH_INSTRUMENTATION
altair:
  enabled: true
  cdn:
    enabled: false
graphiql:
  enabled: true
  cdn:
    enabled: false
voyager:
  enabled: false
  cdn:
    enabled: false
graphql.playground:
  enabled: false
  cdn:
    enabled: false
```

And you can go run your Application class.
That's it.