package br.edu.infnet.ingrid_api.model.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Escola  implements Identificavel{
    // o que eu preciso de Escola?
    private Long id;
    private String nome;
    private String cidade;
    private boolean ativa;
    private double avaliacao;

    // relacao 1 escola : varias turmas -> 1 to many

    private final List<Turma> turmas = new ArrayList<>();
    // construtor padrao

   public void adicinonarTurma(Turma turma){
       // add o parametro turma na coleçao de turmas da escola
       // marca a turma que vai ser add com a escola
       if (turma == null){
           throw new IllegalArgumentException("A turma nao pode ser nula!");
       }
       turmas.add(turma);
       turma.setEscola(this);
    }



    public Escola(){
        // esse cosntrutor esta vazio porque posso preencher as informaçoes com o que quiser vindo dos setters
    }

    // tostring serve para apresentar as informaçoes quando eu for imprimir o objeto
    @Override
    public String toString() {
        return String.format(
                "Escola{nome='%s', cidade='%s', ativa=%s, avaliacao=%.2f, qtdeTurmas=%d,}",
                nome,
                cidade,
                ativa ? "sim" : "nao",
                avaliacao,
                turmas.size()
        );
    }


    public Escola(Long id,String nome, boolean ativa, String cidade, double avaliacao) {
       super();
       this.id=id;
       this.nome = nome; // this.name -> atributo e nome; info contida dentro do construtor, algo que veio de forma atraves dos parametros do construtor.
        this.ativa = ativa;
        this.cidade = cidade;
        this.avaliacao = avaliacao;
    }


    // getters e setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public List<Turma> getTurmas() {
        return Collections.unmodifiableList(turmas);
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}


