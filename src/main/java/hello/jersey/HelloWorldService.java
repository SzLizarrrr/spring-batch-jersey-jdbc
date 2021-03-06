package hello.jersey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldService {
	
	@Value("${message:World}")
	private String msg;
	
	public String message() {
		return this.msg;
	}

}
