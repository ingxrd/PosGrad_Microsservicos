# Resumo da Aula 03 — Separação em Serviços Independentes e Comunicação HTTP

## Parte Teórica

### 1. Contexto: onde estamos

* **Aula 01:** organizamos a aplicação por domínio (`aluno`, `turma`).
* **Aula 02:** os módulos começaram a se relacionar. `TurmaService` passou a usar `AlunoService` por meio de uma **chamada local**, na mesma JVM. Também introduzimos DTOs para controlar o que sai na API.
* **Aula 03:** damos o passo mais importante até agora: **separamos o contexto de `aluno` em uma segunda aplicação Spring Boot**. A chamada local passa a ser uma **chamada remota via HTTP**.

---

### 2. O que muda quando separamos em dois processos

* **Antes:** `TurmaService` chamava `AlunoService` diretamente, tudo na mesma JVM, mesmo deploy e mesma porta.
* **Agora:** `AlunoService` vive em **outro processo**, outra porta e outro deploy. A referência Java **não existe mais**.
* A comunicação passa a acontecer **pela rede**, via **HTTP**, trocando **JSON**.

> **Frase-chave:** *"Quando atravessamos uma fronteira de processo, deixamos de compartilhar objetos Java e passamos a compartilhar contratos."*

---

### 3. Consequências da separação

#### Contratos

O que atravessa a rede não é a entidade `Aluno`, mas um **DTO** (`AlunoResponse`).

O `academico-service` **não conhece** a entidade `Aluno` do `aluno-service`.

```text
academico-service
       │
       │ HTTP + JSON
       ▼
aluno-service
       │
       ▼
   AlunoResponse
```

#### Ownership dos dados

O `aluno-service` é o **dono dos dados de aluno**.

O `academico-service` **não deve** acessar diretamente o banco de dados de aluno. Para obter informações, deve **perguntar ao `aluno-service` via HTTP**.

#### Autonomia

O `aluno-service` pode mudar:

* sua persistência;
* suas regras de negócio;
* seu banco de dados;
* sua implementação interna.

Desde que mantenha o **contrato da API**, o consumidor não precisa mudar.

#### Relação JPA deixa de fazer sentido

O `@ManyToMany` entre `Turma` e `Aluno` fazia sentido quando ambos estavam dentro da mesma unidade de persistência.

Agora, como `Aluno` pertence a outro serviço, `Turma` guarda apenas uma **coleção de IDs de alunos**:

```java
Set<Long> alunos;
```

Quando precisar dos dados completos, o `academico-service` consulta o `aluno-service` via HTTP.

#### Acoplamento não é eliminado, é transformado

Antes:

> **Acoplamento de código/processo**

Agora:

> **Acoplamento de contrato**

O `academico-service` passa a depender de informações como:

* URL/endereço do serviço;
* formato da resposta;
* significado dos dados;
* contrato da API.

#### Novos problemas surgem

A comunicação pela rede introduz novos desafios:

* timeout;
* retry;
* circuit breaker;
* service discovery;
* API Gateway;
* observabilidade;
* versionamento de API;
* indisponibilidade do outro serviço.

#### Ganhos

A separação permite:

* evolução independente;
* deploy independente;
* isolamento entre serviços;
* escalabilidade separada.

#### Custos

Por outro lado, aumenta a complexidade:

* comunicação pela rede;
* latência;
* mais pontos de falha;
* mais configurações;
* observabilidade mais complexa.

---

### 4. Comunicação síncrona

Na comunicação **síncrona**:

1. O `academico-service` faz uma requisição.
2. O `aluno-service` processa a requisição.
3. O `academico-service` **espera a resposta**.
4. Só então continua seu processamento.

Esse é o modelo utilizado nesta aula, por meio do **OpenFeign**.

```text
academico-service
       │
       │ requisição HTTP
       ▼
aluno-service
       │
       │ resposta
       ▼
academico-service
       │
       ▼
continua o processamento
```

Mais adiante na disciplina veremos **comunicação assíncrona**, utilizando mensageria. Nesse modelo, a aplicação publica uma mensagem e pode continuar seu processamento **sem esperar imediatamente pela resposta do outro serviço**.

---

### 5. OpenFeign

**OpenFeign** é uma ferramenta do ecossistema **Spring Cloud** que simplifica chamadas HTTP entre serviços.

Ele permite representar a API de outro serviço por meio de uma **interface Java anotada**.

Por trás dessa interface existe uma **requisição HTTP real**.

#### Dependência

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

#### Habilitação

É necessário habilitar o Feign na aplicação com:

```java
@EnableFeignClients
```

geralmente na classe principal do Spring Boot.

---

### 6. Papel do DTO nesta aula

Na **Aula 02**, o DTO tinha como principal objetivo controlar **o que a API devolvia**.

Agora, ele ganha uma **segunda função**:

> Representar o **contrato** que uma aplicação conhece sobre a outra.

O `academico-service` cria um `AlunoResponse` (`record`) que representa **o que ele espera receber** do `aluno-service`.

Por exemplo:

```java
public record AlunoResponse(
    Long id,
    String nome
) {
}
```

O `academico-service` não precisa conhecer a entidade completa utilizada internamente pelo `aluno-service`.

### Exemplo

Se internamente o `aluno-service` possui:

```text
Aluno
├── id
├── nome
├── dataNascimento
├── cpf
├── ativo
└── ...
```

Mas sua API retorna:

```json
{
  "id": 1,
  "nome": "Ingrid"
}
```

O consumidor conhece apenas esse contrato.

Se a tabela `aluno` mudar, mas a API continuar retornando o mesmo contrato, o `academico-service` **não precisa mudar**.

---

### 7. Externalização de URL

A URL do `aluno-service` **não deve** ficar escrita diretamente no código.

Em vez disso, deve ser configurada no `application.properties`.

Isso permite alterar o endereço conforme o ambiente:

```text
Desenvolvimento → localhost
Homologação     → servidor de homologação
Produção        → servidor de produção
```

### `application.properties`

```properties
aluno.service.url=http://localhost:8081
```

### `@FeignClient`

A URL pode então ser utilizada por meio de uma propriedade:

```java
@FeignClient(
    name = "aluno-service",
    url = "${aluno.service.url}"
)
public interface AlunoClient {
}
```

Dessa forma, a configuração do ambiente fica separada do código da aplicação.

---

## Resumo da evolução

```text
Aula 01
Organização por domínio
        ↓
Aula 02
Módulos + dependência controlada + DTO
        ↓
Aula 03
Serviços independentes + HTTP + JSON
        ↓
Futuro
Comunicação distribuída + mensageria + demais padrões de microsserviços
```

> **Ideia central da aula:** ao separar aplicações, deixamos de compartilhar objetos Java e passamos a compartilhar **contratos por meio da rede**. A partir desse momento, a comunicação, a disponibilidade e o contrato da API passam a fazer parte da arquitetura.
