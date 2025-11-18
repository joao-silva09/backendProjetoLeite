package com.projeto.max.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.max.domain.Collections;
import java.util.List;
import com.projeto.max.domain.Animals;
import com.projeto.max.domain.Farms;
import com.projeto.max.domain.Users;
import java.time.LocalDateTime;

public interface CollectionsRepository extends JpaRepository<Collections, Long> {

	List<Collections> findByIdcollection(Long idcollection);
	List<Collections> findByAnimal(Animals animal);
	List<Collections> findByFarm(Farms farm);
	List<Collections> findByProducer(Users producer);
	List<Collections> findByCollector(Users collector);
	List<Collections> findByQuantity(Float quantity);
	List<Collections> findByTemperature(Float temperature);
	List<Collections> findByAcidity(Float acidity);
	List<Collections> findByProducerPresent(Boolean producerPresent);
	List<Collections> findByObservationsContaining(String observations);
	List<Collections> findByObservations(String observations);
	List<Collections> findByCollectionDate(LocalDateTime collectionDate);
	List<Collections> findByDataCadastro(LocalDateTime dataCadastro);
	List<Collections> findByDataAtualizacao(LocalDateTime dataAtualizacao);

	List<Collections> findByProducerAndCollectionDateBetween(Users producer, LocalDateTime startDate, LocalDateTime endDate);

}
