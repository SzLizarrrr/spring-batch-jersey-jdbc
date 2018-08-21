package hello.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import hello.jdbctemplate.PetController;

@Component
public class JerseyConfig extends ResourceConfig {
	
	public JerseyConfig() {
		register(GreetingController.class);
		register(PetController.class);
	}

}
