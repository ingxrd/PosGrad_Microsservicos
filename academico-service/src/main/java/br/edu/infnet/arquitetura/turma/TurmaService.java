package br.edu.infnet.arquitetura.turma;

import br.edu.infnet.arquitetura.aluno.client.AlunoGateway;
import br.edu.infnet.arquitetura.aluno.client.AlunoResponse;
import br.edu.infnet.arquitetura.turma.dto.TurmaResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final AlunoGateway alunoGateway;   // <-- Gateway em vez de Client

    public TurmaService(TurmaRepository turmaRepository, AlunoGateway alunoGateway) {
        this.turmaRepository = turmaRepository;
        this.alunoGateway = alunoGateway;
    }

    public TurmaResponse obterDetalhes(Long id) {
        return converterParaResponse(obterEntidadePorId(id));
    }

    public TurmaResponse obterPorId(Long id) {
        return converterParaResponse(obterEntidadePorId(id));
    }

    public List<TurmaResponse> obterLista() {
        return turmaRepository.findAll().stream()
                .map(this::converterParaResponse).toList();
    }

    public List<TurmaResponse> obterAtivas() {
        return turmaRepository.findByAtivaTrue().stream()
                .map(this::converterParaResponse).toList();
    }

    public List<TurmaResponse> obterPorNome(String nome) {
        return turmaRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::converterParaResponse).toList();
    }

    public TurmaResponse incluir(Turma turma) {
        return converterParaResponse(turmaRepository.save(turma));
    }

    public TurmaResponse alterar(Long id, Turma turma) {
        Turma existente = obterEntidadePorId(id);
        existente.setNome(turma.getNome());
        existente.setAtiva(turma.isAtiva());
        return converterParaResponse(turmaRepository.save(existente));
    }

    public void excluir(Long id) {
        turmaRepository.delete(obterEntidadePorId(id));
    }

    public TurmaResponse matricularAluno(Long turmaId, Long alunoId) {

        Turma turma = obterEntidadePorId(turmaId);

        // Agora usa o Gateway, que traduz as falhas técnicas
        AlunoResponse aluno = alunoGateway.obterPorId(alunoId);

        if (turma.getAlunoIds().contains(aluno.id())) {
            throw new IllegalArgumentException("O aluno já está matriculado nesta turma.");
        }

        turma.adicionarAluno(aluno.id());

        Turma turmaAtualizada = turmaRepository.save(turma);

        return converterParaResponse(turmaAtualizada);
    }

    private Turma obterEntidadePorId(Long id) {
        return turmaRepository.findById(id)
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