package com.sobad;

import com.sobad.util.DatabaseUtil;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AppRunner {
    @EventListener(ContextRefreshedEvent.class)
    public void onStartup() {
        DatabaseUtil.initTables();
    }
}