package de.oguz.buglab.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
@RequestScope
public class BugTracker {

    private final Set<String> triggeredBugIds = new LinkedHashSet<>();

    public void record(String bugId) {
        if (bugId == null || bugId.isBlank()) {
            return;
        }
        triggeredBugIds.add(bugId.trim().toLowerCase());
    }

    public Set<String> getTriggeredBugIds() {
        return Set.copyOf(triggeredBugIds);
    }

    public boolean isEmpty() {
        return triggeredBugIds.isEmpty();
    }
}
