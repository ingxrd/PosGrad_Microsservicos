package br.edu.infnet.arquitetura.aluno.client;

import br.edu.infnet.arquitetura.batch.AlunoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "aluno-service", url = "${aluno.service.url}")
public interface AlunoClient {

	@GetMapping("/alunos/{id}")
	AlunoResponse obterPorId(@PathVariable Long id);

	@PostMapping("/alunos")
	void incluir(@RequestBody AlunoRequest aluno);

}