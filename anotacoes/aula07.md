# Parte Teórica

## 1. Onde estamos e o que muda

- **Etapas 1 e 2:** organização, separação de serviços, comunicação HTTP/REST e tratamento de falhas.
- **Etapa 3:** configuração externa (profiles e variáveis de ambiente) e conceitos de Docker (pendente de prática).
- **Etapa 4:** ampliação do repertório arquitetural. Dois novos tipos de problema:
   - **Comunicação assíncrona (mensageria):** quem produz não espera quem consome.
   - **Processamento em lote (Spring Batch):** processar grandes volumes de dados de forma estruturada.

> **Frase-chave:** "Não estamos abandonando o REST. Estamos ampliando o repertório. Cada abordagem resolve melhor um tipo de problema."

---

## 2. Síncrono vs Assíncrono

| **Aspecto** | **Síncrono (REST)** | **Assíncrono (Mensageria)** |
|---|---|---|
| Quem chama | Espera a resposta | Não espera |
| Dependência temporal | Forte (precisa estar no ar) | Fraca (desacoplamento temporal) |
| Uso típico | Consulta imediata, validação online | Integração entre sistemas, tarefas posteriores, desacoplamento |
| Exemplo | `academico-service` → `aluno-service` | Fila de CAT (acidente de trabalho) entre Dataprev e Serpro |

### Conceitos-chave

- **Mensagem:** dado que representa uma solicitação, por exemplo: `"aluno cadastrado"` ou `"matrícula realizada"`.
- **Fila:** intermediário que armazena a mensagem até o consumidor estar pronto.
- **Produtor:** quem envia a mensagem.
- **Consumidor:** quem lê e processa a mensagem.
- **Desacoplamento temporal:** produtor e consumidor não precisam agir ao mesmo tempo.

---

## 3. Mensageria NÃO resolve tudo

- Nem toda necessidade assíncrona é mensageria.
- Quando o problema é **processar grandes volumes de dados** de forma estruturada, entra o **Spring Batch**.
- **Mensageria:** integração entre serviços.
- **Batch:** processamento em lote, como:
   - Importação.
   - Exportação.
   - Consolidação.
   - Rotinas periódicas.

---

## 4. Spring Batch — Conceitos

- **Job:** processamento completo, ou seja, a rotina inteira.
- **Step:** uma etapa do Job. Um Job pode ter vários Steps.
- **ItemReader:** lê os dados de entrada.
- **ItemProcessor:** aplica transformação, validação ou regra de negócio.
- **ItemWriter:** grava ou envia o resultado final.
- **Chunk:** agrupamento de itens processados antes de uma escrita/transação.
