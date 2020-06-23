package com.cognizant.purchase.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class SecurityConfigTest  extends WebSecurityConfigurerAdapter{
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("cognizant").password("{noop}admin").authorities("ADMIN")
				.and()
				.withUser("purchaseAdmin").password("{noop}purchase").authorities("PURCHASEADMIN")
				.and()
				.withUser("salesAdmin").password("{noop}sales").authorities("SALESADMIN");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		 http.httpBasic().and().authorizeRequests()
			.antMatchers("/cognizant/purchase/**").hasAuthority("PURCHASEADMIN")
			.antMatchers("/cognizant/sales/**").hasAuthority("SALESADMIN")
			.antMatchers("/cognizant").hasAuthority("ADMIN")
			 .and().csrf().disable();
	}
}
