# Aula 02 — Dependência entre módulos e DTOs

## 1. Contexto: onde estamos

* Na **Aula 01**, organizamos a aplicação por **domínio/responsabilidade** (`aluno`, `turma`), em vez de pacotes técnicos (`controller`, `service`, `repository`).
* Continuamos com **uma única aplicação Spring Boot**: mesma JVM, mesma porta, mesmo deploy.
* **Módulo ≠ Microserviço**. Ainda não estamos distribuindo nada.

### Frases-guia da disciplina

> Aula 01: *"Antes de distribuir, precisamos organizar."*

> Aula 02: *"Antes de separar, precisamos controlar como as responsabilidades se relacionam."*

---

## 2. Dependência entre módulos

* Uma **turma precisa conhecer seus alunos**. Logo, o módulo `turma` vai precisar usar algo do módulo `aluno`.
* **Dependência não é necessariamente ruim.** `Controller` depende de `Service`, `Service` depende de `Repository` — isso é esperado e adequado.
* O problema surge quando um módulo passa a **conhecer detalhes internos demais** de outro.

---

## 3. Fronteira entre módulos

Se `TurmaService` precisa saber se um aluno existe, ele **não deve** acessar `AlunoRepository` diretamente.

O correto é:

```text
TurmaService → AlunoService → AlunoRepository
```

### Por quê?

* O `AlunoRepository` é um detalhe interno de persistência do módulo `aluno`.
* Quem deve oferecer operações de negócio sobre aluno é o `AlunoService`.
* Assim, `turma` não precisa conhecer como `aluno` implementa sua persistência.

### Analogia

É como um setor de uma empresa:

> Em vez de invadir o arquivo do outro setor, você **pergunta ao setor responsável**.

### Benefício

Se o módulo `aluno` mudar sua forma de persistência, por exemplo, trocar JPA ou trocar o banco de dados, o módulo `turma` **não precisa saber**.

---

## 4. Serviço como ponto de colaboração

`TurmaService` chama `AlunoService`.

Neste momento, essa é uma **chamada local**:

* mesma JVM;
* sem rede;
* sem HTTP;
* dentro da mesma aplicação Spring Boot.

Quando, no futuro, `aluno` virar um serviço independente, essa chamada direta poderá se transformar em uma **chamada HTTP/REST**.

Ou seja:

```text
Agora:
TurmaService → AlunoService
       ↓
   chamada local

Futuro:
Serviço Turma → Serviço Aluno
       ↓
   HTTP/REST
```

O que fazemos agora, portanto, **prepara o terreno para uma futura separação em microserviços**.

---

## 5. DTO — Data Transfer Object

Até agora, usávamos as **próprias entidades** como entrada e saída da API.

Isso funciona em aplicações pequenas, mas apresenta alguns problemas:

* A entidade passa a ser, ao mesmo tempo:

    * modelo de domínio;
    * objeto persistido;
    * contrato da API.
* Pode expor dados que não deveriam sair, como `dataNascimento` ou `ativo` do aluno dentro de uma resposta de turma.
* Dificulta a evolução independente do modelo interno e do contrato externo.

### O que é DTO?

**DTO (Data Transfer Object)** é um objeto criado apenas para **transportar dados** entre partes da aplicação.

### Separação conceitual

| Objeto       | Responsabilidade                                                                |
| ------------ | ------------------------------------------------------------------------------- |
| **Entidade** | Representa como a aplicação **guarda** os dados (modelo interno).               |
| **DTO**      | Representa como a aplicação **apresenta ou troca** os dados (contrato externo). |

> Nesta aula, o professor focou apenas em **DTOs de resposta**. DTOs de requisição (`TurmaRequest`, `AlunoRequest`) ficam para depois.

---

## 6. `record` do Java

`record` é um recurso do Java moderno para representar **objetos portadores de dados**.

### Vantagens

O Java gera automaticamente:

* construtor;
* métodos de acesso aos componentes;
* `equals`;
* `hashCode`;
* `toString`.

Além disso:

* é **imutável por padrão**, pois seus componentes são `final`;
* é muito mais conciso que uma classe tradicional;
* combina muito bem com DTOs, já que DTOs servem principalmente para **transportar informações**.

### Importante

`record` **não é entidade**.

Ele:

* não recebe anotação JPA;
* não representa uma tabela do banco;
* não deve ser usado como modelo de persistência.

---

## 7. Conclusão teórica

Controlar **dependências entre módulos** e controlar **os dados que atravessam suas fronteiras** são duas ideias centrais da arquitetura.

* **DTO** ajuda a controlar **o que** atravessa a fronteira.
* **Service** ajuda a controlar **como** um módulo utiliza outro.
* Ainda não estamos trabalhando com microserviços: estamos **preparando a aplicação para uma futura separação**.

> **Resumo da aula:** antes de separar uma aplicação em microserviços, precisamos definir fronteiras claras, controlar as dependências entre módulos e evitar que um módulo conheça detalhes internos demais de outro.
