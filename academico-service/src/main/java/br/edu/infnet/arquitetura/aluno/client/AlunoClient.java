package br.edu.infnet.arquitetura.aluno.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "aluno-service", url = "${aluno.service.url}")
public interface AlunoClient {

	@GetMapping("/alunos/{id}")
	AlunoResponse obterPorId(@PathVariable Long id);
}