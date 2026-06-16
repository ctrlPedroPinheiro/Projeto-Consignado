package pp.dev.econsig.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import pp.dev.econsig.dto.ConsignadoDTO;
import pp.dev.econsig.entity.Competencia;
import pp.dev.econsig.entity.Consignado;
import pp.dev.econsig.repository.ConsignadoRepository;

@Service
public class ConsignadoService {
    public final CompetenciaService competenciaService;
    public final ConsignadoRepository consignadoRepository;

    public ConsignadoService(CompetenciaService competenciaService, ConsignadoRepository consignadoRepository) {
        this.competenciaService = competenciaService;
        this.consignadoRepository = consignadoRepository;
    }

    public ConsignadoDTO criar(ConsignadoDTO consignadoDTO) {
        Competencia competencia = competenciaService.buscarEntidadePorId(consignadoDTO.competenciaId());
        Consignado consignado = new Consignado();
        consignado.setContrato(consignadoDTO.contrato());
        consignado.setNome(consignadoDTO.nome());
        consignado.setCpf(consignadoDTO.cpf());
        consignado.setMatricula(consignadoDTO.matricula());
        consignado.setPrazoTotal(consignadoDTO.prazoTotal());
        consignado.setNumeroPrestacao(consignadoDTO.numeroPrestacao());
        consignado.setValorPrestacao(consignadoDTO.valorPrestacao());
        consignado.setCompetencia(competencia);
        Consignado novo = consignadoRepository.save(consignado);
        return new ConsignadoDTO(novo.getId(), novo.getContrato(), novo.getNome(), novo.getCpf(), novo.getMatricula(), novo.getPrazoTotal(), novo.getNumeroPrestacao(), novo.getValorPrestacao(), novo.getCompetencia().getId());
    }

    public void deletar(Long id) {
        if (!consignadoRepository.existsById(id)) {
            throw new RuntimeException("Consignado não encontrado");
        }
        consignadoRepository.deleteById(id);
    }

    public ConsignadoDTO buscarPorId(Long id) {
        Consignado consignado = consignadoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Consignado não encontrado"));
        return new ConsignadoDTO(consignado.getId(), consignado.getContrato(), consignado.getNome(), consignado.getCpf(), consignado.getMatricula(), consignado.getPrazoTotal(), consignado.getNumeroPrestacao(), consignado.getValorPrestacao(), consignado.getCompetencia().getId());
    }

    public Consignado buscarEntidadePorId(Long id) {
        return consignadoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Consignado não encontrado"));
    }

    public List<ConsignadoDTO> buscarPorCompetencia(Long competenciaId) {
        List<Consignado> consignados = consignadoRepository.findByCompetenciaId(competenciaId);
        return consignados.stream()
            .map(c -> new ConsignadoDTO(c.getId(), c.getContrato(), c.getNome(), c.getCpf(), c.getMatricula(), c.getPrazoTotal(), c.getNumeroPrestacao(), c.getValorPrestacao(), c.getCompetencia().getId()))
            .collect(Collectors.toList());
    }

    public ConsignadoDTO atualizar(Long id, ConsignadoDTO consignadoDTO) {
        Consignado existente = buscarEntidadePorId(id);
        Competencia competencia = competenciaService.buscarEntidadePorId(consignadoDTO.competenciaId());
        existente.setContrato(consignadoDTO.contrato());
        existente.setNome(consignadoDTO.nome());
        existente.setCpf(consignadoDTO.cpf());
        existente.setMatricula(consignadoDTO.matricula());
        existente.setPrazoTotal(consignadoDTO.prazoTotal());
        existente.setNumeroPrestacao(consignadoDTO.numeroPrestacao());
        existente.setValorPrestacao(consignadoDTO.valorPrestacao());
        existente.setCompetencia(competencia);
        Consignado atualizado = consignadoRepository.save(existente);
        return new ConsignadoDTO(atualizado.getId(), atualizado.getContrato(), atualizado.getNome(), atualizado.getCpf(), atualizado.getMatricula(), atualizado.getPrazoTotal(), atualizado.getNumeroPrestacao(), atualizado.getValorPrestacao(), atualizado.getCompetencia().getId());
    }
}
