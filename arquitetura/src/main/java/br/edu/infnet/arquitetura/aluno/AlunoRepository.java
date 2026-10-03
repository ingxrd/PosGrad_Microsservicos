/*
 * 1. O que é esta classe?
É uma interface que estende JpaRepository<Aluno, Long>. Ou seja, é um repositório Spring Data JPA.

2. Para que ela serve?
Serve para abstrair o acesso ao banco de dados da entidade Aluno. Ela fornece automaticamente os métodos CRUD básicos (save, findAll, findById, delete, etc.) e permite declarar consultas derivadas, como:

findByAtivo(Boolean ativo)

findByNomeContainingIgnoreCase(String nome)

findByEmail(String email)

3. Por que criei ela?
Porque a camada de serviço não deve acessar o banco diretamente. O repositório é a única porta de entrada para persistência. Além disso, ao colocá-lo dentro do pacote aluno, reforçamos a ideia de coesão por domínio: tudo que é do aluno fica junto. Isso também nos permite começar a identificar fronteiras e possíveis candidatos a serviço independente no futuro.


 * */
package br.edu.infnet.arquitetura.aluno;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    List<Aluno> findByAtivoTrue();
    List<Aluno> findByNomeContainingIgnoreCase(String nome);

    // busca por email
    Optional<Aluno> findByEmail(String email);

}
