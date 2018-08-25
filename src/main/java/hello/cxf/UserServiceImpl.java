package hello.cxf;

import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {
	
	private Map<Long, UserDto> userMap = new HashMap<Long, UserDto>();
	
	public UserServiceImpl() {
		UserDto user = new UserDto();
		user.setUserId(10001L);
		user.setUsername("Victor");
		user.setEmail("xxx@xxx.com");
		userMap.put(user.getUserId(), user);

		user = new UserDto();
		user.setUserId(10002L);
		user.setUsername("Sam");
		user.setEmail("yyy@yyy.com");
		userMap.put(user.getUserId(), user);

		user = new UserDto();
		user.setUserId(10003L);
		user.setUsername("Patty");
		user.setEmail("zzz@zzz.com");
		userMap.put(user.getUserId(), user);

	}

	@Override
	public String getName(Long userId) {
		return "liyd-" + userId;
	}

	@Override
	public UserDto getUser(Long userId) {
		return userMap.get(userId);
	}

}
