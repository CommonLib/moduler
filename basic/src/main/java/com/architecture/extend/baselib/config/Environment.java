package com.architecture.extend.baselib.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by byang059 on 9/22/17.
 */

public class Environment {
    private static final Properties mProperties = new Properties();

    static {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream resourceStream = loader.getResourceAsStream(AppConfig.ENV_CONFIG_PATH);
            mProperties.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final String BASE_URL = mProperties.getProperty("API_BASE_URL");
}
