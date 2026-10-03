package br.edu.infnet.arquitetura.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BatchRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job importarAlunosJob;

    public BatchRunner(JobLauncher jobLauncher, Job importarAlunosJob) {
        this.jobLauncher = jobLauncher;
        this.importarAlunosJob = importarAlunosJob;
    }

    @Override
    public void run(String... args) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(importarAlunosJob, params);
    }
}