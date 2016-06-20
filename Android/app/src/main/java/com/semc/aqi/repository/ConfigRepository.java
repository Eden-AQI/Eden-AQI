package com.semc.aqi.repository;

public class ConfigRepository {

    private volatile static ConfigRepository instance;

    private ConfigRepository() {
    }

    public static ConfigRepository getInstance() {
        if (instance == null) {
            synchronized (ConfigRepository.class) {
                if (instance == null) {
                    instance = new ConfigRepository();
                }
            }
        }
        return instance;
    }
}
