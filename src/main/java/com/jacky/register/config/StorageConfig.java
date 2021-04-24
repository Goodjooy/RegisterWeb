package com.jacky.register.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("storage")
public class StorageConfig {
    private  String storagePath="./localStorage";

    public String getStoragePath(){
        return storagePath;
    }

    public void setStoragePath(String path){
        this.storagePath=path;
    }
}
