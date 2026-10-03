package br.edu.infnet.ingrid_api.controller;


import br.edu.infnet.ingrid_api.exception.RecursoNaoEncontradoException;
import br.edu.infnet.ingrid_api.model.domain.Comunicado;
import br.edu.infnet.ingrid_api.model_service.ComunicadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/comunicados")
public class ComunicadoController {

    private final ComunicadoService comunicadoService;

    public ComunicadoController(ComunicadoService comunicadoService) {
        this.comunicadoService = comunicadoService;
    }

    /**
     * ATENCAO: aqui estamos retornando a ENTIDADE Comunicado direto no corpo
     * da resposta (ResponseEntity<Comunicado>).
     *
     * Se Comunicado tiver relacionamento BIDIRECIONAL com outra entidade
     * (ex: Comunicado -> Autor -> List<Comunicado> -> Autor -> ...),
     * o Jackson (biblioteca que o Spring usa por padrão pra fazer a conversão entre objeto Java ↔ JSON.)
     * vai entrar em loop infinito ao serializar pra JSON
     * (StackOverflowError).
     *
     * SOLUCAO: trocar List<Comunicado> / Comunicado por um DTO
     * (ex: ComunicadoDTO), que representa so os campos que queremos
     * expor, sem referencia de volta pro objeto pai. Assim quebra o ciclo
     * na raiz, em vez de remendar com @JsonIgnore / @JsonManagedReference.
     */
    @GetMapping
    public ResponseEntity <List<Comunicado>> obterLista() {
        List<Comunicado> comunicados = comunicadoService.obterLista();
        return ResponseEntity.ok(comunicados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comunicado> obterPorId(@PathVariable Long id){
        try{
            Comunicado comunicado = comunicadoService.obterPorId(id);
            return ResponseEntity.ok(comunicado);
        } catch  (RecursoNaoEncontradoException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(params = "titulo")
    public ResponseEntity<List<Comunicado>> obterPorTitulo(@RequestParam String titulo){
        List<Comunicado> comunicados = comunicadoService.obterPorTitulo(titulo);
        return ResponseEntity.ok(comunicados);
    }

    /**
     * POST -> cria um novo recurso Comunicado.
     *
     * Alem de devolver 201 Created + o objeto criado no corpo, tambem
     * preenchemos o header "Location" da resposta, apontando pra URL
     * onde esse novo recurso pode ser acessado via GET.
     *
     * Isso segue a convencao REST: quando um POST cria um recurso, a
     * resposta 201 Created "deveria" vir com o header Location, pra
     * que o cliente saiba imediatamente onde buscar o recurso depois,
     * sem precisar montar a URL manualmente.
     *
     * ServletUriComponentsBuilder.fromCurrentRequest() pega a URL atual
     * da requisicao (ex: http://localhost:8080/comunicados) e:
     *   .path("/{id}")                      -> acrescenta "/{id}" no final
     *   .buildAndExpand(comunicado.getId()) -> troca {id} pelo id gerado
     *   .toUri()                            -> monta o objeto URI final
     *
     * Resultado (ex: id = 42):
     *   http://localhost:8080/comunicados/42
     *
     * Vantagem sobre so usar ResponseEntity.status(CREATED).body(...):
     * aquela versao tambem retorna 201, mas SEM o header Location,
     * entao o cliente nao fica sabendo onde o recurso criado pode
     * ser consultado depois.
     */
    @PostMapping
    public ResponseEntity<Comunicado> incluirComunicado(@RequestBody Comunicado comunicado){
        comunicadoService.incluir(comunicado);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()              // pega a URL atual da requisição (ex: http://localhost:8080/comunicados)
                .path("/{id}")                     // acrescenta "/{id}" no final
                .buildAndExpand(comunicado.getId()) // substitui {id} pelo id do comunicado recem criado
                .toUri();                          // transforma em objeto URI
        return ResponseEntity.created(location).body(comunicado);
        //return ResponseEntity.status(HttpStatus.CREATED).body(comunicado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comunicado> alterar(@PathVariable Long id, @RequestBody Comunicado comunicado){

        comunicado.setId(id);

        comunicadoService.alterar(comunicado);

        return ResponseEntity.ok(comunicado); // retorna um ok a alteracao aconteceu
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){

        comunicadoService.excluir(id);

        return ResponseEntity.noContent().build();
    }

}