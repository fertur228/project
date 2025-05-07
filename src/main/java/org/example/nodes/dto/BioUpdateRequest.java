// src/main/java/org/example/nodes/dto/BioUpdateRequest.java
package org.example.nodes.dto;

public class BioUpdateRequest {
    private String bio;           // только 1 поле

    public String getBio()  { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}
