/*
 * 
 * 1. O que é esta classe?
É uma classe de domínio (entidade JPA) que representa o conceito de "Aluno" dentro do sistema. Ela é anotada com @Entity e mapeada para uma tabela no banco de dados.

2. Para que ela serve?
Serve para modelar os dados de um aluno (id, nome, e-mail, data de nascimento e status ativo) e permitir que o JPA/Hibernate persistam e recuperem esses dados no banco. Também carrega as anotações de validação (@NotBlank, @Email, @Past, etc.) que garantem a integridade dos dados antes de salvar.

3. Por que criei ela?
Porque toda aplicação orientada a domínio precisa de uma representação do seu objeto principal. No nosso caso, o Aluno é a primeira funcionalidade real do projeto, e ela serve como modelo para as demais (Turma, Projeto, Comunicado). Além disso, foi a forma de revisar e consolidar o que já tínhamos feito na disciplina anterior (mapeamento JPA + Bean Validation), agora dentro de uma organização modular por domínio.
  */

package br.edu.infnet.arquitetura.aluno;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDateTime;


@Entity
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "O nome é obrigatório")
    private String nome;
    @NotBlank (message = "O e-mail é obrigatório")
    @Email(message = "O e-mail deve ser válido")
    private String email;
    @NotBlank(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve estarn o passado")
    private LocalDateTime dataNascimento;
    @NotNull
    private boolean ativo;

    public Aluno(){}

    public Aluno(Long id, String nome, String email, LocalDateTime dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDateTime dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
