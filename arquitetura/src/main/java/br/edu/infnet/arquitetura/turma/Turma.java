package br.edu.infnet.arquitetura.turma;

import br.edu.infnet.arquitetura.aluno.Aluno;
import org.hibernate.annotations.ManyToAny;

import java.util.ArrayList;
import java.util.List;

public class Turma {
    @ManyToAny
    private List<Aluno> alunos = new ArrayList<>();
}
