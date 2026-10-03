package br.edu.infnet.arquitetura.turma;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    private boolean ativa;

    @ElementCollection
    @CollectionTable(
            name = "turma_aluno",
            joinColumns = @JoinColumn(name = "turma_id")
    )
    @Column(name = "aluno_id")
    private Set<Long> alunoIds = new HashSet<>();

    public Turma() {
    }

    public Turma(String nome, boolean ativa) {
        this.nome = nome;
        this.ativa = ativa;
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

    public Set<Long> getAlunoIds() {
        return alunoIds;
    }

    public void adicionarAluno(Long alunoId) {
        alunoIds.add(alunoId);
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
}