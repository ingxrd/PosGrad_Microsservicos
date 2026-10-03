package br.edu.infnet.ingrid_api.exception;

// as classes exception vao  ajudar na hroa da api pq poderemos retornar algum retono futur ocom elas
public class RecursoNaoEncontradoException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
