package br.edu.infnet.arquitetura.batch;

import br.edu.infnet.arquitetura.aluno.client.AlunoClient;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/*
 * 1. O que é esta classe?
 * É um ItemWriter do Spring Batch que, em vez de gravar em arquivo,
 * envia cada AlunoRequest para a API do aluno-service.
 *
 * 2. Para que ela serve?
 * Serve para materializar o resultado do processamento em lote:
 * cada item processado é enviado para a API REST do aluno-service,
 * que é o dono dos dados de aluno.
 *
 * 3. Por que criei ela?
 * Porque o Batch não deve gravar direto no banco do aluno-service.
 * Ele deve respeitar o ownership dos dados e chamar a API exposta
 * pelo aluno-service.
 *
 * IMPORTANTE: no Spring Batch 5.x, o método write recebe um Chunk<T>
 * (não mais uma List<T> como era no Spring Batch 4.x).
 */
@Component
public class AlunoApiWriter implements ItemWriter<AlunoRequest> {

    private final AlunoClient alunoClient;

    public AlunoApiWriter(AlunoClient alunoClient) {
        this.alunoClient = alunoClient;
    }

    @Override
    public void write(Chunk<? extends AlunoRequest> chunk) {
        for (AlunoRequest aluno : chunk) {
            System.out.println("Aluno enviado para a API: " + aluno);
            alunoClient.incluir(aluno);
        }
    }
}