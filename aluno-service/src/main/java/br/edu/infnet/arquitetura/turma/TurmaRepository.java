package br.edu.infnet.arquitetura.turma;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurmaRepository extends JpaRepository<Turma, Long> {

    List<Turma> findByAtivaTrue();

    List<Turma> findByNomeContainingIgnoreCase(String nome);
}