package hello.jersey;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import hello.mybatis.User;
import hello.mybatis.UserDao;
import hello.mybatis.UserMapper;
import lombok.extern.slf4j.Slf4j;

@Component
@Path("/greet")
@Slf4j
public class GreetingController {

	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
	Job job;
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	UserDao userDao;

	private final HelloWorldService service;
	
	
	public GreetingController(HelloWorldService service) {
		this.service = service;
	}
	
	@GET
	@Path("/greeting")
	public String greeting() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobExecution jobExecution = jobLauncher.run(job, new JobParameters(maps));

		return jobExecution.getStatus().toString();
	}
	
	@GET
	@Path("/hello")
	public String message() {
		return "Hello " + this.service.message();
	}
	
	@GET
	@Path("/reverse")
	public String reverse(@QueryParam("input") @NotNull String input) {
		return new StringBuilder(input).reverse().toString();
	}
	
	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	public User findUser(@QueryParam("name") @NotNull String name) {
		log.info("find user by dao inject way: {}", userDao.selectUserByName(name));
		return userMapper.findByName(name);
	}
	
	@POST
	@Path("/user")
	@Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE) //don't know why, delete this line still work
	public int addUser(User user) {
		return userMapper.insert(user.getName(), user.getAge());
	}

}
