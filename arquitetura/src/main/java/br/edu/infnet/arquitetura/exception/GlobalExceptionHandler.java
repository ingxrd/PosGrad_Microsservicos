/*
 * 1. O que é esta classe?
É uma classe anotada com @RestControllerAdvice (ou @ControllerAdvice) que centraliza o tratamento de exceções lançadas pela aplicação.

2. Para que ela serve?
Serve para interceptar exceções específicas e convertê-las em respostas HTTP padronizadas. Por exemplo:

AlunoNaoEncontradoException → HTTP 404 com mensagem "Aluno não encontrado".

MethodArgumentNotValidException → HTTP 400 com a lista de campos inválidos e suas mensagens.

Outras exceções → HTTP 500 ou outro status adequado.

3. Por que criei ela?
Porque sem um handler global, cada Controller teria que tratar exceções individualmente, gerando código duplicado e respostas inconsistentes. O GlobalExceptionHandler:

Centraliza o tratamento de erros.

Padroniza o formato das respostas de erro.

Mantém os Controllers limpos.

É uma boa prática de arquitetura em APIs REST.

Além disso, ele fica em um pacote separado (exception) porque é uma preocupação transversal a todos os módulos, não pertence a um domínio específico.


 * */
package br.edu.infnet.arquitetura.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import br.edu.infnet.arquitetura.aluno.AlunoNaoEncontradoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private ResponseEntity<ErroResponse> criarResposta(HttpStatus status, String mensagem){
		
		ErroResponse erro = new ErroResponse(
				status.value(), 
				status.getReasonPhrase(), 
				mensagem, 
				LocalDateTime.now()
			);

		return ResponseEntity.status(status).body(erro);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroResponse> tratarErroValidacao(MethodArgumentNotValidException exception){
		
		String mensagem = exception
				.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
				.collect(Collectors.joining("; "));
		
		return criarResposta(HttpStatus.BAD_REQUEST, mensagem);
	}
	
	@ExceptionHandler(AlunoNaoEncontradoException.class)
	public ResponseEntity<ErroResponse> tratarAlunoNaoEncontrado(AlunoNaoEncontradoException exception){

		return criarResposta(HttpStatus.NOT_FOUND, exception.getMessage());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErroResponse> tratarArgumentoInvalido(IllegalArgumentException exception){

		return criarResposta(HttpStatus.BAD_REQUEST, exception.getMessage());
	}
}