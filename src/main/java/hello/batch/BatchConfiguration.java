package hello.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    BatchDecider batchDecider;

    @Bean
    public FlatFileItemReader<Person> flatReader() {
        return new FlatFileItemReaderBuilder<Person>()
            .name("personFlatItemReader")
            .resource(new ClassPathResource("sample-data.csv"))
            .delimited()
            .names(new String[]{"firstName", "lastName"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                setTargetType(Person.class);
            }})
            .build();
    }
    
    @Bean
    public JdbcCursorItemReader<Person> jdbcReader(DataSource dataSource) {
    	return new JdbcCursorItemReaderBuilder<Person>()
    			.name("personJdbcItemReader")
    			.sql("SELECT * FROM PEOPLE")
    			.dataSource(dataSource)
    			.saveState(false)
    			.rowMapper(new PersonMapper())
    			.build();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }
    
    @Bean
    public FlatFileItemWriter<Person> flatWriter() {
    	DelimitedLineAggregator<Person> delLineAgg = new DelimitedLineAggregator<>();
    	delLineAgg.setDelimiter(", ");
    	BeanWrapperFieldExtractor<Person> fieldExtractor = new BeanWrapperFieldExtractor<>();
    	fieldExtractor.setNames(new String[] {"firstName", "lastName"});
    	delLineAgg.setFieldExtractor(fieldExtractor);
    	return new FlatFileItemWriterBuilder<Person>()
    			.name("personFlatItemWriter")
    			.resource(new FileSystemResource("/tmp/sample-data.dat"))// classpathResource not working for flatFileWriter
    			.lineAggregator(delLineAgg).build();

    }

    @Bean
    public JdbcBatchItemWriter<Person> jdbcWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
        		.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
        		.sql("INSERT INTO PEOPLE (first_name, last_name) VALUES (:firstName, :lastName)")
        		.dataSource(dataSource)
        		.build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1, Step step2) { // don't know how job know step1 is step1(), step2 is step2(). could be inject by name.
    	BatchDecider batchDecider = new BatchDecider();
        return jobBuilderFactory.get("importUserJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .next(batchDecider).on("COMPLETED").to(step2)
            .end()
            .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Person> writer) {
        return stepBuilderFactory.get("step1")
            .<Person, Person> chunk(10)
            .reader(flatReader())
            .processor(processor())
            .writer(writer)
            .build();
    }
    
    @Bean
    public Step step2(JdbcCursorItemReader<Person> reader) {
    	return stepBuilderFactory.get("step2")
    			.<Person, Person> chunk(10)
                .reader(reader)
                .processor(processor())
                .writer(flatWriter())
                .build();
    }
    
    /******* config for memory save metadata *****/
    @Bean
    public ResourcelessTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public MapJobRepositoryFactoryBean mapJobRepositoryFactory(ResourcelessTransactionManager txManager)
            throws Exception {
        MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean(txManager);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public JobRepository jobRepository(MapJobRepositoryFactoryBean factory) throws Exception {
        return factory.getObject();
    }
    
    @Bean
    public JobExplorer jobExplorer(MapJobRepositoryFactoryBean factory) {
        return new SimpleJobExplorer(factory.getJobInstanceDao(), factory.getJobExecutionDao(),
                factory.getStepExecutionDao(), factory.getExecutionContextDao());
    }

    @Bean
    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }
    
}
