package br.edu.infnet.ingrid_api.controller;


public class sobreProtocoloHttp {

    // camada de controle -> traduz o http pra chamada java
    // service -> executa as regras da nossa aplicaçao

    // ==== AULA 04 ====

    // HTTP trata-se de um protocolo de comunicacao que padroniza a forma como um cliente externo envia uma requisicao e recebe uma resposta do servidor
    // o http add uma camada na frente do java
    // toda comunicacao tem o request (cliente envia) e response (servidor responde)
    // pode ser em json ou xml (json eh o mais usado)
    // rest e recursos > estilo arquitetural usado na construcao de APIs
    // a ideia eh que em vez de pensar em metodos java, deve-se pensar em RECURSOS
    // acessados por URLs
    // a url representa um recurso (SUBSTANTIVO) e NAO uma ACAO
    // ou seja, evita-se url como listarComunicados
    // a operacao eh expressa pelo METODO HTTP, nao pelo nome da rota

    // crud:
    // GET    – recuperar/listar informacoes
    // POST   – criar um novo recurso
    // PUT    – atualizar um recurso existente
    // DELETE – remover um recurso


    // spring MVC e anotacoes
    // @GetMapping, @PostMapping, @PutMapping, @DeleteMapping derivam de uma anotacao mais generica, que eh a
    // @RequestMapping


    // DISPATCHER SERVLET

    // antes dele existir, se você fosse fazer tudo em Java puro (Servlet API pura), você teria que escrever manualmente o código para:
    //Ficar "escutando" as conexões HTTP
    //Interpretar a URL que chegou e decidir qual código deveria tratar aquilo
    //Converter o JSON do corpo da requisição pra um objeto Java
    //Chamar o método certo
    //Pegar o retorno e converter de volta pra JSON
    //Montar a resposta HTTP com status, headers etc.


    // mas com a existencia do dispatcher dervlet...

    //Toda requisição HTTP que chega na aplicação passa primeiro pelo DispatcherServlet (é o "front controller" — controlador frontal).
    //Ele olha a URL e o método HTTP (GET, POST, etc.) e descobre qual Controller e qual método dentro dele deve tratar aquela requisição (isso é feito usando as anotações que vocês viram, tipo @GetMapping).
    //Ele encaminha a requisição pro Controller certo.
    //O Controller chama o Service, que executa a regra de negócio.
    //O resultado volta pro Controller, que devolve pro DispatcherServlet.
    //O DispatcherServlet usa o HttpMessageConverter pra converter esse objeto Java em JSON.
    //Ele monta a resposta HTTP final e devolve pro cliente.

    // Ele é responsavel por orquestrar / coordenar quem faz o quê e em que ordem, desde o momento que a requisição chega até a resposta sair.

    // anatomia da url:
    // http://localhost:8080/comunicados
    //   http        -> protocolo
    //   localhost   -> servidor (host)
    //   8080        -> porta
    //   /comunicados -> recurso

    // endpoint -> ponto de acesso que a nossa api disponibiliza, caracterizado
    // pelo metodo http + caminho (url). o cliente usa essa url pra fazer alguma
    // acao dentro da nossa aplicacao
    // ex: GET /comunicados eh um endpoint, POST /comunicados eh outro endpoint

    // uma api retorna dados e comunica o resultado da operacao.!!!! nao somente retorna dados.

    //


}