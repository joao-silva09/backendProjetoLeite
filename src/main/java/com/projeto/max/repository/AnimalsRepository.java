package com.projeto.max.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.max.domain.Animals;
import java.util.List;
import java.time.LocalDateTime;

public interface AnimalsRepository extends JpaRepository<Animals, Long> {

	List<Animals> findByIdanimal(Long idanimal);
	List<Animals> findByNameContaining(String name);
	List<Animals> findByName(String name);
	List<Animals> findByDataCadastro(LocalDateTime dataCadastro);
	List<Animals> findByDataAtualizacao(LocalDateTime dataAtualizacao);

}
