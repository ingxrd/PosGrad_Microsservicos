package br.edu.infnet.arquitetura.exception;

import java.time.LocalDateTime;

public record ErroResponse(int status, String erro, String mensagem, LocalDateTime dataHora){
	
}