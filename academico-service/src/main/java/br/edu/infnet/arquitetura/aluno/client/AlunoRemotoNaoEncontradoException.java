package br.edu.infnet.arquitetura.aluno.client;

/*
 * 1. O que é esta classe?
 * É uma exceção customizada que estende RuntimeException.
 *
 * 2. Para que ela serve?
 * Serve para representar, no contexto do acadêmico-service, a situação em que
 * o aluno-service respondeu 404 (o aluno não existe do outro lado).
 *
 * 3. Por que criei ela?
 * Porque queremos que o TurmaService lide apenas com exceções do nosso domínio,
 * e não com FeignException ou outras exceções técnicas. É o AlunoGateway que
 * traduz a falha técnica (404) nessa exceção de negócio.
 */
public class AlunoRemotoNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AlunoRemotoNaoEncontradoException(Long id) {
        super("Aluno com ID " + id + " não foi encontrado no aluno-service.");
    }
}