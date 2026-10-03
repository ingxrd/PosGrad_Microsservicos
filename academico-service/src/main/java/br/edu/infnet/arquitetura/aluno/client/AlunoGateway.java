package br.edu.infnet.arquitetura.aluno.client;

import feign.FeignException;
import feign.RetryableException;
import org.springframework.stereotype.Component;

/*
 * 1. O que é esta classe?
 * É uma classe de integração (anotada com @Component) que faz a fronteira
 * entre a lógica de negócio do acadêmico-service e o aluno-service.
 *
 * 2. Para que ela serve?
 * Serve para executar a chamada HTTP ao aluno-service (via AlunoClient) e
 * TRADUZIR exceções técnicas (FeignException, RetryableException) em
 * exceções do domínio do acadêmico-service:
 *   - FeignException.NotFound → AlunoRemotoNaoEncontradoException
 *   - RetryableException      → AlunoServiceIndisponivelException
 *
 * 3. Por que criei ela?
 * Porque o TurmaService não deve conhecer detalhes do Feign. Ao criar essa
 * camada intermediária (Gateway), isolamos a tecnologia de integração e
 * mantemos a regra de negócio limpa. Não confundir com API Gateway — aqui
 * é uma classe de integração, não um componente de infraestrutura.
 */
@Component
public class AlunoGateway {

    private final AlunoClient alunoClient;

    public AlunoGateway(AlunoClient alunoClient) {
        this.alunoClient = alunoClient;
    }

    public AlunoResponse obterPorId(Long alunoId) {
        try {
            return alunoClient.obterPorId(alunoId);
        } catch (FeignException.NotFound e) {
            throw new AlunoRemotoNaoEncontradoException(alunoId);
        } catch (RetryableException e) {
            throw new AlunoServiceIndisponivelException();
        }
    }
}