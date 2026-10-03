# Repository

Camada responsável por fazer o acesso aos dados — ou seja, é ela que
efetivamente conversa com o banco de dados, substituindo o trabalho
que antes era feito "na mão" através do `BaseService` em cima de um
`Map` em memória.

## Responsabilidades

1. **Disponibilizar métodos prontos de acesso a dados**: gravar,
   buscar, excluir, verificar se existe — as operações do CRUD, sem
   precisar escrever essas implementações na unha.
2. **Abstrair o trabalho braçal com persistência**, que antigamente
   era feito manualmente; o Spring Data JPA reduz bastante esse
   esforço.
3. **Ser a camada entre o Service e o banco físico**: o fluxo passa a
   ser `Controle → Service → Repository → Banco`, no lugar do antigo
   `Controle → Service → BaseService → Map`.
4. **Esconder/encapsular a execução real de SQL**: o banco continua
   existindo e o SQL continua sendo executado por baixo dos panos —
   o Repository só encapsula esse trabalho.

---

## JPA (Jakarta Persistence API)

O JPA é apenas uma **especificação**. Ou seja, ele define um conjunto
de regras e contratos, mas sozinho **não faz nada** — ele não é um
banco de dados e não sabe, por si só, como persistir informações.
Serve para a persistência de objetos Java em bancos de dados
relacionais.

Antigamente (Spring Boot 2.x ou anterior) usava-se `javax.persistence`;
hoje, no ecossistema Jakarta (Spring Boot 3.x), usa-se
`jakarta.persistence`.

É o JPA quem define as anotações usadas nas classes de domínio
(`@Entity`, `@Id`, `@GeneratedValue`, `@Column`, `@OneToMany`, etc.)
para transformar uma classe Java comum em uma "entidade JPA", que
pode ser mapeada para uma tabela do banco.

---

## Hibernate

O Hibernate é uma **implementação** da especificação JPA. Resumindo
a lógica:

- **JPA** → define **O QUE** deve ser feito (as regras)
- **Hibernate** → define **COMO** isso é feito de verdade

É o Hibernate quem:

- Lê as anotações (`@Entity`, `@Id`, `@GeneratedValue`, `@Column`...)
- Gera o SQL de fato (`CREATE TABLE`, `INSERT`, `UPDATE`, `SELECT`...)
- Faz o **ORM** (*Object Relational Mapping*): converte um objeto
  Java em uma linha de tabela relacional, e uma linha de tabela
  relacional de volta em um objeto Java.
- "Reclama" (lança erros) quando falta alguma configuração, como
  identificador ausente ou tipo de campo desconhecido.

Normalmente, quando se usa Spring Boot + Spring Data JPA, é o
Hibernate quem está por trás executando essa camada de persistência
— mesmo que o código da aplicação nunca mencione o Hibernate
diretamente.