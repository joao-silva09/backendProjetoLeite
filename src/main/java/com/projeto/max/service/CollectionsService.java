package com.projeto.max.service;

import org.springframework.stereotype.Service;
import com.projeto.max.domain.Collections;
import com.projeto.max.repository.CollectionsRepository;
import java.util.List;
import com.projeto.max.domain.Animals;
import com.projeto.max.domain.Farms;
import com.projeto.max.domain.Users;
import java.time.LocalDateTime;

@Service
public class CollectionsService extends GenericService<Collections, Long> {

    private final CollectionsRepository collectionsRepository;
    public CollectionsService(CollectionsRepository collectionsRepository) {
        super(collectionsRepository);
        this.collectionsRepository = collectionsRepository;
    }

	public List<Collections> encontrarPorIdcollection(Long idcollection) { return this.collectionsRepository.findByIdcollection(idcollection); };
	public List<Collections> encontrarPorAnimal(Animals animal) { return this.collectionsRepository.findByAnimal(animal); };
	public List<Collections> encontrarPorFarm(Farms farm) { return this.collectionsRepository.findByFarm(farm); };
	public List<Collections> encontrarPorProducer(Users producer) { return this.collectionsRepository.findByProducer(producer); };
	public List<Collections> encontrarPorCollector(Users collector) { return this.collectionsRepository.findByCollector(collector); };
	public List<Collections> encontrarPorQuantity(Float quantity) { return this.collectionsRepository.findByQuantity(quantity); };
	public List<Collections> encontrarPorTemperature(Float temperature) { return this.collectionsRepository.findByTemperature(temperature); };
	public List<Collections> encontrarPorAcidity(Float acidity) { return this.collectionsRepository.findByAcidity(acidity); };
	public List<Collections> encontrarPorProducerPresent(Boolean producerPresent) { return this.collectionsRepository.findByProducerPresent(producerPresent); };
	public List<Collections> encontrarPorObservationsContendo(String observations) { return this.collectionsRepository.findByObservationsContaining(observations); };
	public List<Collections> encontrarPorObservations(String observations) { return this.collectionsRepository.findByObservations(observations); };
	public List<Collections> encontrarPorCollectionDate(LocalDateTime collectionDate) { return this.collectionsRepository.findByCollectionDate(collectionDate); };
	public List<Collections> encontrarPorDataCadastro(LocalDateTime dataCadastro) { return this.collectionsRepository.findByDataCadastro(dataCadastro); };
	public List<Collections> encontrarPorDataAtualizacao(LocalDateTime dataAtualizacao) { return this.collectionsRepository.findByDataAtualizacao(dataAtualizacao); };
	
	public List<Collections> produtorEntre(Users producer, LocalDateTime startDate, LocalDateTime endDate) { return this.collectionsRepository.findByProducerAndCollectionDateBetween(producer, startDate, endDate); };
}
