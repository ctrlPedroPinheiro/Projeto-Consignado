package pp.dev.econsig.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import pp.dev.econsig.entity.Mudanca;

public interface MudancaRepository extends JpaRepository<Mudanca, Long> {

    List<Mudanca> findByCompetenciaId(Long competenciaId);

}
