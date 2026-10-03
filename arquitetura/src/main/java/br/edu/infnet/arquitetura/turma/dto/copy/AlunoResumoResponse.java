package br.edu.infnet.arquitetura.turma.dto.copy;

public record AlunoResumoResponse(Long id, String nome, String email) {

}


/*
* Pontos importantes:

converterParaResponse é privado — é detalhe interno do Service.

Usa stream().map(...).toList() para transformar List<Aluno> em List<AlunoResumoResponse>.

obterDetalhes é o método público que o Controller vai chamar.


* */