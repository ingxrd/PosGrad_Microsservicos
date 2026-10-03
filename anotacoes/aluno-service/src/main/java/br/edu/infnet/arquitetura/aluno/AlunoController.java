package br.edu.infnet.arquitetura.aluno;

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

import br.edu.infnet.arquitetura.aluno.dto.AlunoResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {

        this.alunoService = alunoService;
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> incluir(@Valid @RequestBody Aluno aluno) {

        AlunoResponse alunoResponse = alunoService.incluir(aluno);

        return ResponseEntity.status(HttpStatus.CREATED).body(alunoResponse);
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponse>> obterLista() {

        return ResponseEntity.ok(alunoService.obterLista());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponse> obterPorId(@PathVariable Long id) {

        return ResponseEntity.ok(alunoService.obterPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponse> alterar(@PathVariable Long id, @Valid @RequestBody Aluno aluno) {

        return ResponseEntity.ok(alunoService.alterar(id, aluno)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        alunoService.excluir(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<AlunoResponse>> obterAtivos() {

        return ResponseEntity.ok(alunoService.obterAtivos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<AlunoResponse>> obterPorNome(
            @RequestParam String nome) {

        return ResponseEntity.ok(alunoService.obterPorNome(nome));
    }

    @GetMapping("/email")
    public ResponseEntity<AlunoResponse> obterPorEmail(@RequestParam String email) {

        return ResponseEntity.ok(alunoService.obterPorEmail(email));
    }
}