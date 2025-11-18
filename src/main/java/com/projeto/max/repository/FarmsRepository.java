package com.projeto.max.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.max.domain.Farms;
import java.util.List;
import com.projeto.max.domain.Users;
import java.time.LocalDateTime;

public interface FarmsRepository extends JpaRepository<Farms, Long> {

	List<Farms> findByIdfarm(Long idfarm);
	List<Farms> findByNameContaining(String name);
	List<Farms> findByName(String name);
	List<Farms> findByProducer(Users producer);
	List<Farms> findByActive(Boolean active);
	List<Farms> findByDataCadastro(LocalDateTime dataCadastro);
	List<Farms> findByDataAtualizacao(LocalDateTime dataAtualizacao);

}
