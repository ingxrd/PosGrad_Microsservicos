package br.edu.infnet.arquitetura.aluno;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
 * 1. O que é esta classe?
 * É uma interface que estende JpaRepository<Aluno, Long>. Ou seja, é um
 * repositório Spring Data JPA.
 *
 * 2. Para que ela serve?
 * Serve para abstrair o acesso ao banco de dados da entidade Aluno. Fornece
 * automaticamente os métodos CRUD básicos (save, findAll, findById, delete,
 * etc.) e permite declarar consultas derivadas.
 *
 * 3. Por que criei ela?
 * Porque o AlunoService não deve acessar o banco diretamente. O repositório
 * é a única porta de entrada para persistência de aluno.
 */
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    List<Aluno> findByAtivoTrue();

    List<Aluno> findByNomeContainingIgnoreCase(String nome);

    Optional<Aluno> findByEmail(String email);
}