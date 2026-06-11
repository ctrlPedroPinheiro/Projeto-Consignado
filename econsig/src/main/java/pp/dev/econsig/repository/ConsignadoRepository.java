package pp.dev.econsig.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import pp.dev.econsig.entity.Consignado;

public interface ConsignadoRepository extends JpaRepository<Consignado, Long> {

    List<Consignado> findByCompetenciaId(Long competenciaId);

}
