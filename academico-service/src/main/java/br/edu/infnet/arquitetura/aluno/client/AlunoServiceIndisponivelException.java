package br.edu.infnet.arquitetura.aluno.client;

/*
 * 1. O que é esta classe?
 * É uma exceção customizada que estende RuntimeException.
 *
 * 2. Para que ela serve?
 * Serve para representar a situação em que o aluno-service está indisponível
 * (rede, connection refused, timeout, etc.).
 *
 * 3. Por que criei ela?
 * Porque essa falha é de INFRAESTRUTURA, não de negócio. Ela vai ser mapeada
 * para HTTP 503 (Service Unavailable) no GlobalExceptionHandler, comunicando
 * melhor o problema do que um 500 genérico.
 */
public class AlunoServiceIndisponivelException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AlunoServiceIndisponivelException() {
        super("Não foi possível acessar o aluno-service.");
    }
}