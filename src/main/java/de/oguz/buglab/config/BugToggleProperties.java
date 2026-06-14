package de.oguz.buglab.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "buglab")
public class BugToggleProperties {

    private Map<String, Boolean> bugs = new HashMap<>();

    public Map<String, Boolean> getBugs() {
        return bugs;
    }

    public void setBugs(Map<String, Boolean> bugs) {
        this.bugs = bugs;
    }
}