package br.edu.infnet.arquitetura.batch;

public class AlunoBatch {

    private Long id;
    private String nome;
    private String email;
    private String ativo;

    public AlunoBatch() {
    }

    public AlunoBatch(Long id, String nome, String email, String ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.ativo = ativo;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAtivo() { return ativo; }
    public void setAtivo(String ativo) { this.ativo = ativo; }

    @Override
    public String toString() {
        return "AlunoBatch{id=" + id + ", nome='" + nome + "', email='" + email + "', ativo='" + ativo + "'}";
    }
}

/*
* Não é entidade JPA — é apenas um objeto de transporte do Batch.

Tem construtor vazio — o Spring Batch usa reflection (JavaBeans) para preencher os campos.

Tem construtor com todos os campos — para o Processor criar o novo objeto.

toString — para log.


*
*
* */