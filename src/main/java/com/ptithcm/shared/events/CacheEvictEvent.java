package com.ptithcm.shared.events;

import org.springframework.context.ApplicationEvent;

public class CacheEvictEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;
    private final String cacheKey;

    public CacheEvictEvent(Object source, String cacheKey) {
        super(source);
        this.cacheKey = cacheKey;
    }

    public String getCacheKey() {
        return cacheKey;
    }
}
