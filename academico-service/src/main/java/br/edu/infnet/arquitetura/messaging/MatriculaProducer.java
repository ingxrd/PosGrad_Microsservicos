package br.edu.infnet.arquitetura.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/*
 * 1. O que é esta classe?
 * É uma classe anotada com @Component que publica mensagens na fila
 * do RabbitMQ.
 *
 * 2. Para que ela serve?
 * Serve para enviar uma MatriculaMessage para a fila "matriculas".
 * Usa o RabbitTemplate, que faz a conversão do objeto Java para JSON
 * e envia para o broker.
 *
 * 3. Por que criei ela?
 * Porque o TurmaService precisa publicar uma mensagem quando uma matrícula
 * é realizada. Isolar essa responsabilidade em uma classe dedicada mantém
 * o código organizado e testável.
 */
@Component
public class MatriculaProducer {

    private final RabbitTemplate rabbitTemplate;

    public MatriculaProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviar(MatriculaMessage mensagem) {
        System.out.println("Mensagem enviada: " + mensagem);
        rabbitTemplate.convertAndSend("matriculas", mensagem);
    }
}