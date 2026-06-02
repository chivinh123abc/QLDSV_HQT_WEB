package com.ptithcm.shared.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ptithcm.shared.events.CacheEvictEvent;
import com.ptithcm.shared.services.RedisService;

@Component
public class CacheEventListener {

    private static final Logger logger = LoggerFactory.getLogger(CacheEventListener.class);

    @Autowired
    private RedisService redisService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCacheEvictEvent(CacheEvictEvent event) {
        redisService.delete(event.getCacheKey());
        logger.info("Auto-evicted cache key: [{}] after successful DB commit.", event.getCacheKey());
    }
}
