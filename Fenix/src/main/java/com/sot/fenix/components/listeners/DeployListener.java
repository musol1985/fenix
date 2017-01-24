package com.sot.fenix.components.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DeployListener {
	final static Logger log = LogManager.getLogger(DeployListener.class);
	
	@EventListener({ContextRefreshedEvent.class})
    void contextRefreshedEvent() {
		log.debug("ieeeeeeeeeeeeeeeee->>>>>>>>>");
		//TODO proceso para revisar problemos de BD
    }
}
