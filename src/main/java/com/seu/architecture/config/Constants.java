package com.seu.architecture.config;

import org.apache.catalina.TrackedWebResource;

public class Constants {

    public static final String OPEN_USER_STATUS = "open";
    public static final String LOCKED_USER_STATUS = "locked";

    public static final String IMAGE_URL = "http://115.159.49.46:8888/";
    public static final String THUMBNAIL = "Thumbnail";
    public static final String ORIGINAL_IMAGE = "originalImage";

//    public static final String IMAGE_PATH = "Y:\\scan2net\\images\\01 souvenir\\";
//    public static final String DATA_PATH = "Y:\\scan2net\\images\\01 souvenir\\图书扫描目录登记整理.xlsx";
//    public static final String IMG_PATH = "Y:\\scan2net\\images\\01 souvenir\\library\\img";
//    public static final String THUMBNAIL_PATH = "Y:\\scan2net\\images\\01 souvenir\\library\\thumbnail";

    public static final String IMAGE_PATH = "E:\\test\\";
    public static final String DATA_PATH = "E:\\test\\图书扫描目录登记整理.xlsx";
    public static final String IMG_PATH = "E:\\test\\library\\img";
    public static final String THUMBNAIL_PATH = "E:\\test\\library\\thumbnail";


    public static final String QUARTZ_PATH = "E:\\quartzJob\\";
    public static final String ADMIN_ROLE = "admin";
    public static final String PRIMARY_USER_ROLE = "primaryUser";
    public static final String MIDDLE_USER_ROLE = "middleUser";
    public static final String SENIOR_USER_ROLE = "seniorUser";

    public static final String UPLOAD_DIR = "E:/upload/";
    public static final String ORIGIN_IMAGE_DIR = UPLOAD_DIR + "database/";
    public static final String ORIGIN_THUMBNAIL_DIR = UPLOAD_DIR + "thumbnail/";
    public static final String DOMAIN = "http://127.0.0.1:8080/";
    public static final String IMAGE_NOT_FOUND = "not_found.jpg";

    public static final String ORIGIN_IMAGE = UPLOAD_DIR + "origin_image/";

    public static final int THUMBNAIL_WIDTH = 170;
    public static final int THUMBNAIL_HEIGHT = 140;
    public static String[] IMAGE_FILE_EXTD = new String[]{"png", "bmp", "jpg", "jpeg"};

    public static final String PDF_DIR = UPLOAD_DIR + "database/000274 ELEMENTARY WATER COLOUR PAINTING/";


    public static final String PROTOCOL = "http://";
    public static final String SEPARATOR = "/";
    public static final String TRACKER_NGINX_ADDR = "115.159.49.46";
    public static final String TRACKER_NGINX_PORT = "8888";
    public static final String CLIENT_CONFIG_FILE = "fdfs_client.conf";

    public static final String POPULAR_BOOK = "POPULAR";
    public static final String NEW_BOOK = "NEW";
    public static final String LIKE_BOOK = "LIKE";


    public static boolean isFileAllowed(String fileName) {
        for (String ext : IMAGE_FILE_EXTD) {
            if (ext.equals(fileName)) {
                return true;
            }
        }
        return false;
    }
}
