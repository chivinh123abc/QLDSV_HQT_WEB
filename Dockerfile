# === STAGE 1: Build mã nguồn với Maven & JDK 21 ===
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy file cấu hình maven, file format code và cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code và tiến hành build đóng gói ra file WAR
COPY src ./src
RUN mvn clean package -DskipTests -Dspotless.check.skip=true

# === STAGE 2: Môi trường chạy chính thức với Tomcat 10.1 & JDK 21 ===
FROM tomcat:10.1-jdk21
WORKDIR /usr/local/tomcat

# Xóa các ứng dụng mặc định của Tomcat để tránh xung đột routing ROOT
RUN rm -rf webapps/ROOT webapps/ROOT.war

# Copy file WAR đã build từ Stage 1 vào thư mục triển khai của Tomcat
COPY --from=builder /app/target/*.war webapps/ROOT.war
RUN mkdir -p webapps/ROOT && cd webapps/ROOT && jar -xf ../ROOT.war && rm ../ROOT.war

# Khởi chạy Tomcat server
EXPOSE 8080
CMD ["catalina.sh", "run"]
