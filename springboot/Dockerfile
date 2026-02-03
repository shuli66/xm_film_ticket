# 多阶段构建 - 第一阶段: 构建应用
FROM maven:3.9-eclipse-temurin-17 AS builder

# 设置工作目录
WORKDIR /app

# 复制 pom.xml 并下载依赖(利用 Docker 缓存)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 构建应用(跳过测试以加快构建速度)
RUN mvn clean package -DskipTests -B

# 多阶段构建 - 第二阶段: 运行应用
FROM eclipse-temurin:17-jre-alpine

# 设置工作目录
WORKDIR /app

# 创建日志目录
RUN mkdir -p /app/logs

# 创建文件上传目录
RUN mkdir -p /usr/share/nginx/files

# 从构建阶段复制 JAR 文件
COPY --from=builder /app/target/springboot-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 9090

# 设置环境变量 - 激活 docker 配置文件
ENV SPRING_PROFILES_ACTIVE=docker

# 设置 JVM 参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
