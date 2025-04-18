package com.careerthon.assignment.security.filter;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum FilterExcludePath {
    LOGIN("/login"),
    SIGNUP("/signup"),
    SWAGGER_UI("/swagger-ui"),
    SWAGGER_UI_HTML("/swagger-ui.html"),
    SWAGGER("/swagger"),
    API_DOCS("/api-docs"),
    V3_API_DOCS("/v3/api-docs"),
    H2_CONSOLE("/h2-console");

    private final String path;

    FilterExcludePath(String path) {
        this.path = path;
    }

    public static boolean shouldExclude(String requestUri) {
        return Arrays.stream(FilterExcludePath.values())
                .anyMatch(p -> requestUri.startsWith(p.getPath()));
    }
}
