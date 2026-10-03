package br.edu.infnet.arquitetura.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/*
 * 1. O que é esta classe?
 * É uma classe anotada com @Component que escuta a fila "matriculas"
 * e processa as mensagens recebidas.
 *
 * 2. Para que ela serve?
 * Serve para reagir a eventos de matrícula. Quando uma mensagem chega
 * na fila, o Spring chama automaticamente o método anotado com
 * @RabbitListener.
 *
 * 3. Por que criei ela?
 * Porque o consumidor é quem processa a mensagem. Nesta aula, apenas
 * imprimimos no console para demonstrar o funcionamento. Em um cenário
 * real, ele poderia gerar notificação, atualizar relatório, etc.
 */
@Component
public class MatriculaConsumer {

    @RabbitListener(queues = "matriculas")
    public void receber(MatriculaMessage mensagem) {
        System.out.println("Mensagem recebida: " + mensagem);
    }
}