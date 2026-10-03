package br.edu.infnet.ingrid_api.exception;

// classe cujo objetivo é.....
// ela herda do runtime exception
public class IdentificadorDuplicadoException extends RuntimeException{
    // sessao nao verificado significa que o compilador nao vai obrigar os metodos a declarar o throws ....essa excecao
    // tb nao exige que todas as chamadas usem o try catch

    private static final long serialVersionUID = 1L;

    public IdentificadorDuplicadoException(String mensagem) {
    // vai enviar a msg pro construtor da classe mae
        super(mensagem); // run time exception vai armazenar e disponibilizar a msg atraves do metodo getMensagem =)
    }
}
