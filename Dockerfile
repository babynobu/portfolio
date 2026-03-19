# 1. ビルド用環境 (Maven) セクションはすべて削除またはコメントアウト
# FROM maven:3.8.4-openjdk-17 AS builder ...などは不要です

# 2. 実行用環境 (Tomcat) だけにする
FROM tomcat:9.0-jdk17-openjdk-slim

# デフォルトのアプリを消す
RUN rm -rf /usr/local/tomcat/webapps/*

# 【ここを修正】
# ローカルで書き出したWARファイルを直接Tomcatにコピーします。
# もしファイル名が portfolio.war なら、以下のように書きます。
COPY target/portfolio.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
