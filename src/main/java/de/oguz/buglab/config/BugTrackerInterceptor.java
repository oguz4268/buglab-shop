package de.oguz.buglab.config;

import de.oguz.buglab.service.BugTracker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

@Component
public class BugTrackerInterceptor implements HandlerInterceptor {

    public static final String HEADER_ACTIVE_BUGS = "X-Buglab-Active-Bugs";
    public static final String HEADER_BUG_REASONS = "X-Buglab-Bug-Reasons";

    private final BugTracker bugTracker;

    public BugTrackerInterceptor(BugTracker bugTracker) {
        this.bugTracker = bugTracker;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        writeHeaders(response);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        if (!response.containsHeader(HEADER_ACTIVE_BUGS)) {
            writeHeaders(response);
        }
    }

    private void writeHeaders(HttpServletResponse response) {
        if (bugTracker.isEmpty()) {
            return;
        }

        Set<String> ids = bugTracker.getTriggeredBugIds();

        if (ids.isEmpty()) {
            return;
        }

        response.setHeader(HEADER_ACTIVE_BUGS, String.join(",", ids));
        response.setHeader(
                HEADER_BUG_REASONS,
                ids.stream()
                        .map(id -> "BUG-" + id.toUpperCase())
                        .reduce((a, b) -> a + "," + b)
                        .orElse("")
        );
    }
}
