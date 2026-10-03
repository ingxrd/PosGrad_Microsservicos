package br.edu.infnet.ingrid_api.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comunicados")
public class Comunicado implements Identificavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //cria o identificador automaticamente qnd for criado no banco
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String conteudo;

    private boolean publicado;

    private LocalDateTime dataPublicacao;

    @JsonIgnore // colocando json ignore pq ta estourando no endpoint, nao ta conseguindo deserializar tudo
    @Transient
    private Turma turma;


    //construtor vazio necessário para o JPA conseguir criar as entidades
    protected Comunicado() {
    }

    //construtor usado para criar um comunicado apenas com titulo e conteudo
    public Comunicado(String titulo, String conteudo) {
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    //mantendo a ordem original dos parâmetros
    public Comunicado(Long id, String titulo, String conteudo, boolean publicado, LocalDateTime dataPublicacao) {
        this(titulo,conteudo);
        //super();
       this.id = id;
        //this.titulo = titulo;
        this.publicado = publicado;
        //this.conteudo = conteudo;
        this.dataPublicacao = dataPublicacao;
    }


    @Override
    public String toString() {
        String nomeTurma = turma != null ? turma.getNome() : "Sem nome";

        return String.format(
                "Comunicado{titulo='%s', conteudo='%s', publicado=%s, dataPublicacao=%s,turma=%s}",
                titulo,
                conteudo,
                publicado ? "sim" : "nao",
                dataPublicacao,
                nomeTurma
        );
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public boolean isPublicado() {
        return publicado;
    }

    public void setPublicado(boolean publicado) {
        this.publicado = publicado;
    }

    public LocalDateTime getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDateTime dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}


// ======= AULA 06 =======

// DTO -> classe que serve para fazer a transferência apenas dos dados que você quer expor para o cliente,
// sem precisar levar todas as informações que a classe de domínio possui

//quando se está trabalhando com banco de dados, nem sempre é interessante
//apresentar todos os valores que uma classe tem internamente. e é aí que entra o DTO:
// uma estrutura separada da classe de domínio (como a classe Comunicado), usada só para carregar os
// dados necessários numa resposta ou requisição.


// Nas aulas 06 + 07 nós vamos ver:
/*
Introdução ao JPA — explicar o que é o mapeamento das entidades.
Criar o repository — a nova camada Repository.
Adaptar os services — trocar o uso do Map/BaseService para usar o JpaRepository.
Ver relacionamentos — um-para-muitos, muitos-para-um, e relacionamentos com herança.
Fazer as validações — usando o famoso Bean Validation.
Fazer consultas — como o findBy, onde dá pra montar buscas só com o nome dos campos, sem precisar escrever o método na mão.
Tentar se conectar com uma API externa — para fazer essa comunicação toda acontecer, no final.
*/