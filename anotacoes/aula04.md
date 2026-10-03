# Resumo da Aula 04 — Tratamento de Falhas na Comunicação Remota, Gateway e Timeout

## Parte Teórica

### 1. Contexto: onde estamos

- **Aula 01:** organizamos a aplicação por domínio (`aluno`, `turma`).
- **Aula 02:** os módulos começaram a se relacionar; introduzimos DTOs.
- **Aula 03:** separamos o `aluno` em uma segunda aplicação Spring Boot (`aluno-service`, porta 8081) e o `academico-service` passou a chamá-lo via **HTTP/OpenFeign**. Testamos a falha de rede desligando o `aluno-service`, mas o tratamento ainda era genérico (erro 500).
- **Aula 04:** a pergunta muda. Não basta **atravessar a rede**; precisamos **lidar com o que acontece quando o outro lado não responde bem**. Vamos distinguir **erro de negócio** de **erro de infraestrutura**, traduzir falhas técnicas em respostas HTTP coerentes e limitar o tempo de espera com **timeout**.

---

### 2. Leitura do log de erro — o que aprendemos

Quando o `aluno-service` está desligado e o `academico-service` tenta matricular, o log mostra:

- `Connection Refused` → **erro de rede**, não é HTTP 404 nem 500 do outro serviço. É a impossibilidade de **estabelecer a conexão TCP**.
- `java.net.ConnectException` → causa raiz: falha ao abrir o socket TCP.
- `RetryableException` → exceção do Feign que indica que a falha pertence a uma **categoria que poderia ser repetida (retry)**, mas o Feign **não fez retry automaticamente**. Ele apenas classificou a falha.
- O Feign cria um **proxy em tempo de execução** para a interface `AlunoClient`. A chamada Java que parece "local" se transforma em uma requisição HTTP real.

> **Frase-chave:** "Uma parte do sistema pode estar funcionando enquanto outra parte está indisponível. Isso é um dos conceitos centrais dos sistemas distribuídos."

---

### 3. Três cenários distintos que a aplicação deve distinguir

| **Cenário** | **O que aconteceu** | **Status HTTP correto** |
| :--- | :--- | :--- |
| Aluno existe | Comunicação OK, recurso encontrado | `200 OK` |
| Aluno não existe | Comunicação OK, recurso não encontrado | `404 Not Found` |
| Serviço indisponível | Comunicação nem se estabeleceu (`Connection Refused`, timeout) | **`503 Service Unavailable`** |

O erro **`500 Internal Server Error`** genérico **não** é a resposta adequada para o terceiro caso.

O `503` comunica melhor:

> "O servidor não conseguiu completar a operação porque um serviço necessário está temporariamente indisponível."

---

### 4. Erro de negócio vs. erro de infraestrutura

#### Erro de negócio

O outro serviço **respondeu**.

Exemplo:

```text
academico-service → aluno-service
                     ↓
                  HTTP 404
                     ↓
             "Aluno não encontrado"