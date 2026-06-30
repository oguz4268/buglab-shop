package de.oguz.buglab.api;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class BugTriggeredException extends ResponseStatusException {

    private final String bugId;

    public BugTriggeredException(HttpStatusCode status, String bugId, String reason) {
        super(status, reason);
        this.bugId = bugId;
    }

    public String getBugId() {
        return bugId;
    }

    public String getBugReason() {
        if (bugId == null || bugId.isBlank()) {
            return null;
        }
        return "BUG-" + bugId.toUpperCase();
    }
}
