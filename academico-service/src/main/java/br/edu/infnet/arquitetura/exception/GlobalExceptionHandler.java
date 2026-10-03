package br.edu.infnet.arquitetura.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.edu.infnet.arquitetura.aluno.client.AlunoRemotoNaoEncontradoException;
import br.edu.infnet.arquitetura.aluno.client.AlunoServiceIndisponivelException;
import br.edu.infnet.arquitetura.turma.TurmaNaoEncontradaException;

/*
 * 1. O que é esta classe?
 * É uma classe anotada com @RestControllerAdvice que centraliza o tratamento
 * de exceções lançadas pela aplicação.
 *
 * 2. Para que ela serve?
 * Serve para interceptar exceções específicas e convertê-las em respostas
 * HTTP padronizadas no formato ErroResponse:
 *   - MethodArgumentNotValidException  → 400 (erros de validação)
 *   - TurmaNaoEncontradaException      → 404
 *   - IllegalArgumentException         → 400 (ex.: aluno já matriculado)
 *   - AlunoRemotoNaoEncontradoException → 404 (aluno não existe no aluno-service)
 *   - AlunoServiceIndisponivelException → 503 (aluno-service fora do ar / timeout)
 *
 * 3. Por que criei ela?
 * Porque sem um handler global, o Spring usa o tratamento padrão (500 genérico),
 * que não distingue erro de negócio de erro de infraestrutura.
 * Além disso, ele fica no pacote exception porque é uma preocupação transversal.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErroResponse> criarResposta(HttpStatus status, String mensagem) {

        ErroResponse erro = new ErroResponse(
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarErroValidacao(MethodArgumentNotValidException exception) {

        String mensagem = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return criarResposta(HttpStatus.BAD_REQUEST, mensagem);
    }

    @ExceptionHandler(TurmaNaoEncontradaException.class)
    public ResponseEntity<ErroResponse> tratarTurmaNaoEncontrada(TurmaNaoEncontradaException exception) {
        return criarResposta(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> tratarArgumentoInvalido(IllegalArgumentException exception) {
        return criarResposta(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(AlunoRemotoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarAlunoRemotoNaoEncontrado(AlunoRemotoNaoEncontradoException exception) {
        return criarResposta(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(AlunoServiceIndisponivelException.class)
    public ResponseEntity<ErroResponse> tratarAlunoServiceIndisponivel(AlunoServiceIndisponivelException exception) {
        return criarResposta(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage());
    }
}