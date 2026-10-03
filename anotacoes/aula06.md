# Resumo da Aula 06 — Docker: Conceitos Fundamentais (Parte Teórica)


---

## Parte Teórica

### 1. Por que o Docker entra nesta etapa (Etapa 3)

* Na **Aula 05**, externalizamos configurações (profiles, variáveis de ambiente). Isso resolve **o que muda entre ambientes**.
* Agora falta resolver **como a aplicação é executada** de forma padronizada.
* Cenário-problema: uma equipe de 5 devs, cada um com SO diferente (Windows, Linux, Mac), versões de Java diferentes, bancos diferentes, portas diferentes. **Mesmo código, comportamento diferente** por causa do ambiente.
* **Docker entra para padronizar a execução**, tornando-a **previsível e reproduzível**.
* Frase-chave: *"O Docker não substitui o Spring Boot nem o Maven. Ele entra em outra camada."*

### 2. Aplicação, JAR, Imagem, Container (as 4 palavras que se confundem)

| Conceito      | O que é                                                                           | Analogia Java |
| ------------- | --------------------------------------------------------------------------------- | ------------- |
| **Aplicação** | O código-fonte (classes, controllers, services, config)                           | —             |
| **JAR**       | O artefato executável gerado pelo Maven (`mvn package`)                           | —             |
| **Imagem**    | Um **modelo** que descreve um ambiente de execução (Java runtime + JAR + comando) | **Classe**    |
| **Container** | Uma **instância em execução** de uma imagem                                       | **Objeto**    |

**Fluxo:**

```text
Código Java → Maven → JAR → Imagem → Container
```

* **Imagem** pode existir sem estar rodando.
* **Container** representa execução.
* Uma mesma imagem pode gerar **vários containers** com configurações diferentes.

### 3. Container vs Máquina Virtual

| Aspecto          | Máquina Virtual           | Container                        |
| ---------------- | ------------------------- | -------------------------------- |
| O que virtualiza | **Máquina completa** + SO | **Processos** isolados           |
| Kernel           | Cada VM tem o seu         | **Compartilha** o kernel do host |
| Peso             | Pesado                    | Leve                             |
| Inicialização    | Lenta                     | Rápida                           |
| Isolamento       | Muito forte               | Forte                            |

* Containers **não substituem** VMs em todos os casos. Eles podem **coexistir**.
* Ex.: Docker Desktop no Windows usa uma camada de virtualização para oferecer ambiente Linux.
* Frase-chave: *"O container é uma máquina virtual pequena" é uma frase tentadora, mas esconde a diferença principal.*

### 4. Dockerfile

* É um **arquivo texto** chamado `Dockerfile` (sem extensão), colocado na **raiz do projeto**.
* Funciona como uma **receita de construção** da imagem.
* **Não é** o container.
* **Não é** a imagem.
* É a **descrição** de como construí-la.
* Pode (e deve) ser **versionado** junto com o código.

#### Estrutura típica

| Instrução                                | O que faz                                                          |
| ---------------------------------------- | ------------------------------------------------------------------ |
| `FROM eclipse-temurin:17`                | Define a **imagem base** (runtime Java 17)                         |
| `WORKDIR /app`                           | Define o **diretório de trabalho** dentro da imagem                |
| `COPY target/*.jar app.jar`              | Copia o **JAR** já compilado pelo Maven para dentro da imagem      |
| `ENTRYPOINT ["java", "-jar", "app.jar"]` | Define o **comando principal** executado quando o container inicia |

#### Pontos importantes

* A imagem base precisa ser **compatível** com a aplicação (versão do Java).
* **Não se copia o código-fonte**. Copia-se o **JAR já compilado**.
* Decisões que antes ficavam "na cabeça do dev" passam a ficar **declaradas e versionadas**.

### 5. Portas: Host vs Container

* Dentro do container, a aplicação continua escutando na porta configurada (`server.port`, por exemplo, `8080`).
* Isso **não significa** que essa porta está acessível pela máquina host.
* É preciso mapear as portas usando:

```bash
-p HOST:CONTAINER
```

#### Exemplo

```bash
-p 8080:8080
```

* Primeiro `8080` = porta do **host**.
* Segundo `8080` = porta do **container**.

As portas também podem ser diferentes:

```bash
-p 8085:8080
```

Nesse caso:

* A aplicação escuta `8080` **dentro** do container.
* Ela é acessível pela porta `8085` da **máquina host**.

#### Distinção-chave

* `server.port` (Spring) → onde a aplicação escuta **dentro** do container.
* `-p` (Docker) → como uma porta do **host** é conectada a uma porta do **container**.

> Nem todo container precisa publicar uma porta. Se um banco só precisa ser acessado por outro container na mesma rede, não é necessário publicar a porta para o host.

### 6. `localhost` dentro de container

* `localhost` sempre aponta para **o próprio ambiente** onde o processo está rodando.
* **Fora do Docker:** `localhost` = a máquina.
* **Dentro do Docker:** `localhost` = **o próprio container**.

#### Problema clássico

Se o `academico-service` está em um container e tenta chamar:

```text
http://localhost:8081
```

ele procura o `aluno-service` **dentro do próprio container**.

Ele não procura automaticamente outro container.

#### Solução

Usar o **nome do serviço** na rede Docker, por exemplo:

```text
http://aluno-service:8081
```

#### Conexão com a Aula 05

Como a URL já está **externalizada** (`aluno.service.url`), basta mudar o valor da configuração:

```text
Dev local:
http://localhost:8081
```

```text
Docker Compose:
http://aluno-service:8081
```

**O código Java não muda.**

### 7. Volumes

* Containers são **descartáveis** por natureza. Podem ser parados, removidos e recriados.
* **Dados de negócio não podem ser perdidos.**
* Se o container do banco for removido, os dados não podem simplesmente desaparecer.
* **Volume** é o mecanismo que mantém dados **fora da camada descartável** do container.
* O ciclo de vida do volume pode ser **diferente** do ciclo de vida do container.

#### Exemplo

Um container PostgreSQL armazena seus dados em:

```text
/var/lib/postgresql/data
```

Sem volume:

```text
Container removido → dados removidos
```

Com volume:

```text
Container removido → volume continua existindo → dados preservados
```

Outro container pode utilizar o mesmo volume.

#### Conexão com a arquitetura

Cada serviço terá seu **próprio banco** e seu **próprio volume**:

```text
Aluno-service
      ↓
  Aluno-DB
      ↓
   Volume
```

```text
Academico-service
      ↓
 Academico-DB
      ↓
   Volume
```

### 8. Redes Docker

* Containers são isolados, mas precisam **se comunicar** entre si.
* Docker oferece **redes** para conectar containers.
* Containers na **mesma rede** podem se localizar pelo **nome do serviço**.
* Isso evita depender de IPs fixos, que podem mudar quando containers são recriados.

#### Distinção importante

**Comunicação entre containers na mesma rede** é diferente de **acesso do host a um container**.

O Postman na sua máquina precisa de uma **porta publicada** para acessar o `academico-service`.

Já o `academico-service`, dentro da rede Docker, pode falar com o `aluno-service` usando:

```text
http://aluno-service:8081
```

sem precisar publicar essa porta para o host.

#### Conexão com a Aula 05

A variável:

```text
ALUNO_SERVICE_URL
```

pode receber:

```text
http://localhost:8081
```

em desenvolvimento local.

Ou:

```text
http://aluno-service:8081
```

em Docker Compose.

**Mesma aplicação, configuração diferente, sem alteração no código.**

### 9. Docker Compose

Até aqui, cada conceito foi visto isoladamente:

* imagem;
* container;
* porta;
* volume;
* rede.

Em uma solução real, temos vários containers:

```text
academico-service
aluno-service
academico-db
aluno-db
```

Subir tudo manualmente com `docker run` seria trabalhoso e propenso a erros.

O **Docker Compose** permite descrever toda a solução em um único arquivo:

```text
docker-compose.yml
```

#### O que o Compose descreve?

* Quais serviços existem.
* De quais imagens dependem.
* Como devem ser construídos.
* Quais variáveis de ambiente recebem.
* Quais portas publicam.
* Quais volumes utilizam.
* Como os serviços se relacionam na rede.

> **Frase-chave:** *"Em vez de configuração espalhada em comandos manuais, ela passa a estar declarada em um arquivo."*

#### Docker Compose não é orquestrador de produção

Para produção em grande escala existem ferramentas como **Kubernetes**.

Docker Compose é especialmente útil para:

* desenvolvimento;
* estudos;
* ambientes locais;
* cenários com múltiplos serviços coordenados.

### 10. Conexão com a Aula 05 (configuração externa)

**Aula 05:** externalizamos portas, URLs e credenciais.

**Aula 06:** agora externalizamos também **a forma de execução**.

São responsabilidades complementares:

| Tecnologia                                  | Responsabilidade                                                         |
| ------------------------------------------- | ------------------------------------------------------------------------ |
| **Spring Profiles + variáveis de ambiente** | Definem **o que muda** entre ambientes                                   |
| **Docker + Docker Compose**                 | Definem **como os processos e dependências são executados e conectados** |

### Consequência prática

A mesma imagem pode ser utilizada com configurações diferentes:

```text
Mesma imagem
     ↓
┌───────────────┐
│ Configuração  │
│     Dev       │
└───────────────┘
```

ou:

```text
Mesma imagem
     ↓
┌───────────────┐
│ Configuração  │
│    Docker     │
└───────────────┘
```

Sem precisar reconstruir o código.

### 11. Resumo da Etapa 3 até agora

| Aula   | O que foi feito                                                                |
| ------ | ------------------------------------------------------------------------------ |
| **05** | Externalização de configuração (profiles, variáveis de ambiente, placeholders) |
| **06** | Fundamentos de Docker (teoria)                                                 |
| **07** | Prática de Docker: Dockerfile, build, run, volumes e redes                     |
| **08** | Docker Compose completo + fechamento da disciplina                             |

---

## Parte Prática

> ⚠️ **Nesta aula NÃO houve prática.** O professor apenas apresentou os conceitos. A prática de Docker será na próxima aula (quarta) e o fechamento da disciplina será na segunda, em uma aula estendida das 19h às 22h.

**Não há código a implementar nesta aula.**

---

# POR QUE FIZEMOS ISSO?

## Por que estudar Docker nesta etapa?

Porque o sistema agora possui **múltiplos processos independentes**:

* dois serviços;
* dois bancos;
* configurações específicas;
* comunicação entre serviços.

Sem padronização, cada desenvolvedor precisaria instalar e configurar tudo manualmente.

Docker traz:

* **previsibilidade**;
* **reprodutibilidade**;
* **padronização**.

## Por que separar aplicação, JAR, imagem e container?

Porque esses conceitos aparecem frequentemente misturados.

A sequência correta é:

```text
Código Java
    ↓
Maven
    ↓
JAR
    ↓
Imagem Docker
    ↓
Container
```

A analogia ajuda:

```text
Imagem  = Classe
Container = Objeto
```

A imagem é o modelo. O container é uma instância em execução desse modelo.

## Por que container não é "máquina virtual pequena"?

Porque existe uma diferença fundamental:

* **VM:** virtualiza uma máquina completa e seu sistema operacional.
* **Container:** isola processos e compartilha o kernel do host.

Isso explica por que containers geralmente são:

* mais leves;
* mais rápidos para iniciar;
* mais eficientes em recursos.

## Por que usar Dockerfile?

Para **declarar** as decisões necessárias para construir a imagem:

* qual Java utilizar;
* onde colocar o JAR;
* qual diretório utilizar;
* qual comando executar.

Essas decisões ficam:

* documentadas;
* versionadas;
* reproduzíveis.

Isso reduz o problema de:

> "Na minha máquina funciona."

## Por que portas do host e do container podem ser diferentes?

Porque são **dois contextos diferentes**.

Por exemplo:

```text
Host:      8085
             ↓
Container: 8080
```

Isso permite executar múltiplas instâncias que internamente utilizam a mesma porta.

Exemplo:

```text
Container 1 → 8080 → Host 8081
Container 2 → 8080 → Host 8082
```

## Por que `localhost` não funciona entre containers?

Porque `localhost` sempre aponta para **o próprio ambiente**.

Dentro de um container:

```text
localhost
    ↓
próprio container
```

Não significa:

```text
localhost
    ↓
host
```

nem:

```text
localhost
    ↓
outro container
```

Para comunicação entre containers, usamos o **nome do serviço** na rede Docker:

```text
http://aluno-service:8081
```

## Por que usar volumes?

Porque containers são **descartáveis**, enquanto dados de negócio precisam ser persistentes.

Sem volume:

```text
Container → dados
     ↓
Container removido
     ↓
Dados perdidos
```

Com volume:

```text
Container → Volume
     ↓
Container removido
     ↓
Volume continua existindo
```

## Por que usar redes Docker?

Para permitir que os containers:

* se comuniquem;
* sejam encontrados pelo nome;
* não dependam de IPs fixos;
* mantenham a comunicação interna separada da exposição externa.

## Por que Docker Compose?

Porque uma aplicação com múltiplos serviços envolve muitas configurações.

Em vez de executar diversos comandos manualmente:

```text
docker run ...
docker run ...
docker run ...
docker run ...
```

podemos declarar a solução em:

```text
docker-compose.yml
```

Esse arquivo descreve:

* serviços;
* imagens;
* builds;
* variáveis;
* portas;
* volumes;
* redes.

## Por que isso se conecta à
