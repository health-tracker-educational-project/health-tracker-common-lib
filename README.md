# Health Tracker common library.
## How to connect ?
### 1. Change base package directories of your service to this:

#### For main:
```
java.
  healthtracker.
          yourservice - here inside this package your controller, service packages
          YourServiceApplication - it's class with main method
```
#### For tests:
```
java.
  healthtracker.
          yourservice - here inside this package your test packages
```
### 2. Pull this library and build it with next command:

`mvn clean install`
### 3. Delete next packages from your service:

`config`
`wrapper`
`pagination`

### 4. Add maven dependency to your service:

```
<dependency>  
    <groupId>com.healthtracker</groupId>  
    <artifactId>healthtracker-common</artifactId>  
    <version>1.0.5</version>  
</dependency>
```
### 5. Add annotation @DependsOn("topics") on your controllers.

```
@DependsOn("topics")
@Component
class YourController {}
```

### 6. The following dependencies can be deleted from your service:

- kafka
- lombok

### 7. Add entity list to application.yaml. Thus, kafka creates your service topics:

#### application.yaml:
```
kafka:
  entities:
    - entity1(product, recipe, ...)
    - entity2
    - ...
```
#### @KafkaListener annotation(optional):
In **topics** parameter you need to pass topic name with next template:

`${kafka.topics.entity.action}`

For example:
```
@KafkaListener(topics = "${kafka.topics.product.list-request}")
```
**If you already have this do nothing!**
### (new) 8. Added annotation @TopicName for topic name generation on fields:
Usage: 
```
@TopicName(entity = "recipe", action = TopicAction.GET, operation = TopicOperation.REQUEST)
private String topic; 
```
Generated string: get-recipe-request