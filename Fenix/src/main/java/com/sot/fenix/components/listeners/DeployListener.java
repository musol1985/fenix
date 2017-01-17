package com.sot.fenix.components.listeners;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DeployListener {
	
	@EventListener({ContextRefreshedEvent.class})
    void contextRefreshedEvent() {
		//TODO proceso para revisar problemos de BD
    }
}
