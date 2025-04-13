#依赖jdk17环境
#FROM myopenjdk:17

#对外暴露9090
EXPOSE 9090
#复制构建好的jar包到docker容器中并命名为app.jar
ADD springboot-0.0.1-SNAPSHOT.jar app.jar
#执行命令
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=pro"]