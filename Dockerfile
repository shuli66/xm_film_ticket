FROM openjdk:17-jdk-slim

WORKDIR /app

# 将打包的jar文件复制到容器中
COPY springboot/target/springboot-0.0.1-SNAPSHOT.jar app.jar

# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

# 暴露应用端口
EXPOSE 9090

# 启动命令，添加生产环境配置
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=pro"]