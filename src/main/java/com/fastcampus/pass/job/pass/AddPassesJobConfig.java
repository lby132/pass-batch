package com.fastcampus.pass.job.pass;

import com.fastcampus.pass.repository.pass.PassEntity;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class AddPassesJobConfig {

    private final AddPassesTasklet addPassesTasklet;

    private final PlatformTransactionManager platformTransactionManager;


    @Bean
    public Job addPassesJob(JobRepository jobRepository) {
        return new JobBuilder("addPassesJob", jobRepository)
                .start(addPassesJobStep(jobRepository))
                .build();
    }

    @Bean
    public Step addPassesJobStep(JobRepository jobRepository) {
        return new StepBuilder("addPassesJobStep", jobRepository)
                .tasklet(addPassesTasklet, platformTransactionManager)
                .build();
    }
}