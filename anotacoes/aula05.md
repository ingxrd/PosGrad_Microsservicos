# Resumo da Aula 05 — Profiles, Configuração Externa e Variáveis de Ambiente

## Parte Teórica

### 1. Contexto: onde estamos

* **Aula 01:** organizamos a aplicação por domínio.
* **Aula 02:** os módulos começaram a se relacionar; introduzimos DTOs.
* **Aula 03:** separamos o `aluno-service` e comunicamos via HTTP/Feign.
* **Aula 04:** tratamos falhas remotas (Gateway, exceções, timeout). **Fechamos a Etapa 2.**
* **Aula 05:** iniciamos a **Etapa 3 — Configuração e Execução dos Serviços**.

A pergunta agora é:

> "Como fazer a mesma aplicação rodar em ambientes diferentes (dev, homologação, produção) sem alterar o código?"

---

### 2. Separação entre código e configuração

* **Código** descreve **o comportamento** da aplicação (regras de negócio).
* **Configuração** descreve **onde e como** ela será executada (porta, URL de banco, credenciais, timeouts).
* **Não misturar** as duas coisas.

#### Exemplos

* **Regra de negócio:** "o aluno não pode ser matriculado duas vezes na mesma turma" — verdade em qualquer ambiente.
* **Configuração:** "o `aluno-service` está na porta `8081`" — pode mudar de ambiente para ambiente.

> **Frase-chave:** "Você não deve alterar o código fonte quando a aplicação muda de ambiente."

---

### 3. Ambientes

#### Desenvolvimento

* Facilidade.
* Feedback rápido.
* Banco local.
* `localhost`.

#### Homologação

* Tenta reproduzir o comportamento de produção.
* URLs reais de teste.
* Banco de homologação.

#### Produção

* Segurança.
* Estabilidade.
* Credenciais externas.
* Logs controlados.

Cada empresa pode ter suas próprias variações, como um ambiente de **sustentação** entre homologação e produção.

---

### 4. O que é externalizar configuração

Externalizar configuração **não** significa simplesmente espalhar as configurações em vários arquivos.

Significa evitar que valores específicos de um ambiente obriguem alterações no **código-fonte**.

O ideal é que:

> **O mesmo artefato (JAR) rode em qualquer ambiente, mudando apenas a configuração externa.**

---

### 5. Spring Profiles

Um **Profile** ativa um conjunto específico de configurações para determinado contexto.

Uma estrutura típica é:

```text
application.properties
application-dev.properties
application-prod.properties
```

#### Função de cada arquivo

* `application.properties` → configurações **comuns** a todos os ambientes.
* `application-dev.properties` → configurações **do ambiente de desenvolvimento**.
* `application-prod.properties` → configurações **do ambiente de produção**.

O Spring carrega:

```text
application.properties
        +
application-{profile}.properties
```

Por exemplo, se o profile `dev` estiver ativo:

```text
application.properties
        +
application-dev.properties
```

#### Ativação

```properties
spring.profiles.active=dev
```

Porém, essa configuração **pode vir de fora da aplicação**.

> **Importante:** não é recomendado fixar o profile ativo no arquivo de configuração, porque o ambiente é justamente uma característica externa à aplicação.

---

### 6. Variáveis de ambiente

As variáveis de ambiente são fornecidas ao **processo que inicia a aplicação**, e não diretamente ao código-fonte.

Podem ser:

* **Temporárias:** existem apenas durante a sessão do terminal.
* **Permanentes:** configuradas no sistema operacional, podendo exigir privilégios de administrador.

O Spring consegue ler essas variáveis automaticamente.

#### Vantagem

Não é necessário editar o arquivo de configuração toda vez que o ambiente mudar.

#### Dica do professor

Usar o **Git Bash** para:

1. Criar variáveis temporárias.
2. Rodar a aplicação.
3. Fazer isso **no mesmo terminal**.

---

### 7. Placeholders com valor padrão

É possível definir uma variável de ambiente com um valor padrão:

```properties
${NOME_DA_VARIAVEL:valor_padrao}
```

O comportamento é:

* Se a variável existir no ambiente → utiliza o valor da variável.
* Se a variável não existir → utiliza o valor definido depois dos `:`.

Exemplo:

```properties
server.port=${PORTA:8080}
```

Nesse caso:

* Se `PORTA=8081` → aplicação roda na porta `8081`.
* Se `PORTA` não existir → aplicação utiliza `8080`.

Isso permite definir conscientemente quais configurações são **opcionais** e quais são **obrigatórias**.

---

### 8. Configurações sensíveis (segredos)

**Não devemos versionar credenciais no Git.**

Exemplos de informações sensíveis:

* Senhas de banco de dados.
* Tokens.
* Chaves de API.
* Credenciais de serviços externos.

Já informações como uma porta de aplicação **não são consideradas segredo** e podem permanecer no arquivo de configuração.

#### Exemplo

Não fazer:

```properties
spring.datasource.password=minhaSenha123
```

Preferir algo como:

```properties
spring.datasource.password=${DB_PASSWORD}
```

A senha pode então ser fornecida por uma variável de ambiente ou por um **cofre de segredos**.

> **Exemplo citado pelo professor:** um aluno colocou credenciais da AWS no Git e teve a conta comprometida.

---

### 9. Falhar cedo

Uma configuração **obrigatória** que não foi fornecida deve fazer a aplicação **falhar durante a inicialização**.

Isso é preferível a iniciar a aplicação silenciosamente com um valor incorreto.

> **É melhor falhar cedo do que subir com uma configuração errada e descobrir o problema em produção.**

O professor demonstrou esse comportamento ativando o profile `prod` sem possuir o `application-prod.properties`. Dependendo da configuração utilizada, a aplicação poderia falhar na inicialização ou utilizar algum mecanismo de fallback.

---

### 10. Próximos passos da Etapa 3

| Aula        | Tema                                                                       |
| ----------- | -------------------------------------------------------------------------- |
| **Aula 06** | Banco de dados relacional externo (MySQL)                                  |
| **Aula 07** | Docker e Docker Compose                                                    |
| **Aula 08** | Configuração centralizada (Config Server) e containers recebendo variáveis |

---

## Conceitos principais da Aula 05

```text
Código
  ↓
Comportamento da aplicação

Configuração
  ↓
Onde e como a aplicação será executada

Profiles
  ↓
Configurações específicas de cada ambiente

Variáveis de ambiente
  ↓
Configuração fornecida externamente

Placeholders
  ↓
Valores externos + valores padrão

Segredos
  ↓
Nunca versionar no Git

Falhar cedo
  ↓
Configuração obrigatória ausente → erro na inicialização
```
