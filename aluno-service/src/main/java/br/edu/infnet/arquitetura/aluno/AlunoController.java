package br.edu.infnet.arquitetura.aluno;

import jakarta.validation.Valid;
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

import java.util.List;

/*
 * 1. O que é esta classe?
 * É uma classe anotada com @RestController e @RequestMapping("/alunos"),
 * responsável por expor os endpoints HTTP do contexto de aluno.
 *
 * 2. Para que ela serve?
 * Serve para receber as requisições HTTP, delegar ao AlunoService e devolver
 * as respostas adequadas. Nenhuma regra de negócio fica aqui.
 *
 * 3. Por que criei ela?
 * Porque o aluno-service precisa expor sua API para que o academico-service
 * (e outros consumidores) possam consultar os dados de aluno via HTTP.
 * O endpoint GET /alunos/{id} é o que o Feign do academico-service vai chamar.
 */
@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public ResponseEntity<Aluno> incluir(@Valid @RequestBody Aluno aluno) {
        Aluno incluido = alunoService.incluir(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(incluido);
    }

    @GetMapping
    public List<Aluno> obterLista() {
        return alunoService.obterLista();
    }

    @GetMapping("/{id}")
    public Aluno obterPorId(@PathVariable Long id) {
        return alunoService.obterPorId(id);
    }

    @PutMapping("/{id}")
    public Aluno alterar(@PathVariable Long id, @Valid @RequestBody Aluno aluno) {
        return alunoService.alterar(id, aluno);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        alunoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ativos")
    public List<Aluno> obterAtivos() {
        return alunoService.obterAtivos();
    }

    @GetMapping("/buscar")
    public List<Aluno> obterPorNome(@RequestParam String nome) {
        return alunoService.obterPorNome(nome);
    }

    @GetMapping("/email")
    public ResponseEntity<Aluno> obterPorEmail(@RequestParam String email) {
        return alunoService.obterPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}