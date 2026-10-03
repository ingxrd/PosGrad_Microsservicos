package br.edu.infnet.ingrid_api.model_service;

// encapsula o map aqui dentro

// por enquanto eh o nosso recurso de persistencia, pq ainda n estamos trabalhando com banco de dados

// PROBLEMA -> Eu vou replicar este CRUD para TODAS as minhas classes.
// como eu vou ter o crud pra cada uma delas, eu aumento o problema para
// 1. manutencao
// 2. varios testes caso tenha algum problema
// 3. repeticao de codigo

// para solucionar este problema, usamos o GENERICS
import br.edu.infnet.ingrid_api.model.domain.Comunicado;
import org.springframework.stereotype.Service;

import java.util.*;
// anotacao diz que esta classe sera administrada pelo springboot =)
// a anotacao service tb para comunicar a intenção desta classe

@Service
public class ComunicadoService extends BaseService<Comunicado> {

    // metodo IMPERATIVO
    public List<Comunicado> obterPublicados() {
        List<Comunicado> publicados = new ArrayList<Comunicado>();

        for (Comunicado comunicado : obterLista()) {
            if(comunicado.isPublicado()){
                publicados.add(comunicado);
            }
        }
        return publicados;
    }

    // metodo DECLARATIVO usando Streams
    public List<Comunicado> obterListaPublicados(){

        return obterLista().stream().filter(Comunicado::isPublicado).toList();
        // faz o filtro e retorna a lista.
        // faz a mesma coisa que o obterPublicados
        // eh mais declarativo porque diz O QUE queremos,
        // em vez de definir COMO fazer com for e if
        // filtrar, ordenar, agrupar, transformar uma colecao em outra, reducao -> USO
    }

    // metodo IMPERATIVO de trazer as informacoes
    public List<Comunicado> buscarComunicadosPorTitulo(String termo){

        List<Comunicado> resultado = new ArrayList<Comunicado>();

        for(Comunicado comunicado : obterLista()) {
            String tituloMinusculo = comunicado.getTitulo().toLowerCase();
            String termoMinusculo = termo.toLowerCase();

            if (tituloMinusculo.contains(termoMinusculo)) {
                resultado.add(comunicado);
            }
        }
        return resultado;
    }

    // metodo DECLARATIVO usando Streams
    public List<Comunicado> buscarTituloDeclarativa(String termo){

        // ADICIONADO -> estava faltando a validacao aqui
        // sem isso, se o termo vier null ou "", o metodo deixava passar
        // ou estourava NullPointerException la dentro do .contains()
        validarTermo(termo);

        return obterLista().stream()
                .filter(comunicado -> comunicado.getTitulo().toLowerCase().contains(termo.toLowerCase()))
                .toList();
    }

    public List<Comunicado> obterPorTitulo(String termo) {

        validarTermo(termo);

        String termoNormalizado = termo.toLowerCase();

        List<Comunicado> resultado = new ArrayList<>();

        for (Comunicado comunicado : obterLista()) {

            if (comunicado.getTitulo()
                    .toLowerCase()
                    .contains(termoNormalizado)) {

                resultado.add(comunicado);
            }
        }

        return resultado;
    }

    // ADICIONADO -> metodo de validacao, igual ao do professor
    // fica privado aqui dentro do proprio ComunicadoService
    // (antes eu tava usando um validarTermo que vinha herdado do BaseService)
    private void validarTermo(String termo) {
        if (
                termo == null
                        || termo.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "O termo de busca deve ser informado."
            );
        }
    }

    // ADICIONADO -> metodo que o professor ja tem, ainda como TODO
    public List<Comunicado> ordenarPorTitulo() {
        // TODO Auto-generated method stub
        return new ArrayList<Comunicado>();
    }

    // ADICIONADO -> metodo que o professor ja tem, ainda como TODO
    public List<String> obterTitulos() {
        // TODO Auto-generated method stub
        return new ArrayList<String>();
    }

    /*
        === FAZENDO COMENTARIO PQ ESTOU APLICANDO AGORA O GENERICS ====

    // final indica que a referencia comunicados
    // nao significa que o conteudo do map eh imutavel, apenas nao podemos
    // a referencia apontar pra outro map, apenas para comunicados

    private final Map<Long, br.edu.infnet.ingrid_api.model.domain.Comunicado> comunicados =
            new LinkedHashMap<Long, br.edu.infnet.ingrid_api.model.domain.Comunicado>();

    // metodo de inclusao de comunicado
    // nao retorna nada
    // parametro comunicado
    public void incluir(Comunicado comunicado){
        comunicados.put(comunicado.getId(), comunicado);
    }

    public Collection<Comunicado> obterListaComunicados(){
        return comunicados.values();
    }

    // ALTERAR

    public void alterar(Comunicado comunicado){
        comunicados.put(comunicado.getId(), comunicado);
    }

    // EXCLUIR

    public void excluir(Long id){
        comunicados.remove(id);
    }

    // OBTER POR ID

    public Comunicado obterPoId(Long id){
        return comunicados.get(id);
    }

    // posso transformar uma colecao de map pra list
    /*
    public List<Comunicado> obterLista(){
        return new ArrayList<Comunicado>(comunicado.values());
    }
    */
}