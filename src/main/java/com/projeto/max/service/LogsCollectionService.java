package com.projeto.max.service;

import org.springframework.stereotype.Service;
import com.projeto.max.domain.LogsCollection;
import com.projeto.max.repository.LogsCollectionRepository;
import java.util.List;
import com.projeto.max.domain.Users;
import java.time.LocalDateTime;

@Service
public class LogsCollectionService extends GenericService<LogsCollection, Long> {

    private final LogsCollectionRepository logsCollectionRepository;
    public LogsCollectionService(LogsCollectionRepository logsCollectionRepository) {
        super(logsCollectionRepository);
        this.logsCollectionRepository = logsCollectionRepository;
    }

	public List<LogsCollection> encontrarPorIdlogCollection(Long idlogCollection) { return this.logsCollectionRepository.findByIdlogCollection(idlogCollection); };
	public List<LogsCollection> encontrarPorNameContendo(String name) { return this.logsCollectionRepository.findByNameContaining(name); };
	public List<LogsCollection> encontrarPorName(String name) { return this.logsCollectionRepository.findByName(name); };
	public List<LogsCollection> encontrarPorDescriptionContendo(String description) { return this.logsCollectionRepository.findByDescriptionContaining(description); };
	public List<LogsCollection> encontrarPorDescription(String description) { return this.logsCollectionRepository.findByDescription(description); };
	public List<LogsCollection> encontrarPorUser(Users user) { return this.logsCollectionRepository.findByUser(user); };
	public List<LogsCollection> encontrarPorDataCadastro(LocalDateTime dataCadastro) { return this.logsCollectionRepository.findByDataCadastro(dataCadastro); };
	public List<LogsCollection> encontrarPorDataAtualizacao(LocalDateTime dataAtualizacao) { return this.logsCollectionRepository.findByDataAtualizacao(dataAtualizacao); };

}
