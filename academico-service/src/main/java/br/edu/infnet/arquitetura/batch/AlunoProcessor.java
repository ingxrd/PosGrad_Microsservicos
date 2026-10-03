package br.edu.infnet.arquitetura.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class AlunoProcessor implements ItemProcessor<AlunoBatch, AlunoBatch> {

    private static final Logger log = LoggerFactory.getLogger(AlunoProcessor.class);

    @Override
    public AlunoBatch process(AlunoBatch aluno) {

        AlunoBatch processado = new AlunoBatch();
        processado.setId(aluno.getId());
        processado.setNome(aluno.getNome().trim().toUpperCase());
        processado.setEmail(aluno.getEmail().trim().toLowerCase());
        processado.setAtivo(aluno.getAtivo());

        log.info("Processando: {} -> {}", aluno, processado);
        return processado;
    }
}

/*
*@Component — para que o Spring registre como Bean e o Step possa injetá-lo.

implements ItemProcessor<AlunoBatch, AlunoBatch> — entrada e saída são do mesmo tipo.

trim().toUpperCase() — remove espaços e coloca nome em maiúsculas.

trim().toLowerCase() — remove espaços e coloca e-mail em minúsculas.

O log mostra o "antes e depois" no console.


* */