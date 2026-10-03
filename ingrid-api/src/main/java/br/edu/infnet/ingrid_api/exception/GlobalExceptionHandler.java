package br.edu.infnet.ingrid_api.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice -> centraliza o tratamento de excecoes de TODOS os
// controllers da aplicacao num lugar so. Assim os controllers ficam limpos,
// sem try/catch espalhado por todo lugar (so tem UM try/catch aqui dentro,
// representado pelos metodos @ExceptionHandler abaixo)
@RestControllerAdvice
public class GlobalExceptionHandler {

    // metodo auxiliar (privado) pra nao repetir a montagem do ErroResponse
    // em cada @ExceptionHandler. Cada handler so precisa passar o status
    // HTTP certo + a mensagem da excecao
    private ResponseEntity<ErroResponse> criarResposta(HttpStatus status, String mensagem){

        ErroResponse erro = new ErroResponse(
                status.value(),          // ex: 404, 409, 400
                status.getReasonPhrase(),// ex: "Not Found", "Conflict", "Bad Request"
                mensagem,                // mensagem da excecao (exception.getMessage())
                LocalDateTime.now()      // timestamp de quando o erro ocorreu
        );
        return ResponseEntity.status(status).body(erro);
    }

    // trata quando um recurso (ex: Comunicado) nao eh encontrado por id
    // -> devolve 404 NOT FOUND
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException exception){
        return criarResposta(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    // trata quando tentam criar um recurso com um id que ja existe
    // -> devolve 409 CONFLICT
    // IMPORTANTE: IdentificadorDuplicadoException tem que ser uma classe
    // PROPRIA (extends RuntimeException), separada de IllegalArgumentException.
    // Se ela fosse a mesma coisa (ou herdasse) IllegalArgumentException,
    // o Spring ficaria em duvida sobre qual @ExceptionHandler chamar
    // quando essa excecao fosse lancada -> erro "Ambiguous @ExceptionHandler"
    // (foi exatamente o erro que apareceu na inicializacao da aplicacao)
    @ExceptionHandler(IdentificadorDuplicadoException.class)
    public ResponseEntity<ErroResponse> tratarIdentificadorDuplicado(IdentificadorDuplicadoException exception){
        return criarResposta(HttpStatus.CONFLICT, exception.getMessage());
    }

    // trata argumento invalido genérico (ex: campo obrigatorio vazio,
    // valor fora do esperado etc) -> devolve 400 BAD REQUEST
    // esse fica responsavel SO pela IllegalArgumentException "pura" do Java,
    // nao pode ter outro metodo mapeado pro mesmo tipo de excecao aqui em cima
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> tratarArgumentoInvalido(IllegalArgumentException exception){
        return criarResposta(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}