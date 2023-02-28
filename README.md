# slbk-spring  
  **Sift Log By Key, Working on MDC.**    


## Dependency  
**maven**:  
```xml
<dependency>
    <groupId>io.github.kerwin612</groupId>
    <artifactId>slbk-spring</artifactId>
    <version>0.0.3</version>
</dependency>
```
**gradle**:  
```groovy
implementation 'io.github.kerwin612:slbk-spring:0.0.3'
```  

## Configuration

**yaml**
```yaml
slbk:
  enabled: true         #Enabled by default, set to false to disable
  filterKey: log_key    #discriminator.key
```

**logback-spring.xml**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
	<include resource="org/springframework/boot/logging/logback/defaults.xml"/>

	<property name="logPath" value="./logs"/>

	<property name="LOG_PATTERN"
			  value="%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}"/>

	<appender name="LOG_BY_KEY" class="ch.qos.logback.classic.sift.SiftingAppender">
		<discriminator>
			<key>log_key</key><!--Equal to the `slbk.filterKey` configuration item in yaml-->
			<defaultValue>main</defaultValue>
		</discriminator>
		<sift>
			<appender name="LOG_BY_KEY" class="ch.qos.logback.core.rolling.RollingFileAppender">
				<file>${logPath}/${log_key}/log.txt</file>
				<encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
					<Pattern>${LOG_PATTERN}</Pattern>
				</encoder>
			</appender>
		</sift>
	</appender>

	<root>
		<appender-ref ref="LOG_BY_KEY" />
	</root>
</configuration>
```

> According to the above configuration, if the current **log_key** has no value, the log will be stored in the **./logs/main/log.txt** path, if there is a value, the log will be stored in the **./logs/${log_key}/log.txt** path

## Example  

**Default**
```java
public void example() {
    LOG.info("the log will be stored in the **./logs/main/log.txt** path");
}
```

**With Thread**
```java
new SLBKThread("example-thread", () -> {
    LOG.info("the log will be stored in the **./logs/example-thread/log.txt** path");
}).start();
```

**With Scheduled**
```java
@Scheduled(...)
@SLBKLogKey("example-scheduled")
public void scheduled() {
    LOG.info("the log will be stored in the **./logs/example-scheduled/log.txt** path");
}
```