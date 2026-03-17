package model;

public class DbConfig {

    private static String env(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isEmpty()) ? def : v;
    }

    // ★ ここが重要：デフォルトは touma-portfolio-db を見に行く
    public static final String DB_HOST = env("PORTFOLIO_DB_HOST", "touma-portfolio-db");
    public static final String DB_PORT = env("PORTFOLIO_DB_PORT", "3306");
    public static final String DB_NAME = env("PORTFOLIO_DB_NAME", "portfolio_db");
    public static final String DB_USER = env("PORTFOLIO_DB_USER", "test_user");
    public static final String DB_PASS = env("PORTFOLIO_DB_PASS", "test_pass");

    public static final String JDBC_URL =
            "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
            + "?characterEncoding=UTF-8"
            + "&serverTimezone=Asia/Tokyo"
            + "&useSSL=false"
            + "&allowPublicKeyRetrieval=true";
}