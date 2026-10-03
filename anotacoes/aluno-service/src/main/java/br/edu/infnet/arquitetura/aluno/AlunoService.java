package br.edu.infnet.arquitetura.aluno;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.infnet.arquitetura.aluno.dto.AlunoResponse;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public AlunoResponse incluir(Aluno aluno) {

        if (alunoRepository.findByEmail(aluno.getEmail()).isPresent()) {
            throw new IllegalArgumentException(
                    "Já existe um aluno com o e-mail informado."
            );
        }

        Aluno alunoIncluido = alunoRepository.save(aluno);

        return converterParaResponse(alunoIncluido);
    }

    public List<AlunoResponse> obterLista() {

        return alunoRepository
                .findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public AlunoResponse obterPorId(Long id) {

        Aluno aluno = obterEntidadePorId(id);

        return converterParaResponse(aluno);
    }

    public AlunoResponse alterar(Long id, Aluno aluno) {

        Aluno existente = obterEntidadePorId(id);

        alunoRepository
                .findByEmail(aluno.getEmail())
                .filter(outroAluno ->
                        !outroAluno.getId().equals(id))
                .ifPresent(outroAluno -> {
                    throw new IllegalArgumentException(
                            "Já existe outro aluno com o e-mail informado."
                    );
                });

        existente.setNome(aluno.getNome());
        existente.setEmail(aluno.getEmail());
        existente.setDataNascimento(aluno.getDataNascimento());
        existente.setAtivo(aluno.isAtivo());

        Aluno alunoAtualizado = alunoRepository.save(existente);

        return converterParaResponse(alunoAtualizado);
    }

    public void excluir(Long id) {

        Aluno existente = obterEntidadePorId(id);

        alunoRepository.delete(existente);
    }

    public List<AlunoResponse> obterAtivos() {

        return alunoRepository
                .findByAtivoTrue()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public List<AlunoResponse> obterPorNome(String nome) {

        return alunoRepository
                .findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public AlunoResponse obterPorEmail(String email) {

        Aluno aluno = alunoRepository.findByEmail(email).orElseThrow(() -> new AlunoEmailNaoEncontradoException(email));

        return converterParaResponse(aluno);
    }

    private Aluno obterEntidadePorId(Long id) {

        return alunoRepository
                .findById(id)
                .orElseThrow(() ->
                        new AlunoNaoEncontradoException(id)
                );
    }

    private AlunoResponse converterParaResponse(Aluno aluno) {

        return new AlunoResponse(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getDataNascimento(),
                aluno.isAtivo()
        );
    }
}