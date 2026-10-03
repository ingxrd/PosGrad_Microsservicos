package br.edu.infnet.arquitetura.turma.dto;

import java.util.Set;

public record TurmaResponse(
        Long id,
        String nome,
        boolean ativa,
        Set<Long> alunoIds) {   // ← alunoIds
}