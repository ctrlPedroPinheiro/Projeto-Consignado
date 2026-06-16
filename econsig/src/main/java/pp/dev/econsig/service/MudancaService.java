package pp.dev.econsig.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import pp.dev.econsig.repository.MudancaRepository;
import pp.dev.econsig.dto.MudancaDTO;
import pp.dev.econsig.entity.*;

@Service
public class MudancaService {
    public final ConsignadoService consignadoService;
    public final CompetenciaService competenciaService;
    public final MudancaRepository mudancaRepository;

    public MudancaService(ConsignadoService consignadoService, CompetenciaService competenciaService, MudancaRepository mudancaRepository) {
        this.consignadoService = consignadoService;
        this.competenciaService = competenciaService;
        this.mudancaRepository = mudancaRepository;
    }

    public MudancaDTO criar(MudancaDTO mudancaDTO) {
        Consignado consignado = consignadoService.buscarEntidadePorId(mudancaDTO.consignadoId());
        Mudanca mudanca = new Mudanca(consignado, MotivoMudanca.valueOf(mudancaDTO.motivo()));
        Mudanca novo = mudancaRepository.save(mudanca);
        return new MudancaDTO(novo.getId(), novo.getConsignado().getId(), novo.getMotivo().name());
    }

    public void deletar(Long id) {
        if (!mudancaRepository.existsById(id)) {
            throw new RuntimeException("Mudança não encontrada");
        }
        mudancaRepository.deleteById(id);
    }

    public MudancaDTO buscarPorId(Long id) {
        Mudanca mudanca = mudancaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mudança não encontrada"));
        return new MudancaDTO(mudanca.getId(), mudanca.getConsignado().getId(), mudanca.getMotivo().name());
    }

    public Mudanca buscarEntidadePorId(Long id) {
        return mudancaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mudança não encontrada"));
    }

    public List<MudancaDTO> buscarPorCompetencia(Long competenciaId) {
        List<Mudanca> mudancas = mudancaRepository.findByCompetenciaId(competenciaId);
        return mudancas.stream()
            .map(m -> new MudancaDTO(m.getId(), m.getConsignado().getId(), m.getMotivo().name()))
            .collect(Collectors.toList());
    }

    public MudancaDTO atualizar(Long id, MudancaDTO mudancaDTO) {
        Mudanca mudanca = buscarEntidadePorId(id);
        Consignado consignado = consignadoService.buscarEntidadePorId(mudancaDTO.consignadoId());
        mudanca.setConsignado(consignado);
        mudanca.setMotivo(MotivoMudanca.valueOf(mudancaDTO.motivo()));
        Mudanca atualizado = mudancaRepository.save(mudanca);
        return new MudancaDTO(atualizado.getId(), atualizado.getConsignado().getId(), atualizado.getMotivo().name());
    }
}
