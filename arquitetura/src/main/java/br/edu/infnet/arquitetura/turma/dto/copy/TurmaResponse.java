package br.edu.infnet.arquitetura.turma.dto.copy;

import java.util.List;

public record TurmaResponse(Long id, String nome, boolean ativa, List<AlunoResumoResponse> alunos) {

}

/*
* representar uma visão resumida do aluno, usada apenas dentro da resposta de turma. Não expõe dataNascimento nem ativo.


 * */