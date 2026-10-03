package br.edu.infnet.arquitetura.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class AlunoProcessor implements ItemProcessor<AlunoBatch, AlunoRequest> {

    @Override
    public AlunoRequest process(AlunoBatch aluno) {
        if ("false".equalsIgnoreCase(aluno.getAtivo())) {
            return null;
        }

        return new AlunoRequest(
                aluno.getNome().trim().toUpperCase(),
                aluno.getEmail().trim().toLowerCase(),
                Boolean.parseBoolean(aluno.getAtivo())
        );
    }
}

/*
*@Component — para que o Spring registre como Bean e o Step possa injetá-lo.

implements ItemProcessor<AlunoBatch, AlunoBatch> — entrada e saída são do mesmo tipo.

trim().toUpperCase() — remove espaços e coloca nome em maiúsculas.

trim().toLowerCase() — remove espaços e coloca e-mail em minúsculas.

O log mostra o "antes e depois" no console.


* */