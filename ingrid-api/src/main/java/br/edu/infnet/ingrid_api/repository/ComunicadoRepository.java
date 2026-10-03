package br.edu.infnet.ingrid_api.repository;

import br.edu.infnet.ingrid_api.model.domain.Comunicado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComunicadoRepository extends JpaRepository<Comunicado, Long> {
}


// criado como interface, e não como classe e
// por isso não é preciso implementar nenhum método na mão

// Ele usa herança entre interfaces (extends)
// para aproveitar tudo que o Spring Data JPA já oferece pronto.

// O JpaRepository<Comunicado, Long>
// precisa de dois tipos genéricos:
    // T (Comunicado) — a entidade do domínio, ou seja, a classe anotada com
    // @Entity que representa a tabela do banco. Como a classe de trabalho da aula é Comunicado, é ela que entra aqui.

    // ID (Long) — o tipo do campo chave primária dessa entidade, o mesmo tipo usado no atributo anotado com @Id dentro de Comunicado.


// só de herdar de JpaRepository, a interface já "ganha de graça" métodos como save, findAll, findById, existsById,
// count, deleteById, entre outros — sem precisar escrever nenhuma linha de implementação.

// Sobre a ausência de @Repository
//
//O professor chama atenção para um ponto importante: diferente do Service (@Service) ou do Controller
// (@RestController), aqui não é necessário anotar explicitamente com @Repository​ porque o próprio Spring Data JPA
// já identifica que essa interface estende JpaRepository e cria o componente correspondente automaticamente

// ==== A ARQUITETURA EXPLICADA ATE AGORA AULA 07 =======

/*
* 1. Controller cuida do HTTP,
* 2. Service cuida das regras de negócio
* 3. Repository é a camada de acesso aos dados — a abstração que conversa com o Hibernate/JPA para persistir e consultar informações no banco H2,
*   substituindo aos poucos aquele armazenamento provisório que era feito em um Map.

 */