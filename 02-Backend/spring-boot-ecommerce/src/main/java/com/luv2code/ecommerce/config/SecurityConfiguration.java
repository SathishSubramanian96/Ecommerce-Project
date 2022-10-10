package com.luv2code.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.okta.spring.boot.oauth.Okta;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// protect endpoint /api/orders
		http.authorizeRequests()
		           .antMatchers("/api/orders/**")
		           .authenticated()
		           .and()
		           .oauth2ResourceServer()
		           .jwt();
		
		//add CORS Filters
		http.cors();
		
		// force a non-empty response body for 401's to make the resposne more friendly
		Okta.configureResourceServer401ResponseBody(http);
		
		//disable CSRF since we are not using cookies for session tracking
		http.csrf().disable();
		
	}

	
}
