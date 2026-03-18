package model;

public class DbConfig {

    public static final String DB_USER = "test_user";
    public static final String DB_PASS = "test_pass";

    public static final String JDBC_URL =
            "jdbc:mysql://localhost:3306/portfolio_db"
            + "?characterEncoding=UTF-8"
            + "&serverTimezone=Asia/Tokyo"
            + "&useSSL=false"
            + "&allowPublicKeyRetrieval=true";
}