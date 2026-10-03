package br.edu.infnet.ingrid_api;

import br.edu.infnet.ingrid_api.exception.IdentificadorDuplicadoException;
import br.edu.infnet.ingrid_api.exception.RecursoNaoEncontradoException;
import br.edu.infnet.ingrid_api.model.domain.*;
import br.edu.infnet.ingrid_api.model_service.*;
import br.edu.infnet.ingrid_api.repository.ComunicadoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProjectRunner implements CommandLineRunner {

    // ============================================================
    // AULA 04 - SERVICES
    // ============================================================

    // Os Services ficam como atributos da classe para que possam
    // ser utilizados pelos diferentes testes realizados no Runner.

    // ================ SOBRE BEANS ================
    // essa repeticao (new EscolaService(), new TurmaService() etc) eh EVITADA pelo Spring
    // atualmente o ProjectRunner esta administrando manualmente a criacao de cada um dos nossos services

    // inversao de controle (IoC) -> inverte quem controla a criacao dos objetos
    // ou seja, a nossa classe NAO cria mais o que precisa, ela so recebe (usa)
    // quem cria e cuida do ciclo de vida desses componentes passa a ser o spring

    // spring vai criar o service e vai passar (injetar) esse service para o project runner

    // BEAN -> objeto criado e gerenciado pelo container do spring

    // quando marcamos uma classe com @Component, estamos pedindo ao spring
    // para ser responsavel por criar e administrar as instancias dessa classe
    // @Component diz basicamente que essa eh uma classe especial, porque
    // quem vai administrar seus objetos eh o container do spring

    // exemplo de como era ANTES (sem IoC/injecao de dependencia) - o project runner
    // criando manualmente cada service:
    /*
    private final EscolaService escolaService = new EscolaService();
    private final TurmaService turmaService = new TurmaService();
    private final ComunicadoService comunicadoService = new ComunicadoService();
    private final ProfessorService professorService = new ProfessorService();
    private final ResponsavelService responsavelService = new ResponsavelService();*/


    // fornece uma instancia.
    // spring cria o bean comunicadoService, depois o project runner pede o comunicado service e depois o spring INJETA  esta dpeendencia
    // e depois nós conseguimos usar o comunicadoService

    // AULA 04
    // injecao de dependencia

    // o project runner dependia antes do comunicadoService pra fazer as funcionalidades
    // esse eh um mecanismo usado pelo spring pra conseguir entregar a dependencia pronta pra uma classe
    // neste caso, o project runner

    // a project runner NAO CRIA, apenas DECLARA a partir de agora

    // spring cria o bean comunicadoService, depois o project runner pede o comunicadoService
    // e o spring INJETA essa dependencia, e a partir dai a gente consegue usar o comunicadoService
    // ou seja, o spring fornece uma instancia pronta

    // melhor fazer a injecao pelo construtor pra tornar isso obrigatorio
    // se a classe depende de um service, ela nao deveria existir sem ele
    // entao so vou conseguir criar o project runner se todo mundo (todas as dependencias)
    // estiver preenchido no construtor

    private final EscolaService escolaService;
    private final TurmaService turmaService;
    private final ComunicadoService comunicadoService;
    private final ProfessorService professorService;
    private final ResponsavelService responsavelService;

    // INJENCAO DE DEPENDENCIA AULA 8
    private final ComunicadoRepository comunicadoRepository;

    // ADD O COMUNICADO REPOSITORY DENTRO DO MEU CONSTRUTOR
    public ProjectRunner(EscolaService escolaService, TurmaService turmaService, ComunicadoService comunicadoService,
                         ProfessorService professorService,
                         ResponsavelService responsavelService,
                         ComunicadoRepository comunicadoRepository) {
        this.escolaService = escolaService;
        this.turmaService = turmaService;
        this.comunicadoService = comunicadoService;
        this.professorService = professorService;
        this.responsavelService = responsavelService;
        this.comunicadoRepository = comunicadoRepository;
    }

    // METODO GRANDAO PARA TESTAR O REPOSITORY
    private void demonstrarRepository() {
        System.out.println("\n========================================");
        System.out.println("TESTE DO COMUNICADO REPOSITORY (SPRING DATA JPA)");
        System.out.println("========================================\n");

        // 1. Salvar novos comunicados (usando o construtor de 2 argumentos, sem ID)
        Comunicado com1 = new Comunicado("Primeiro comunicado via Repository", "Testando Spring Data JPA");
        Comunicado salvo1 = comunicadoRepository.save(com1);
        System.out.println("Salvo com ID: " + salvo1.getId());

        Comunicado com2 = new Comunicado("Segundo comunicado via Repository", "Persistência no H2 funcionando");
        Comunicado salvo2 = comunicadoRepository.save(com2);
        System.out.println("Salvo com ID: " + salvo2.getId());

        // 2. Testar count()
        long quantidade = comunicadoRepository.count();
        System.out.println("Quantidade total no banco: " + quantidade);

        // 3. Testar findAll()
        System.out.println("\n--- Lista de todos os comunicados ---");
        List<Comunicado> todos = comunicadoRepository.findAll();
        todos.forEach(System.out::println);

        // 4. Testar findById() com Optional
        System.out.println("\n--- Buscando ID 1 ---");
        comunicadoRepository.findById(1L).ifPresent(c -> System.out.println("Encontrado: " + c));

        // 5. Testar existsById()
        boolean existe = comunicadoRepository.existsById(salvo1.getId());
        System.out.println("\nO comunicado de ID " + salvo1.getId() + " existe? " + (existe ? "SIM" : "NÃO"));

        // 6. Testar deleteById()
        System.out.println("\n--- Excluindo o ID " + salvo1.getId() + " ---");
        comunicadoRepository.deleteById(salvo1.getId());

        // 7. Confirmar exclusão
        System.out.println("Quantidade após exclusão: " + comunicadoRepository.count());
        System.out.println("Existe após exclusão? " + comunicadoRepository.existsById(salvo1.getId()));
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println( "comunicadoService:");
        System.out.println(comunicadoService);
        System.out.println("comunicadoService: " + comunicadoService);
        System.out.println("comunicadoRepository: " + comunicadoRepository); // Opcional, só para ver se injetou

        demonstrarRepository();

        // ========================================================
        // AULA 01 - CRIAÇÃO DOS OBJETOS
        // ========================================================

        Escola escola = new Escola(
                1L,
                "Escolinha",
                true,
                "Sao Paulo",
                4.5
        );

        Turma turma901 = new Turma(
                "901",
                2026,
                true
        );

        System.out.println(escola);
        System.out.println(turma901);


        // ========================================================
        // AULA 02 - HERANÇA E POLIMORFISMO
        // ========================================================

        // Uma das possibilidades de utilizar o polimorfismo
        // é através da herança.
        //
        // Professor e Responsavel herdam de Pessoa.
        // Por isso, podemos declarar a variável como Pessoa
        // e instanciar um objeto Professor ou Responsavel.
        //
        // O construtor da classe filha também pode preencher
        // atributos que pertencem à classe mãe.

        Pessoa oProfessor = new Professor(
                1L,
                "Ingrid",
                "ingridmunho86@gmail.com",
                true,
                "123"
        );

        Pessoa oResponsavel = new Responsavel(
                1L,
                "Tito",
                true,
                "11956843396",
                "tito@gmail.com"
        );

        // Podemos colocar objetos de tipos diferentes na mesma
        // lista porque ambos possuem uma abstração em comum:
        // Pessoa.
        //
        // Isso é polimorfismo:
        // tratar objetos diferentes através de um tipo comum.

        List<Pessoa> pessoas = new ArrayList<>();

        pessoas.add(oResponsavel);
        pessoas.add(oProfessor);

        System.out.println("LISTA DE PESSOAS:");
        pessoas.forEach(System.out::println);


        // ========================================================
        // AULA 02 - List.of()
        // ========================================================

        // List.of() permite criar uma lista de forma mais simples,
        // porém a lista criada é IMUTÁVEL.
        //
        // Portanto, não podemos adicionar ou remover elementos
        // posteriormente.

        List<Pessoa> pessoasImutaveis = List.of(
                oProfessor,
                oResponsavel
        );

        System.out.println("LISTA IMUTÁVEL:");
        pessoasImutaveis.forEach(System.out::println);


        // ========================================================
        // AULA 02 - RELACIONAMENTO ENTRE OBJETOS
        // ========================================================

        // Uma Escola possui Turmas.
        // Aqui estamos estabelecendo esse relacionamento.

        escola.adicinonarTurma(turma901);

        System.out.println("ESCOLA COM TURMA:");
        System.out.println(escola);


        // ========================================================
        // AULA 02/03 - RELACIONAMENTO E COLEÇÕES
        // ========================================================

    /*

    ESTOU COMENTANDO PORQUE AGORA NA AULA 07 ESTA DANDO PROBLEMA PORQUE O CONSTRUTOR DE COMUNICADO NAO ACEITA MAIS ESTES PARAMETROS PQ ESTAMOS USANOD JPA
        Comunicado comunicado001 = new Comunicado(
                1L,
                "Reuniao de responsaveis",
                true,
                "A reuniao sera realizada segunda 10/08",
                LocalDateTime.now()
        );

        Comunicado comunicado002 = new Comunicado(
                2L,
                "AOOOOOBA!",
                false,
                "UHULLLLLLL",
                LocalDateTime.now()
        );*/

        // Removemos o 1L e o 2L do início
        Comunicado comunicado001 = new Comunicado(
                1l,
                "Reuniao de responsaveis", // String titulo
                "A reuniao sera realizada segunda 10/08", // String conteudo
                true,                      // boolean publicado

                LocalDateTime.now()        // LocalDateTime dataPublicacao
        );

        Comunicado comunicado002 = new Comunicado(
                2L,
                "AOOOOOBA!",
                "AOOOOO",
                true,
                LocalDateTime.now()
        );

        // Uma Turma também possui Comunicados.
        turma901.adicionarComunicado(comunicado001);

        System.out.println("TURMA:");
        System.out.println(turma901);

        System.out.println("COMUNICADO:");
        System.out.println(comunicado001);


        // Podemos percorrer a coleção de comunicados utilizando
        // um for tradicional.

        for (Comunicado comunicado : turma901.getComunidados()) {
            System.out.println(comunicado);
        }

        // A mesma operação poderia ser feita utilizando forEach:
        // turma901.getComunidados().forEach(System.out::println);


        // ========================================================
        // AULA 03 - MAP
        // ========================================================

        // O Map trabalha com CHAVE e VALOR.
        //
        // K = Key (chave)
        // V = Value (valor)
        //
        // Como o ID é um identificador natural para buscar
        // um objeto, podemos utilizá-lo como chave.
        //
        // Neste caso:
        // Long       -> chave
        // Comunicado -> valor

        Map<Long, Comunicado> comunicados = new LinkedHashMap<>();

        // Adicionando os comunicados ao Map.
        comunicados.put(comunicado001.getId(), comunicado001);
        comunicados.put(comunicado002.getId(), comunicado002);


        // Recuperação individual através da chave.
        System.out.println("COMUNICADO COM ID 1:");
        System.out.println(comunicados.get(1L));


        // Recuperação de todas as chaves.
        System.out.println("CHAVES:");
        System.out.println(comunicados.keySet());


        // Recuperação de todos os valores.
        System.out.println("VALORES:");
        comunicados.values().forEach(System.out::println);


        // O LinkedHashMap mantém a ordem em que os elementos
        // foram inseridos.
        //
        // Porém, existe uma limitação nessa abordagem:
        // o Map está dentro do Runner.
        //
        // Isso significa que a classe responsável por iniciar
        // e demonstrar a aplicação também está armazenando os dados.
        //
        // Dessa forma, estamos misturando responsabilidades.


        // ========================================================
        // AULA 03 - SERVICE
        // ========================================================

        // Para evitar que o Runner seja responsável por armazenar
        // e manipular os dados diretamente, criamos uma camada
        // de Service.
        //
        // Agora o Runner solicita ao Service que faça as operações.
        //
        // O fluxo passa a ser:
        //
        // ProjectRunner -> ComunicadoService -> armazenamento

        try {
            comunicadoService.incluir(comunicado001);

        } catch (IdentificadorDuplicadoException e) {
            System.out.println("ERROR: " + e.getMessage());

        } catch (RecursoNaoEncontradoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }


        // Incluindo um segundo comunicado através do Service.
        comunicadoService.incluir(comunicado002);


        // Recuperando todos os comunicados através do Service.
        System.out.println("COMUNICADOS ATRAVÉS DO SERVICE:");

        comunicadoService
                .obterLista()
                .forEach(System.out::println);


        // Recuperando somente os comunicados publicados.
        System.out.println("COMUNICADOS PUBLICADOS:");

        comunicadoService
                .obterPublicados()
                .forEach(System.out::println);


        // ========================================================
        // AULA 03 - GENERICS E INTERFACE
        // ========================================================

        // Professor, Responsavel e Escola implementam Identificavel.
        //
        // Por isso, podemos utilizar Identificavel como tipo
        // da variável, independentemente da classe concreta.

        Identificavel prof2 = new Professor();
        Identificavel resp2 = new Responsavel();
        Identificavel escola2 = new Escola();

        // Isso se relaciona diretamente com o BaseService<T>,
        // que trabalha com objetos que implementam Identificavel.
    }
}