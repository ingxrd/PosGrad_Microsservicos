package br.edu.infnet.ingrid_api.model_service;

import br.edu.infnet.ingrid_api.model.domain.Turma;
import org.springframework.stereotype.Service;

@Service
public class TurmaService extends BaseService<Turma>{
    // Nao possui nenhuma especificidade, por isso nao precisamos sobrescrever
// nenhum metodo do BaseService.
// A camada de servico deste objeto utiliza diretamente os metodos
// genericos herdados do BaseService.
}
