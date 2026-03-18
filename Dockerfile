# ベースイメージとしてopenjdkを使用
FROM eclipse-temurin:17-jdk-jammy

# Mavenのインストール
RUN apt-get update && apt-get install -y maven

# 作業ディレクトリを設定
WORKDIR /app

# プロジェクトファイルをコンテナにコピー
COPY . /app

# Mavenビルド
RUN mvn clean package -DskipTests

# アプリケーション実行
CMD ["java", "-jar", "target/SampleWeb-0.0.1-SNAPSHOT.jar"]

# 必要なポートを公開
# ymlファイルのappの右側のポートと合わせる
EXPOSE 9143
