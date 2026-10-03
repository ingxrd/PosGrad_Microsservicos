package br.edu.infnet.arquitetura.aluno;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

/*
 * 1. O que é esta classe?
 * É uma classe de domínio (entidade JPA) que representa o conceito de "Aluno"
 * dentro do aluno-service. Ela é anotada com @Entity e mapeada para uma tabela
 * no banco de dados.
 *
 * 2. Para que ela serve?
 * Serve para modelar os dados de um aluno (id, nome, e-mail, data de nascimento
 * e status ativo) e permitir que o JPA/Hibernate persistam e recuperem esses
 * dados no banco. Também carrega as anotações de validação (@NotBlank, @Email,
 * @Past, etc.) que garantem a integridade dos dados antes de salvar.
 *
 * 3. Por que criei ela?
 * Porque o aluno-service é o dono dos dados de aluno. Ele precisa da entidade
 * para representar esse domínio e expor a API que o academico-service vai
 * consumir via HTTP.
 */
@Entity
@Table(name = "aluno")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail deve ser válido")
    private String email;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve estar no passado")
    private LocalDate dataNascimento;

    @NotNull
    private boolean ativo;

    public Aluno() {
    }

    public Aluno(String nome, String email, LocalDate dataNascimento) {
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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return String.format(
                "Aluno{id=%d, nome='%s', email='%s', ativo=%s}",
                id, nome, email, ativo);
    }
}