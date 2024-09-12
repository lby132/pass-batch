package com.fastcampus.pass;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
//@EnableBatchProcessing
@SpringBootApplication
public class PassBatchApplication {

//	@Bean
//	public Step passStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//		return new StepBuilder("passStep", jobRepository)
//				.tasklet(new Tasklet() {
//					@Override
//					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//						System.out.println("Execute PassStep");
//						return RepeatStatus.FINISHED;
//					}
//						 }, transactionManager
//				)
//				.build();
//	}
//
//	@Bean
//	public Job passJob1(JobRepository jobRepository, Step step) {
//		return new JobBuilder("passJob", jobRepository)
//				.start(step)
//				.build();
//	}

	private final PlatformTransactionManager platformTransactionManager;

	public PassBatchApplication(PlatformTransactionManager platformTransactionManager) {
		this.platformTransactionManager = platformTransactionManager;
	}

	@Bean
	public static BeanDefinitionRegistryPostProcessor jobRegistryBeanPostProcessorRemover() {
		return registry -> registry.removeBeanDefinition("jobRegistryBeanPostProcessor");
	}

	@Bean
	public Job passJob(JobRepository jobRepository) {
		return new JobBuilder("passJob", jobRepository)
				.start(passStep(jobRepository))
				.build();
	}

	@Bean
	public Step passStep(JobRepository jobRepository) {
		return new StepBuilder("passStep", jobRepository)
				.<String, String>chunk(1000,platformTransactionManager)
				.reader(itemReader())
				.writer(itemWriter())
				.build();}

	public static void main(String[] args) {
		SpringApplication.run(PassBatchApplication.class, args);
	}

	@Bean
	public ItemReader<String> itemReader(){
		return new ItemReader<String>() {
			@Override
			public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
				return "Read OK";
			}
		};
	}

	@Bean
	public ItemWriter<String> itemWriter(){
		return strList -> {
			strList.forEach(
					str -> log.info("str: {}", str)
			);
		};
	}

}
