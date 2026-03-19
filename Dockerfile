# 1. ビルド用環境 (Maven) セクション
FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY WebContent ./WebContent
RUN mvn clean package

# 2. 実行用環境 (Tomcat) セクション
FROM tomcat:9.0-jdk17-openjdk-slim

# 画像保存用のディレクトリを作成（AppConfigのUPLOAD_DIRと合わせる）
RUN mkdir -p /img_mount && chmod 777 /img_mount

# 作成した server.xml をコンテナの設定フォルダに上書きコピー
COPY server.xml /usr/local/tomcat/conf/server.xml

RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder /app/target/portfolio.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]

