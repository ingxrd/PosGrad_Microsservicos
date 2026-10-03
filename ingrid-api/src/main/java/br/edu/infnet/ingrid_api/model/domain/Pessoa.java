package br.edu.infnet.ingrid_api.model.domain;

// classe abstrata, p comportar infos em comum entre as outras classes


// ABSTRACT -> Classe abstrata apropriada quando existe mdiferentes classes que compartilham o mesmo comportamento.
// Exemplo: nome e email estavam duplicadas nas outras classes
// É necessario ter uma relacao conceitual importante, IS-A / HAS-A
// Classe nao sera instanciada diretamente. Implementacao parcial para as subclasses.

// Devemos evitar herança quando a rel entre as classes for um has-a (possui-um)
// Escla possui turmas, escola NAO é uma turma, por exemplo.


public abstract class Pessoa implements Identificavel {
    private Long id;
    private String nome;
    private String email;

    public Pessoa(){};

    public Pessoa(Long id,String nome, String email) {
        super();
        this.id = id;
        this.nome = nome;
        this.email = email;
    }



    @Override
    public String toString() {
        return String.format("{nome'%s', e-mail='%s'}",
                nome,
                email
        );
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

    // vem de IDENTIFICAVEL, nao precisa estar em Professor nem Responsavel pq esta na classe mae
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

