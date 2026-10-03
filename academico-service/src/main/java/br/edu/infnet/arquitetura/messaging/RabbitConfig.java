package br.edu.infnet.arquitetura.messaging;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * 1. O que é esta classe?
 * É uma classe de configuração do RabbitMQ.
 *
 * 2. Para que ela serve?
 * Serve para substituir o converter padrão (SimpleMessageConverter)
 * pelo Jackson2JsonMessageConverter, que serializa objetos Java em JSON
 * e desserializa JSON de volta para objetos Java.
 *
 * 3. Por que criei ela?
 * Porque o SimpleMessageConverter só aceita String, byte[] e Serializable.
 * Como estamos enviando um record (MatriculaMessage), precisamos do Jackson
 * para converter o objeto em JSON.
 */
@Configuration
public class RabbitConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}