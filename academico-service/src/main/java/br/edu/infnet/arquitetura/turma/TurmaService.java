package br.edu.infnet.arquitetura.turma;

import br.edu.infnet.arquitetura.aluno.client.AlunoClient;
import br.edu.infnet.arquitetura.aluno.client.AlunoResponse;
import br.edu.infnet.arquitetura.turma.dto.TurmaResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final AlunoClient alunoClient;

    public TurmaService(TurmaRepository turmaRepository, AlunoClient alunoClient) {
        this.turmaRepository = turmaRepository;
        this.alunoClient = alunoClient;
    }

    // ---------- CONSULTAS ----------

    public TurmaResponse obterDetalhes(Long id) {
        return converterParaResponse(obterEntidadePorId(id));
    }

    public TurmaResponse obterPorId(Long id) {
        return converterParaResponse(obterEntidadePorId(id));
    }

    public List<TurmaResponse> obterLista() {
        return turmaRepository
                .findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public List<TurmaResponse> obterAtivas() {
        return turmaRepository
                .findByAtivaTrue()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public List<TurmaResponse> obterPorNome(String nome) {
        return turmaRepository
                .findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    // ---------- COMANDOS ----------

    public TurmaResponse incluir(Turma turma) {
        Turma turmaIncluida = turmaRepository.save(turma);
        return converterParaResponse(turmaIncluida);
    }

    public TurmaResponse alterar(Long id, Turma turma) {
        Turma existente = obterEntidadePorId(id);
        existente.setNome(turma.getNome());
        existente.setAtiva(turma.isAtiva());
        Turma atualizada = turmaRepository.save(existente);
        return converterParaResponse(atualizada);
    }

    public void excluir(Long id) {
        Turma existente = obterEntidadePorId(id);
        turmaRepository.delete(existente);
    }

    public TurmaResponse matricularAluno(Long turmaId, Long alunoId) {

        Turma turma = obterEntidadePorId(turmaId);

        AlunoResponse aluno = alunoClient.obterPorId(alunoId);

        if (turma.getAlunoIds().contains(aluno.id())) {
            throw new IllegalArgumentException("O aluno já está matriculado nesta turma.");
        }

        turma.adicionarAluno(aluno.id());

        Turma turmaAtualizada = turmaRepository.save(turma);

        return converterParaResponse(turmaAtualizada);
    }

    // ---------- PRIVADOS ----------

    private Turma obterEntidadePorId(Long id) {
        return turmaRepository
                .findById(id)
                .orElseThrow(() -> new TurmaNaoEncontradaException(id));
    }

    private TurmaResponse converterParaResponse(Turma turma) {
        return new TurmaResponse(
                turma.getId(),
                turma.getNome(),
                turma.isAtiva(),
                turma.getAlunoIds()
        );
    }
}