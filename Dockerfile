FROM myopenjdk:17
WORKDIR /app
COPY target/springboot-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9090
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java","-jar","app.jar"] 