/*
 * 1. O que é esta classe?
É uma classe de serviço anotada com @Service, responsável por concentrar as regras de negócio e orquestrar as operações sobre o domínio Aluno.

2. Para que ela serve?
Serve como intermediária entre o Controller e o Repository. Ela:

Implementa os casos de uso (incluir, listar, obter por ID, alterar, excluir, obter ativos, buscar por nome, buscar por e-mail).

Aplica regras de negócio (ex.: lançar AlunoNaoEncontradoException quando o ID não existe).

Usa o AlunoRepository para persistência, mas não lida com HTTP.

3. Por que criei ela?
Porque o Controller não deve conter regras de negócio. Essa separação é uma decisão arquitetural clássica: Controller cuida de HTTP, Service cuida das regras, Repository cuida dos dados. Além disso, ao manter o Service dentro do pacote aluno, reforçamos a modularização por domínio e preparamos o terreno para, no futuro, extrair esse módulo como um serviço independente.


 * 
 * */
package br.edu.infnet.arquitetura.aluno;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository){
        this.alunoRepository = alunoRepository;
    }

    // incluir

    public Aluno incluir(Aluno aluno){
        return alunoRepository.save(aluno);
    }

    // obterLista
    public List<Aluno> obterLista(){
        return alunoRepository.findAll();
    }


    //obterPorId
    public Aluno obterPorId(Long id){
        return alunoRepository.findById(id).orElseThrow(() -> new AlunoNaoEncontradoException(id));
    }


    //alterar
    public Aluno alterar(Long id, Aluno aluno){
        Aluno existente = obterPorId(id); // retorna o aluno
        existente.setNome(aluno.getNome());
        existente.setEmail(aluno.getEmail());
        existente.setDataNascimento(aluno.getDataNascimento());
        existente.setAtivo(aluno.isAtivo());
        return alunoRepository.save(existente);
    }



    //excluir

    public void excluir(Long id){
        Aluno existente = obterPorId(id);
        alunoRepository.delete(existente);
    }

    // obterAtivos

    public List<Aluno> obterAtivos(){
        return alunoRepository.findByAtivoTrue();
    }

    // buscar por nome
    public List<Aluno> obterPorNome(String nome){
        return alunoRepository.findByNomeContainingIgnoreCase(nome);
    }

    // buscarporEmail
    public Optional<Aluno> obterPorEmail(String email){
        return alunoRepository.findByEmail(email);
    }


}
