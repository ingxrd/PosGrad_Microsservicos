package br.edu.infnet.arquitetura.messaging;

/*
 * 1. O que é esta classe?
 * É um record que representa o formato da mensagem de matrícula
 * que circula entre o produtor e o consumidor pela fila do RabbitMQ.
 *
 * 2. Para que ela serve?
 * Serve para tipar a mensagem (em vez de mandar strings soltas),
 * facilitando a conversão para JSON (Jackson faz isso automaticamente)
 * e a leitura pelo consumidor.
 *
 * 3. Por que criei ela?
 * Porque a mensagem precisa ter uma estrutura clara e versionável.
 * O record já fornece construtor, getters, equals, hashCode e toString.
 */
public record MatriculaMessage(Long turmaId, Long alunoId) {
}