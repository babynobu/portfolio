package model;

public class AppConfig {
    // Dockerコンテナ内の固定パス（server.xmlのdocBaseと合わせる）
    public static final String UPLOAD_DIR = "/img_mount";

    // URLパス（JSP表示用）
    public static final String IMAGE_PATH_PREFIX = "/img/profile/";
}
