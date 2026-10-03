package br.edu.infnet.arquitetura.aluno.dto;

import java.time.LocalDate;

public record AlunoResponse(Long id, String nome, String email, LocalDate dataNascimento, boolean ativo){
	
}