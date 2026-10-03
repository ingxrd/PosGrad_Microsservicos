package br.edu.infnet.arquitetura.batch;

import java.time.LocalDate;

public class AlunoRequest {

    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private boolean ativo;

    public AlunoRequest() {}

    public AlunoRequest(String nome, String email, boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.dataNascimento = LocalDate.now().minusYears(10);
        this.ativo = ativo;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}