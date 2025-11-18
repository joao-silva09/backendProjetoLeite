package com.projeto.max.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.max.domain.Dairys;
import java.util.List;
import java.time.LocalDateTime;

public interface DairysRepository extends JpaRepository<Dairys, Long> {

	List<Dairys> findByIddairy(Long iddairy);
	List<Dairys> findByNameContaining(String name);
	List<Dairys> findByName(String name);
	List<Dairys> findByDataCadastro(LocalDateTime dataCadastro);
	List<Dairys> findByDataAtualizacao(LocalDateTime dataAtualizacao);

}
