package com.architecture.extend.baselib.config;

/**
 * Created by byang059 on 9/22/17.
 */

public class AppConfig {

    /**
     * default shared preferences file name
     */
    public static final String SHARED_FILE_NAME = "config";

    /**
     * acache max cache file size
     */
    public static final int CACHE_MAX_SIZE = 1024 * 1024 * 50; // 50M

    /**
     * okhttp response max cache
     */
    public static final int HTTP_CACHE_MAX_SIZE = 1024 * 1024 * 50; // 50M

    /**
     * config file store path
     */
    public static final String ENV_CONFIG_PATH = "assets/config.env";

    /**
     * API and Data Access Stuff
     */
    public static final int API_CONNECT_TIMEOUT = 10; //10 seconds
    public static final int API_WRITE_READ_TIMEOUT = 15; //15 seconds
}
