package pp.dev.econsig.service;

import org.springframework.stereotype.Service;

import pp.dev.econsig.dto.CompetenciaDTO;
import pp.dev.econsig.entity.Competencia;
import pp.dev.econsig.repository.CompetenciaRepository;

@Service
public class CompetenciaService {

    public final CompetenciaRepository competenciaRepository;

    public CompetenciaService(CompetenciaRepository competenciaRepository) {
        this.competenciaRepository = competenciaRepository;
    }

    public CompetenciaDTO salvar(CompetenciaDTO competenciaDTO) {
        Competencia competencia = new Competencia();
        competencia.setMes(competenciaDTO.mes());
        competencia.setAno(competenciaDTO.ano());
        Competencia salvo = competenciaRepository.save(competencia);
        return new CompetenciaDTO(salvo.getId(), salvo.getMes(), salvo.getAno());
    }

    public void deletar(Long id) {
        if (!competenciaRepository.existsById(id)) {
            throw new RuntimeException("Competência não encontrada");
        }
        competenciaRepository.deleteById(id);
    }

    public CompetenciaDTO buscarPorId(Long id) {
        Competencia competencia = competenciaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Competência não encontrada"));
        return new CompetenciaDTO(competencia.getId(), competencia.getMes(), competencia.getAno());
    }

    public Competencia buscarEntidadePorId(Long id) {
        return competenciaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Competência não encontrada"));
    }
}
