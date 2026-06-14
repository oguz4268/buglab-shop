package de.oguz.buglab.service;

import de.oguz.buglab.config.BugToggleProperties;
import org.springframework.stereotype.Service;

@Service
public class BugToggleService {

    private final BugToggleProperties bugToggleProperties;

    public BugToggleService(BugToggleProperties bugToggleProperties) {
        this.bugToggleProperties = bugToggleProperties;
    }

    public boolean isEnabled(String bugId) {
        if (bugId == null || bugId.isBlank()) {
            return false;
        }

        return bugToggleProperties.getBugs()
                .getOrDefault(bugId.trim().toLowerCase(), false);
    }
}