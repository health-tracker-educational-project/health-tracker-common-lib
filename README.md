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
    <version>1.0.1</version>  
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
