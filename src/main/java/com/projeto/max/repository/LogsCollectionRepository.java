package com.projeto.max.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.max.domain.LogsCollection;
import java.util.List;
import com.projeto.max.domain.Users;
import java.time.LocalDateTime;

public interface LogsCollectionRepository extends JpaRepository<LogsCollection, Long> {

	List<LogsCollection> findByIdlogCollection(Long idlogCollection);
	List<LogsCollection> findByNameContaining(String name);
	List<LogsCollection> findByName(String name);
	List<LogsCollection> findByDescriptionContaining(String description);
	List<LogsCollection> findByDescription(String description);
	List<LogsCollection> findByUser(Users user);
	List<LogsCollection> findByDataCadastro(LocalDateTime dataCadastro);
	List<LogsCollection> findByDataAtualizacao(LocalDateTime dataAtualizacao);

}
