package com.ptithcm.shared.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ptithcm.shared.events.CacheEvictEvent;
import com.ptithcm.shared.services.RedisService;

@Component
public class CacheEventListener {

    @Autowired
    private RedisService redisService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCacheEvictEvent(CacheEvictEvent event) {
        redisService.delete(event.getCacheKey());
        System.out.println("Đã tự động xóa cache: " + event.getCacheKey() + " sau khi DB Commit thành công.");
    }
}
