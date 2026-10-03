package br.edu.infnet.arquitetura.exception;
import java.time.LocalDateTime;

/*
 * 1. O que é esta classe?
 * É um record que padroniza o formato da resposta de erro da API.
 *
 * 2. Para que ela serve?
 * Serve para que todas as respostas de erro da API tenham o mesmo formato:
 * status, erro, mensagem e dataHora. Sem ele, cada erro retornaria um JSON
 * diferente (ou o padrão feio do Spring).
 *
 * 3. Por que criei ela?
 * Porque sem um formato padronizado, os clientes da API teriam dificuldade
 * em interpretar os erros. Com o ErroResponse, todo erro segue o mesmo padrão.
 */



public record ErroResponse(int status, String erro, String mensagem, LocalDateTime dataHora) {
}
