package de.oguz.buglab.api;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class BugErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> attributes = super.getErrorAttributes(webRequest, options);

        Throwable error = getError(webRequest);

        if (error instanceof BugTriggeredException bug) {
            attributes.put("bugId", bug.getBugId());
            attributes.put("reason", bug.getBugReason());
        }

        return attributes;
    }
}
