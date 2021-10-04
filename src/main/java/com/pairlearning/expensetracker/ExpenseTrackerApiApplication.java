package com.pairlearning.expensetracker;

import com.pairlearning.expensetracker.filters.AuthFilter;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@SpringBootApplication //convenience annotation to specify that this is a configuration class and also triggers auto-configuration and component scanning
public class ExpenseTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApiApplication.class, args);
	}

//	@Bean
//	public FilterRegistrationBean<CorsFilter> corsFilter() { //so that our server doesn't block CORS (Cross Origin) request in case our client is running on a different server
//		FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		CorsConfiguration config = new CorsConfiguration();
//		config.addAllowedOrigin("http://localhost:5500"); //if the client is hosted on 5500
//		config.addAllowedHeader("*");
//		source.registerCorsConfiguration("/**", config); //for any path on the client
//		registrationBean.setFilter(new CorsFilter(source)); // this doesn't work
//		registrationBean.setOrder(0);
//		return registrationBean;
//	}

	@Bean
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
		FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
		AuthFilter authFilter = new AuthFilter();
		registrationBean.setFilter(authFilter);
		registrationBean.addUrlPatterns(Constants.API_BASE+"/*");
		return registrationBean;
	}
}
