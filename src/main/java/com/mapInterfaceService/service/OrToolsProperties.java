package com.mapInterfaceService.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "or-tools")
public class OrToolsProperties {

    private List<String> libPaths;

    public List<String> getLibPaths() {
        return libPaths;
    }

    public void setLibPaths(List<String> libPaths) {
        this.libPaths = libPaths;
    }
}
