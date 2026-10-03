package br.edu.infnet.ingrid_api.model.domain;

// contrato
// nao afirma o que o objeto é, mas o que o objeto pode oferecer
// lado de comportamento e nao caracteristica
// ocmportamento sera feito pelos metodos de cada classe, claro
public interface Identificavel {
    //nao existem implementacoes, apenas assinaturas de metodos
    // define nomes de funcionalidades que dps vamos agrupar nas classes


    // nao precisa da visibilidade pq eh dentro da classe que eu vou definir isto.
    // a classe que implementar esta interface sera OBRIGADA a por um Id
    // Pessoa eh uma classe abstrata pq concentra estado + implementacoes e seus filhos herdam  ->  IS-A
    // Identificavel eh uma interface pq define uma capacidade compartilhada entre classes, nao necessariamente da mesma familia -> NAO NECESSARIAMENTE IS-A
    // Qual contrato sera definido?
    // diferentes classes colocam um mesma funcao? quer desacoplar? utilizamos interfaces
    // quero trabalhar com diferentes objetos atraves de um contrato? utilizamos interface
    // NAO HA HERANÇA NECESSARIAMENTE.
    //

    Long getId(); // getId depois, pra recuperar este campo


}
