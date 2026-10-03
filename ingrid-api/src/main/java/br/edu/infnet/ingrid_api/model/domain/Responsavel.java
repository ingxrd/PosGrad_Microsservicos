package br.edu.infnet.ingrid_api.model.domain;

public class Responsavel extends Pessoa {
    //private String nome;
    //private String email;
    private String telefone;
    private boolean receberNotificacoes;

    // construtor faz nao quebrar na instanciaçao.
    public Responsavel(Long id,String nome, boolean receberNotificacoes, String telefone, String email) {
        super(id,nome,email); //-> ia para a classe Object. agora vai para Pessoa. adicionei os parametros pq vai pro
        this.receberNotificacoes = receberNotificacoes;
        this.telefone = telefone;
    }

    public Responsavel() {}

    @Override
    public String toString() {
        // %s ira receber as informacoes de nome e email que estao em Pessoa.
        return String.format("Responsavel {%s,telefone='%s', receber notificacoes='%s'}",
                super.toString(), //vai ate o toString de PESSOA p depois jogar ele pro %s
                telefone,
                receberNotificacoes ? "sim": "nao"
        );
    }


    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isReceberNotificacoes() {
        return receberNotificacoes;
    }

    public void setReceberNotificacoes(boolean receberNotificacoes) {
        this.receberNotificacoes = receberNotificacoes;
    }


}
