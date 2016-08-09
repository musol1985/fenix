package com.sot.fenix.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.sot.fenix.components.providers.LoginProvider;
 
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	public static final String ROLE_ADMIN="ADMIN";
	public static final String ROLE_ROOT="ROOT";
	public static final String ROLE_USER="USER";
	
	@Autowired
	private LoginProvider loginProvider;
     
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(loginProvider);
    }
     
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       /*
      http.authorizeRequests()
        .antMatchers("/").permitAll()        
        .antMatchers("/app/admin/**").access("hasRole('"+ROLE_ADMIN+"')")
        .antMatchers("/app/root/**").access("hasRole('"+ROLE_ROOT+"')")
        .antMatchers("/app/**").access("hasRole('"+ROLE_USER+"')")
        .and().formLogin().loginPage("/login")
        .usernameParameter("ssoId").passwordParameter("password")
        .and().csrf()
        .and().exceptionHandling().accessDeniedPage("/app/denegado");*/
    	http
        	.authorizeRequests()
        		.antMatchers("/").permitAll() 
        		.antMatchers("/app/admin/**").hasRole(ROLE_ADMIN)
        		.antMatchers("/app/root/**").hasRole(ROLE_ROOT) 
        		.antMatchers("/app/**").hasRole(ROLE_USER) 
    	.and()
    		.formLogin()  
    			.loginPage("/login") 
    				.usernameParameter("ssoId").passwordParameter("password")
    	.and()
    		.csrf()
    	.and()
    		.exceptionHandling().accessDeniedPage("/app/denegado");
    }
}
