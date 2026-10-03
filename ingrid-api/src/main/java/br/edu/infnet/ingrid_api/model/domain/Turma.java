package br.edu.infnet.ingrid_api.model.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Turma implements Identificavel{
    String nome;
    int anoLetivo;
    boolean ativa;
    private Escola escola;// estabelece relacionamento com escola
    private Long id;

    public Turma(){
    }

    List<Comunicado> comunidados = new ArrayList<>();


    // este metodo, toda vez que um metodo eh incluido, duas operacoes ocorrem
    // 1. eu add ela na lista de comunicados
    // 2. depois, ele vai fazer parte de uma turma
    // isso é bom para o encapsulamento, porque queremos que o proprio objeto seja responsavel pela manutencao do seu estado
    // e qualquer classe puder alterar livremente os seus relacionamentos rapidamente, vai surgir um monte de inconsistência difícil da gente identificar.
    // sempre que uma alteracao necessitar mais que uma operacao, devemos fazer isso.
    // relacionamento entre entidades (agregacoes, composicoes) ou quando as regras de negocio envolvem mais de um atributo
    // como turma e comunicado, por exemplo, devemos pensar ainda mais em encapsulamento.
    // este encapsulamento esta sendo feito justamente pelo comunicado.setTurma(this);

    public void adicionarComunicado(Comunicado comunicado){
        if (comunicado == null ) {
            throw new IllegalArgumentException("O comunicado nao pode ser nulo!");
        }
        comunidados.add(comunicado); // add um comunicado na minha lista de comunicados
        comunicado.setTurma(this); //  define esta turma como a turma do comunicado
    }




    public Turma(String nome, int anoLetivo, boolean ativa) {
        this.nome = nome;
        this.anoLetivo = anoLetivo;
        this.ativa = ativa;
    }

    @Override
    public String toString() {
        return String.format(
                "Turma{nome='%s', anoLetivo=%d, ativa=%s, qtdeComunicados=%d}",
                nome,
                anoLetivo,
                ativa ? "sim" : "nao",
                comunidados.size()
        );
    }

    //getters e setters

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(int anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public List<Comunicado> getComunidados() {
        return Collections.unmodifiableList(comunidados);
    }
    // qualquer pessoa pode substituir completamente a lista de comunicados.
    public void setComunidados(List<Comunicado> comunidados) {
        this.comunidados = comunidados;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
