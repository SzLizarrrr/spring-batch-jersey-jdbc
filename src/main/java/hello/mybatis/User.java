package hello.mybatis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class User {
	
	private Long id;
	private String name;
	private Integer age;

}
