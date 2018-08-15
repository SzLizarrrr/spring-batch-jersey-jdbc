package hello.batch;

import java.util.Random;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BatchDecider implements JobExecutionDecider {

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		
		if (new Random().nextInt() % 2 == 0) {
			log.info("Condition trigged, step 2 start");
			return FlowExecutionStatus.COMPLETED;
		}
		
		return FlowExecutionStatus.FAILED;
	}

}
