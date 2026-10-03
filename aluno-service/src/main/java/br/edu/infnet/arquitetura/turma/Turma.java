package br.edu.infnet.arquitetura.turma;

import br.edu.infnet.arquitetura.aluno.Aluno;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/*
* Relacionamento Muitos para Muitos (@ManyToMany) entre Turma e Aluno.

Não foi criada classe intermediária — o JPA gera a tabela turma_alunos automaticamente.

O lado Aluno não foi mapeado de volta (mappedBy não foi adicionado em Aluno nesta aula), para evitar loop de serialização JSON.

Em vez de setAlunos(...), criamos o método de domínio adicionarAluno(Aluno), que expressa a intenção do negócio.


* */
@Entity
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    private boolean ativa;

    @ManyToMany
    @JoinTable(
            name = "turma_aluno",
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    private List<Aluno> alunos = new ArrayList<>();

    public Turma() {
    }

    public Turma(String nome, boolean ativa) {
        this.nome = nome;
        this.ativa = ativa;
    }

    public void adicionarAluno(Aluno aluno) {

        alunos.add(aluno);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    @Override
    public String toString() {
        return String.format(
                "Turma{id=%d, nome='%s', ativa=%s}",
                id,
                nome,
                ativa
        );
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }
}