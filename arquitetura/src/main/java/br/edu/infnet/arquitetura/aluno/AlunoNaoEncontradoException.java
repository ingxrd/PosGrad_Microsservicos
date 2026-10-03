/*
 * 1. O que é esta classe?
É uma exceção customizada que estende RuntimeException.

2. Para que ela serve?
Serve para representar, de forma semântica, a situação em que um aluno não foi encontrado (por ID ou por e-mail). Em vez de retornar null ou lançar uma exceção genérica, usamos uma exceção específica do domínio.

3. Por que criei ela?
Porque o tratamento de erro faz parte da arquitetura. Ao criar uma exceção própria do domínio, ganhamos:

Clareza semântica (AlunoNaoEncontradoException diz exatamente o que aconteceu).

Possibilidade de tratá-la de forma específica no GlobalExceptionHandler, retornando um HTTP 404 com uma mensagem adequada.

Desacoplamento do serviço em relação a exceções genéricas do framework.


 * 
 * */
package br.edu.infnet.arquitetura.aluno;

public class AlunoNaoEncontradoException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public AlunoNaoEncontradoException(Long id){
        super("Aluno nao encontrado. ID: " + id);
    }
}
