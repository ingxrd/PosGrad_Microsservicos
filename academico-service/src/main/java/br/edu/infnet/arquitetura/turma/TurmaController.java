package br.edu.infnet.arquitetura.turma;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.infnet.arquitetura.turma.dto.TurmaResponse;
import jakarta.validation.Valid;

/*
 * 1. O que é esta classe?
 * É uma classe anotada com @RestController e @RequestMapping("/turmas"),
 * responsável por expor os endpoints HTTP do contexto de turma.
 *
 * 2. Para que ela serve?
 * Serve para receber as requisições HTTP, delegar ao TurmaService e devolver
 * as respostas adequadas. Nenhuma regra de negócio fica aqui.
 *
 * 3. Por que criei ela?
 * Porque a aplicação precisa expor suas funcionalidades via API REST.
 * A partir da Aula 03, o Controller NÃO expõe mais a entidade Turma:
 * todos os endpoints retornam TurmaResponse (DTO). Isso mantém a separação
 * entre modelo interno (entidade) e contrato externo (DTO).
 * A exceção é o corpo da requisição de incluir/alterar, que ainda recebe
 * a entidade Turma (na Aula 03 ainda não criamos TurmaRequest).
 */
@RestController
@RequestMapping("/turmas")
public class TurmaController {

    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    // ---------- INCLUIR ----------
    @PostMapping
    public ResponseEntity<TurmaResponse> incluir(@Valid @RequestBody Turma turma) {
        TurmaResponse turmaResponse = turmaService.incluir(turma);
        return ResponseEntity.status(HttpStatus.CREATED).body(turmaResponse);
    }

    // ---------- OBTER LISTA ----------
    @GetMapping
    public ResponseEntity<List<TurmaResponse>> obterLista() {
        List<TurmaResponse> turmas = turmaService.obterLista();
        return ResponseEntity.ok(turmas);
    }

    // ---------- OBTER POR ID ----------
    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponse> obterPorId(@PathVariable Long id) {
        TurmaResponse turmaResponse = turmaService.obterPorId(id);
        return ResponseEntity.ok(turmaResponse);
    }

    // ---------- OBTER DETALHES ----------
    @GetMapping("/{id}/detalhes")
    public ResponseEntity<TurmaResponse> obterDetalhes(@PathVariable Long id) {
        TurmaResponse turmaResponse = turmaService.obterPorId(id);
        return ResponseEntity.ok(turmaResponse);
    }

    // ---------- ALTERAR ----------
    @PutMapping("/{id}")
    public ResponseEntity<TurmaResponse> alterar(@PathVariable Long id, @Valid @RequestBody Turma turma) {
        TurmaResponse turmaResponse = turmaService.alterar(id, turma);
        return ResponseEntity.ok(turmaResponse);
    }

    // ---------- EXCLUIR ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        turmaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- OBTER ATIVAS ----------
    @GetMapping("/ativas")
    public ResponseEntity<List<TurmaResponse>> obterAtivas() {
        List<TurmaResponse> turmas = turmaService.obterAtivas();
        return ResponseEntity.ok(turmas);
    }

    // ---------- BUSCAR POR NOME ----------
    @GetMapping("/buscar")
    public ResponseEntity<List<TurmaResponse>> obterPorNome(@RequestParam String nome) {
        List<TurmaResponse> turmas = turmaService.obterPorNome(nome);
        return ResponseEntity.ok(turmas);
    }

    // ---------- MATRICULAR ALUNO ----------
    @PostMapping("/{turmaId}/alunos/{alunoId}")
    public ResponseEntity<TurmaResponse> matricularAluno(
            @PathVariable Long turmaId,
            @PathVariable Long alunoId) {

        TurmaResponse turmaResponse = turmaService.matricularAluno(turmaId, alunoId);
        return ResponseEntity.ok(turmaResponse);
    }
}