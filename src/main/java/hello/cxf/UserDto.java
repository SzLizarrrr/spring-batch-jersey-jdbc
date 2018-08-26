package hello.cxf;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class UserDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private String username;
	private String email;

}
