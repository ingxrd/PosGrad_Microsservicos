package br.edu.infnet.ingrid_api.model.domain;

public class Professor extends Pessoa {
   // private String nome;
   // private String email;
    private String matricula;
    private boolean ativo;

    public Professor(Long id,String nome, String email, boolean ativo, String matricula) {
        super(id,nome,email);
        this.ativo = ativo;
        this.matricula = matricula;
    }

    public Professor() {}

    @Override
    public String toString() {
        return String.format("Professor {%s,matricula='%s', ativo='%s'}",
                    super.toString(),
                    matricula,
                    ativo ? "sim": "nao"
                );
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }


    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }


}

