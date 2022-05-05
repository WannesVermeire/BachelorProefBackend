package com.example.bachelorproefbackend;

import com.example.bachelorproefbackend.usermanagement.filestorage.FileStorageService;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@SpringBootApplication
@RestController
@Configuration

public class BachelorProefBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BachelorProefBackendApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	// CORS configuration
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowedHeaders("*")
						.allowedOrigins("http://localhost:3000", "http://localhost:19000");
			}
		};

	}

	// logging
	@Bean
	public CommonsRequestLoggingFilter logFilter(){
		CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
		filter.setIncludeQueryString(true);
		filter.setIncludePayload(true);
		filter.setMaxPayloadLength(10000);
		filter.setIncludeHeaders(true);
		filter.setAfterMessagePrefix("REQUEST DATA: ");
		return filter;
	}

	// file upload
	@Resource
	FileStorageService storageService;
	@Bean
	public void run()  {
		storageService.deleteAll();
		storageService.init();
	}

//	//https
//	@Bean
//	public ServletWebServerFactory servletContainer() {
//		//Enable SSL Trafic
//		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//			@Override
//			protected  void postProcessContext(Context context) {
//				SecurityConstraint securityConstraint = new SecurityConstraint();
//				securityConstraint.setUserConstraint("CONFIDENTIAL");
//				SecurityCollection collection = new SecurityCollection();
//				collection.addPattern("/*");
//				securityConstraint.addCollection(collection);
//				context.addConstraint(securityConstraint);
//			}
//		};
//
//		// Add HTTP to HTTPS redirect
//		tomcat.addAdditionalTomcatConnectors(httpToHttpsRedirectConnector());
//		return tomcat;
//	}
//
//	private Connector httpToHttpsRedirectConnector() {
//		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//		connector.setScheme("http");
//		connector.setPort(8081);
//		connector.setSecure(false);
//		connector.setRedirectPort(8443);
//		return connector;
//	}

}
