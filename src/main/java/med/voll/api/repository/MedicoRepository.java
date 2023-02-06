package med.voll.api.repository;

import med.voll.api.medico.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.DoubleStream;

public interface MedicoRepository extends JpaRepository <Medico, Long> {
    Page<Medico> findAllByAtivoTrue(Pageable paginacao);
}