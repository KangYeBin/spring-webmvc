# Spring Boot3 Web MVC 학습

## 프로젝트 초기 설정 시 추가 할 문법
- build.gradle 파일에 jsp 설정 추가
```
implementation 'org.apache.tomcat.embed:tomcat-embed-jasper'
implementation 'jakarta.servlet:jakarta.servlet-api'
implementation 'jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api'
implementation 'org.glassfish.web:jakarta.servlet.jsp.jstl'
```

```
# server port change
server.port=8181

# jsp view resolver setting
spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp
```