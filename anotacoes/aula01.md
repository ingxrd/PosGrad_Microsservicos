# Aula 01

Anotações e projeto da disciplina **Arquiteturas Avançadas de Software com Microsserviços e Spring Framework**.

---

## 1. Mudança de perspectiva em relação à disciplina anterior

Na disciplina anterior, o foco estava em **como construir uma aplicação Spring Boot**, trabalhando conceitos como:

* Modelo de domínio
* Organização em camadas
* API REST
* Persistência com Spring Data JPA

Nesta disciplina, o foco passa a ser **como evoluir arquiteturalmente essa aplicação**, considerando:

* Organização e modularização
* Separação de responsabilidades
* Comunicação entre serviços
* Configuração para diferentes ambientes
* Processamento assíncrono
* Processamento em lote

A base desenvolvida anteriormente não é descartada. Ela será utilizada como **ponto de partida para um amadurecimento arquitetural**.

---

## 2. Objetivo central da disciplina

O objetivo não é utilizar novas tecnologias simplesmente por serem novas, mas compreender **por que cada decisão arquitetural é tomada**.

Cada recurso estudado deve responder a um problema real, como:

* **Microsserviços** → separação de responsabilidades e serviços independentes
* **Docker** → padronização e isolamento do ambiente de execução
* **Mensageria** → comunicação assíncrona entre componentes e serviços
* **Spring Batch** → processamento de grandes volumes de dados em lote

A disciplina está organizada em **4 etapas progressivas**, cada uma com entregas parciais registradas por meio de **tags no Git**.

---

## 3. As 4 etapas da disciplina

### 1. Organização arquitetural da aplicação

Antes de distribuir a aplicação, é necessário **organizá-la internamente**.

Nesta etapa:

* Modularização por domínio
* Revisão das responsabilidades de `Controller`, `Service` e `Repository`
* Consolidação das validações
* Tratamento de exceções
* Organização das consultas
* Documentação da API

> **Primeiro organizamos a aplicação; depois pensamos em distribuí-la.**

---

### 2. Separação e comunicação entre serviços

Nesta etapa, uma responsabilidade da aplicação será selecionada para ser extraída como um **serviço independente**.

Serão introduzidos conceitos como:

* APIs REST
* Contratos entre serviços
* DTOs
* OpenFeign
* Comunicação entre aplicações

A distribuição também traz novos desafios que não existem da mesma forma em uma aplicação monolítica, como:

* Comunicação pela rede
* Portas
* Indisponibilidade de serviços
* Latência
* Tratamento de falhas

---

### 3. Configuração e execução dos serviços

Com os serviços separados, torna-se necessário pensar também em **como configurá-los e executá-los em diferentes ambientes**.

Nesta etapa:

* Spring Profiles
* Variáveis de ambiente
* Configuração externa
* Banco de dados MySQL
* Docker
* Docker Compose
* Execução integrada dos serviços

---

### 4. Comunicação assíncrona e processamento em lote

A aplicação também pode precisar processar informações de outras maneiras além de requisições HTTP síncronas.

Nesta etapa serão estudados:

#### Mensageria

* Produtor e consumidor
* Filas
* Comunicação assíncrona
* Processamento de mensagens

#### Spring Batch

* `Job`
* `Step`
* `ItemReader`
* `ItemProcessor`
* `ItemWriter`

---

## 4. Conceitos arquiteturais fundamentais

### Responsabilidade

Cada parte da aplicação deve possuir uma função clara.

| Componente   | Responsabilidade  |
| ------------ | ----------------- |
| `Controller` | Comunicação HTTP  |
| `Service`    | Regras de negócio |
| `Repository` | Acesso aos dados  |

---

### Coesão

Elementos que possuem responsabilidades relacionadas devem permanecer próximos.

Na organização tradicional, os pacotes são separados por tecnologia:

```text
controller/
service/
repository/
model/
```

A proposta é organizar a aplicação também por **domínio**:

```text
aluno/
turma/
projeto/
comunicado/
```

Dessa forma, as classes relacionadas a um mesmo domínio permanecem próximas.

---

### Acoplamento

Acoplamento representa o grau de dependência entre diferentes responsabilidades da aplicação.

Quanto mais dependências desnecessárias existirem entre componentes, mais difícil pode ser modificá-los individualmente.

Isso não significa que **todo acoplamento seja ruim**. A questão é compreender se aquela dependência faz sentido arquiteturalmente.

---

### Módulo ≠ Microsserviço

Um módulo e um microsserviço não são a mesma coisa.

Vários módulos podem coexistir dentro da mesma aplicação Spring Boot, compartilhando:

* O mesmo processo
* A mesma JVM
* A mesma porta
* O mesmo deploy

Um **microsserviço**, por outro lado, representa uma aplicação ou serviço independente, com seu próprio processo de execução e ciclo de implantação.

---

## 5. Arquitetura como decisão

Arquitetura não começa necessariamente quando dividimos uma aplicação em microsserviços.

Mesmo em uma aplicação única, já existem decisões arquiteturais. Por exemplo:

```text
Controller
    ↓
Service
    ↓
Repository
```

O `Controller` não acessa diretamente o `Repository`, pois cada camada possui uma responsabilidade diferente.

Por isso, antes de separar uma aplicação em serviços, é necessário:

1. Entender a aplicação existente.
2. Identificar suas responsabilidades.
3. Encontrar fronteiras entre os domínios.
4. Avaliar o acoplamento existente.
5. Só então decidir o que deve ou não ser distribuído.

> **Não devemos reorganizar aquilo que ainda não entendemos.**

---

## 6. Evolução arquitetural do projeto

A proposta da disciplina pode ser resumida como uma evolução gradual:

```text
Aplicação Spring Boot
        ↓
Organização por domínio
        ↓
Separação de responsabilidades
        ↓
Serviços independentes
        ↓
Comunicação entre serviços
        ↓
Configuração e execução distribuída
        ↓
Comunicação assíncrona
        ↓
Processamento em lote
```

A ideia central é **pegar a base que já temos e evoluí-la para uma solução arquitetural mais madura**, entendendo os problemas que justificam cada mudança.

![resumo da aula 01](https://i.imgur.com/zIJb5iG.jpeg)
