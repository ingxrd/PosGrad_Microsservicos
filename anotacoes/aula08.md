# Resumo da Aula 08 — Mensageria (RabbitMQ) e Ajustes no Spring Batch


## Parte Teórica

### 1. Recap do Spring Batch

Na aula anterior, foram apresentados os principais conceitos do Spring Batch:

- **Job** = processamento completo.
- **Step** = etapa do Job.
- **Chunk** = agrupamento de itens processados dentro de uma transação.
    - Exemplo: `chunk(3)` → lê 3, processa 3 e escreve 3.
- **JobRepository** = responsável por armazenar os metadados das execuções do Batch.

Algumas das tabelas utilizadas pelo `JobRepository`:

- `BATCH_JOB_INSTANCE`
- `BATCH_JOB_EXECUTION`
- `BATCH_JOB_EXECUTION_PARAMS`
- `BATCH_STEP_EXECUTION`

---

### 2. `ItemProcessor` pode filtrar itens

O `ItemProcessor` não serve apenas para transformar os dados.

Ele também pode **filtrar registros**.

Para isso, basta retornar `null`:

```java
@Override
public Aluno process(Aluno aluno) {
    if (!aluno.isAtivo()) {
        return null;
    }

    return aluno;
}