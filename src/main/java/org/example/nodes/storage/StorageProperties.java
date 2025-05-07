package org.example.nodes.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nodes")
public class StorageProperties {
    /** uploads/posts */
    private String uploadDir;

    public String getUploadDir()            { return uploadDir; }
    public void setUploadDir(String uploadDir) { this.uploadDir = uploadDir; }
}
