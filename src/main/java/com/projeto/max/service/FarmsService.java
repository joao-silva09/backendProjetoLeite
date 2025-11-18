package com.projeto.max.service;

import org.springframework.stereotype.Service;
import com.projeto.max.domain.Farms;
import com.projeto.max.repository.FarmsRepository;
import java.util.List;
import com.projeto.max.domain.Users;
import java.time.LocalDateTime;

@Service
public class FarmsService extends GenericService<Farms, Long> {

    private final FarmsRepository farmsRepository;
    public FarmsService(FarmsRepository farmsRepository) {
        super(farmsRepository);
        this.farmsRepository = farmsRepository;
    }

	public List<Farms> encontrarPorIdfarm(Long idfarm) { return this.farmsRepository.findByIdfarm(idfarm); };
	public List<Farms> encontrarPorNameContendo(String name) { return this.farmsRepository.findByNameContaining(name); };
	public List<Farms> encontrarPorName(String name) { return this.farmsRepository.findByName(name); };
	public List<Farms> encontrarPorProducer(Users producer) { return this.farmsRepository.findByProducer(producer); };
	public List<Farms> encontrarPorActive(Boolean active) { return this.farmsRepository.findByActive(active); };
	public List<Farms> encontrarPorDataCadastro(LocalDateTime dataCadastro) { return this.farmsRepository.findByDataCadastro(dataCadastro); };
	public List<Farms> encontrarPorDataAtualizacao(LocalDateTime dataAtualizacao) { return this.farmsRepository.findByDataAtualizacao(dataAtualizacao); };

}
