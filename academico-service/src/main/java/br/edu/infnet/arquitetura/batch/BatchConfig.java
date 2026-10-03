package br.edu.infnet.arquitetura.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    // ---------- READER ----------
    @Bean
    public FlatFileItemReader<AlunoBatch> alunoReader() {
        return new FlatFileItemReaderBuilder<AlunoBatch>()
                .name("alunoReader")
                .resource(new ClassPathResource("batch/alunos.csv"))
                .linesToSkip(1)
                .delimited()
                .delimiter(";")
                .names("id", "nome", "email", "ativo")
                .targetType(AlunoBatch.class)
                .build();
    }

    // ---------- WRITER ----------
    @Bean
    public FlatFileItemWriter<AlunoBatch> alunoWriter() {
        return new FlatFileItemWriterBuilder<AlunoBatch>()
                .name("alunoWriter")
                .resource(new FileSystemResource("target/alunos-processados.csv"))
                .shouldDeleteIfExists(true)
                .delimited()
                .delimiter(";")
                .names("id", "nome", "email", "ativo")
                .headerCallback(writer -> writer.write("id;nome;email;ativo"))
                .build();
    }

    // ---------- STEP ----------
    @Bean
    public Step importarAlunosStep(JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager,
                                   FlatFileItemReader<AlunoBatch> alunoReader,
                                   AlunoProcessor alunoProcessor,
                                   AlunoApiWriter alunoApiWriter) {

        return new StepBuilder("importarAlunosStep", jobRepository)
                .<AlunoBatch, AlunoRequest>chunk(3, transactionManager)
                .reader(alunoReader)
                .processor(alunoProcessor)
                .writer(alunoApiWriter)
                .build();
    }

    // ---------- JOB ----------
    @Bean
    public Job importarAlunosJob(JobRepository jobRepository,
                                 Step importarAlunosStep) {
        return new JobBuilder("importarAlunosJob", jobRepository)
                .incrementer(new org.springframework.batch.core.launch.support.RunIdIncrementer())
                .start(importarAlunosStep)
                .build();
    }
}

/*Pontos importantes:

@Configuration — classe que define Beans de infraestrutura.

Reader:

new ClassPathResource("batch/alunos.csv") — lê o arquivo do classpath (src/main/resources/batch/alunos.csv).

linesToSkip(1) — pula o cabeçalho.

delimited().delimiter(";") — separador é ponto e vírgula.

names("id", "nome", "email", "ativo") — ordem dos campos na linha.

targetType(AlunoBatch.class) — classe que cada linha vira.

Writer:

new FileSystemResource("target/alunos-processados.csv") — gera o arquivo na pasta target (que não vai para o Git).

shouldDeleteIfExists(true) — apaga o arquivo se já existir (útil em demonstrações).

headerCallback(...) — escreve o cabeçalho na primeira linha.

Step:

<AlunoBatch, AlunoBatch>chunk(3, transactionManager) — processa em blocos de 3 itens (lê 3, processa 3, escreve 3, comita).

.reader(...).processor(...).writer(...) — encadeia os componentes.

Job:

.incrementer(new RunIdIncrementer()) — adiciona um run.id único a cada execução (evita erro de "Job instance already exists").

.start(importarAlunosStep) — define o Step inicial.


* */