package io.j0a0m4.otp.domain.exceptions;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.Map;

public abstract class DetailableException extends RuntimeException {

    @NonNull
    final private String type;

    @NonNull
    final private String title;

    @NonNull
    final private String detail;

    @Nullable
    final private Map<String, ?> metadata;

    public DetailableException(
            @NonNull String type,
            @NonNull String title,
            @NonNull String detail,
            @Nullable Map<String, ?> metadata
    ) {
        super("%s: %s".formatted(title, detail));
        this.type = type;
        this.title = title;
        this.detail = detail;
        this.metadata = metadata;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getDetail() {
        return detail;
    }

    @NonNull
    public Map<String, ?> getMetadata() {
        if (metadata == null) {
            return Collections.emptyMap();
        }
        return metadata;
    }

}
