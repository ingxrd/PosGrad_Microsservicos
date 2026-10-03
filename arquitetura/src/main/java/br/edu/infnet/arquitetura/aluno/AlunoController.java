/*
 * 1. O que é esta classe?
 * É uma classe anotada com @RestController e @RequestMapping("/alunos"),
 * responsável por expor os endpoints HTTP da funcionalidade Aluno.
 *
 * 2. Para que ela serve?
 * Serve para receber as requisições HTTP, delegar a execução ao AlunoService
 * e devolver as respostas adequadas (JSON, status HTTP, etc.). Ela define os endpoints:
 *
 *   POST   /alunos
 *   GET    /alunos
 *   GET    /alunos/{id}
 *   PUT    /alunos/{id}
 *   DELETE /alunos/{id}
 *   GET    /alunos/ativos
 *   GET    /alunos/buscar?nome=...
 *   GET    /alunos/email?email=...
 *
 * 3. Por que criei ela?
 * Porque a aplicação precisa expor suas funcionalidades via API REST.
 * O Controller é a porta de entrada da aplicação. Ao mantê-lo enxuto
 * (apenas recebendo e delegando), garantimos que a regra de negócio fique
 * no Service e que o Controller cuide apenas da comunicação HTTP.
 * Isso também facilita testes e manutenção.
 */

package br.edu.infnet.arquitetura.aluno;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    // Injeção de dependência via construtor
    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    // ---------- INCLUIR ----------
    @PostMapping
    public ResponseEntity<Aluno> incluir(@Valid @RequestBody Aluno aluno) {
        Aluno incluido = alunoService.incluir(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(incluido);
    }

    // ---------- OBTER LISTA ----------
    @GetMapping
    public List<Aluno> obterLista() {
        return alunoService.obterLista();
    }

    // ---------- OBTER POR ID ----------
    @GetMapping("/{id}")
    public Aluno obterPorId(@PathVariable Long id) {
        return alunoService.obterPorId(id);
    }

    // ---------- ALTERAR ----------
    @PutMapping("/{id}")
    public Aluno alterar(
            @PathVariable Long id,
            @Valid @RequestBody Aluno aluno) {

        return alunoService.alterar(id, aluno);
    }

    // ---------- EXCLUIR ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        alunoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- OBTER ATIVOS ----------
    @GetMapping("/ativos")
    public List<Aluno> obterAtivos() {
        return alunoService.obterAtivos();
    }

    // ---------- BUSCAR POR NOME ----------
    @GetMapping("/buscar")
    public List<Aluno> obterPorNome(@RequestParam String nome) {
        return alunoService.obterPorNome(nome);
    }

    // ---------- BUSCAR POR EMAIL ----------
    @GetMapping("/email")
    public ResponseEntity<Aluno> obterPorEmail(@RequestParam String email) {
        return alunoService.obterPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}