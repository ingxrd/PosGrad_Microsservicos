package br.edu.infnet.ingrid_api.model_service;

import br.edu.infnet.ingrid_api.exception.IdentificadorDuplicadoException;
import br.edu.infnet.ingrid_api.exception.RecursoNaoEncontradoException;
import br.edu.infnet.ingrid_api.model.domain.Identificavel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


// ============================================================
// AULA 02 - GENERICS
// ============================================================
//
// O BaseService é genérico porque o CRUD será praticamente o mesmo
// para diferentes classes: Comunicado, Escola, Turma, Professor etc.
//
// T representa o tipo da classe que utilizará o Service.
//
// A restrição:
//
// T extends Identificavel
//
// significa que T obrigatoriamente precisa implementar Identificavel.
//
// Isso permite que o BaseService tenha garantia de que qualquer T
// possui o método getId().
//
// Sem essa restrição, o compilador não permitiria:
//
// objeto.getId()
//
// porque um tipo genérico sozinho não garante a existência desse método.
//
// A interface Identificavel funciona como um contrato mínimo
// exigido pelo BaseService.


// ============================================================
// AULA 03 - CAMADA DE SERVIÇO
// ============================================================
//
// Antes da camada de serviço ser criada, existia apenas um conjunto
// de classes que representava o domínio da aplicação.
//
// Depois, utilizávamos o Loader (agora ProjectRunner) para:
// - instanciar os objetos;
// - mostrar os modelos;
// - verificar se estavam funcionando.
//
// Basicamente, o objetivo era validar o domínio.
//
// Conforme o sistema aumentou, o Runner passou a assumir muitas
// responsabilidades: criar, excluir e executar regras de negócio.
//
// Isso é um sinal de que precisamos evoluir a arquitetura.
//
// Criamos a camada de serviço para centralizar as operações
// relacionadas aos objetos de domínio.
//
// Fluxo:
//
// ProjectRunner -> Service -> Objetos de domínio
//
//
// SEPARAÇÃO DE RESPONSABILIDADES:
//
// domínio  -> representa o problema que estamos resolvendo;
// service  -> representa as regras da aplicação;
// runner   -> inicia a aplicação.
//
// Cada componente deve fazer apenas aquilo para o qual foi criado.



// ============================================================
// AULA 03 - ENCAPSULAMENTO
// ============================================================
//
// O Map que armazena os objetos fica privado dentro do Service.
//
// Dessa forma, outras classes não conseguem acessar diretamente
// ou modificar o armazenamento.
//
// As operações precisam passar pelos métodos do Service,
// onde podemos aplicar as regras da aplicação.
//
// Isso reduz o acoplamento e protege o estado interno da classe.


public abstract class BaseService<T extends Identificavel> {


    // ========================================================
    // ARMAZENAMENTO
    // ========================================================
    //
    // O Map funciona como nosso recurso de persistência temporário,
    // já que ainda não estamos utilizando banco de dados.
    //
    // Chave   -> ID do objeto
    // Valor   -> objeto
    //
    // LinkedHashMap foi utilizado porque mantém a ordem de inserção.
    //
    // final significa que a referência não poderá apontar para outro Map.
    // O conteúdo do Map continua podendo ser alterado.

    private final Map<Long, T> dados = new LinkedHashMap<>();


    // ========================================================
    // VALIDAÇÃO DO OBJETO
    // ========================================================
    //
    // Centralizamos a validação para evitar repetir as mesmas regras
    // em incluir(), alterar() etc.
    //
    // Regras:
    // - o objeto não pode ser nulo;
    // - o ID não pode ser nulo.

    private void validarObjeto(T objeto) {

        if (objeto == null) {
            throw new IllegalArgumentException(
                    "O objeto nao pode ser nulo!!"
            );
        }

        if (objeto.getId() == null) {
            throw new IllegalArgumentException(
                    "O id nao pode ser nulo!!"
            );
        }
    }


    // ========================================================
    // INCLUIR
    // ========================================================
    //
    // Inclui um novo objeto no armazenamento.
    //
    // Antes de incluir:
    // 1. validamos o objeto;
    // 2. verificamos se o ID já existe.
    //
    // Se o ID já existir, lançamos uma exceção específica.

    public void incluir(T objeto) {

        validarObjeto(objeto);

        if (dados.containsKey(objeto.getId())) {
            throw new IdentificadorDuplicadoException(
                    "Ja existe um objeto com esse identificador!!"
            );
        }

        dados.put(objeto.getId(), objeto);
    }


    // ========================================================
    // ALTERAR
    // ========================================================
    //
    // Altera um objeto que já existe.
    //
    // Antes de alterar:
    // 1. validamos o objeto;
    // 2. verificamos se o ID existe.
    //
    // Se existir, substituímos o objeto antigo pelo novo.

    public void alterar(T objeto) {

        validarObjeto(objeto);

        verificarExistencia(objeto.getId());

        dados.put(objeto.getId(), objeto);
    }


    // ========================================================
    // OBTER LISTA
    // ========================================================
    //
    // Retorna uma lista contendo os objetos armazenados.
    //
    // IMPORTANTE:
    // Não retornamos diretamente dados.values().
    //
    // Criamos uma nova lista para proteger o armazenamento interno.
    //
    // Dessa forma, se outra classe modificar a lista retornada,
    // ela não modifica diretamente o Map original.
    //
    // Isso faz parte do encapsulamento.

    public List<T> obterLista() {

        return new ArrayList<>(dados.values());
    }


    // ========================================================
    // OBTER POR ID
    // ========================================================
    //
    // Busca um objeto pelo seu identificador.
    //
    // Antes de buscar, verificamos se o recurso existe.

    public T obterPorId(Long id) {

        verificarExistencia(id);

        return dados.get(id);
    }


    // ========================================================
    // EXCLUIR
    // ========================================================
    //
    // Remove um objeto pelo ID.
    //
    // Antes de remover, verificamos se o recurso existe.

    public void excluir(Long id) {

        verificarExistencia(id);

        dados.remove(id);
    }


    // ========================================================
    // VERIFICAR EXISTÊNCIA
    // ========================================================
    //
    // Essa regra era repetida em vários métodos.
    //
    // Por isso, foi centralizada aqui.
    //
    // Agora:
    //
    // alterar()    -> verificarExistencia()
    // obterPorId() -> verificarExistencia()
    // excluir()    -> verificarExistencia()
    //
    // Isso reduz repetição de código e facilita a manutenção.

    private void verificarExistencia(Long id) {

        // Se o ID for nulo, o argumento recebido é inválido.
        // Nesse caso, IllegalArgumentException faz sentido.

        if (id == null) {
            throw new IllegalArgumentException(
                    "O identificador nao pode ser nulo!!"
            );
        }

        // Se o ID não existir, o recurso não foi encontrado.
        // Utilizamos uma exceção específica para essa situação.

        if (!dados.containsKey(id)) {
            throw new RecursoNaoEncontradoException(
                    "Nenhum recurso encontrado para esse identificador!! "
                            + id + "!!!!"
            );
        }
    }


    // ========================================================
    // EXCEÇÕES
    // ========================================================
    //
    // Exceções também fazem parte da comunicação da aplicação.
    //
    // Cada situação deve possuir uma exceção adequada.
    //
    // IllegalArgumentException
    // -> argumento recebido é inválido.
    //
    // IdentificadorDuplicadoException
    // -> já existe um recurso com esse ID.
    //
    // RecursoNaoEncontradoException
    // -> não existe recurso com esse ID.
    //
    // Evitamos utilizar IllegalArgumentException para todas
    // as situações, pois perdemos informações sobre a causa do erro.


    // ========================================================
    // COLLECTIONS
    // ========================================================
    //
    // Collections são estruturas utilizadas para armazenar
    // e manipular grupos de elementos.
    //
    // Algumas estruturas:
    //
    // ArrayList<E>
    // -> lista baseada em array; boa para acesso por índice.
    //
    // LinkedList<E>
    // -> lista encadeada; útil em cenários com muitas inserções/removções.
    //
    // HashSet<E>
    // -> não permite duplicados e não garante ordem.
    //
    // LinkedHashSet<E>
    // -> não permite duplicados e mantém ordem de inserção.
    //
    // Queue<E>
    // -> representa uma fila; normalmente FIFO
    //    (First In, First Out).
    //
    // Deque<E>
    // -> fila de duas pontas; permite adicionar/remover
    //    no início e no fim.
    //
    // HashMap<K,V>
    // -> armazena chave -> valor; a chave é única.
    //
    // LinkedHashMap<K,V>
    // -> semelhante ao HashMap, mas mantém a ordem de inserção.
    //
    // Collection é uma abstração.
    // Podemos trabalhar com diferentes implementações através
    // dos comportamentos definidos pelas interfaces.


    // ========================================================
    // STREAMS
    // ========================================================
    //
    // Streams são utilizados para processar dados de forma
    // mais declarativa.
    //
    // Em vez de controlar manualmente a repetição com loops,
    // descrevemos as operações que queremos realizar sobre os dados.
    //
    // Exemplos:
    //
    // - filtrar comunicados;
    // - ordenar resultados;
    // - transformar objetos;
    // - selecionar apenas determinadas informações;
    // - realizar operações de redução.
    //
    // IMPORTANTE:
    //
    // Stream NÃO é uma Collection.
    //
    // Collection -> representa/armazena um grupo de elementos.
    //
    // Stream -> representa um fluxo para processar elementos.
    //
    // O Stream não armazena os dados.
    // Ele permite realizar operações sobre os dados de uma Collection.
    //
    // Exemplo:
    //
    // obterLista()
    //      .stream()
    //      .filter(...)
    //      .toList();
    //
    // dados.values()
    // -> retorna os valores armazenados no Map.
    //
    // obterLista()
    // -> transforma esses valores em uma nova lista,
    //    que pode ser utilizada como origem para operações com Stream.
}