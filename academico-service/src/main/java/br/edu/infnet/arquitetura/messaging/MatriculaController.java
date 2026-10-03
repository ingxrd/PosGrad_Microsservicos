package br.edu.infnet.arquitetura.messaging;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * 1. O que é esta classe?
 * É um @RestController que expõe um endpoint POST /matriculas para
 * disparar o envio de uma mensagem para a fila.
 *
 * 2. Para que ela serve?
 * Serve como gatilho de teste. Em vez de chamar o Producer diretamente
 * pelo TurmaService, expõe um endpoint REST que publica uma mensagem.
 * Útil para testar isoladamente a mensageria.
 *
 * 3. Por que criei ela?
 * Porque queremos testar a fila pelo Postman, sem depender de uma
 * matrícula real. É uma ferramenta de demonstração.
 */
@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    private final MatriculaProducer matriculaProducer;

    public MatriculaController(MatriculaProducer matriculaProducer) {
        this.matriculaProducer = matriculaProducer;
    }

    @PostMapping
    public ResponseEntity<Void> enviar(@RequestBody MatriculaMessage mensagem) {
        matriculaProducer.enviar(mensagem);
        return ResponseEntity.ok().build();
    }
}