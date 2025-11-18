package com.projeto.max.service;

import org.springframework.stereotype.Service;
import com.projeto.max.domain.Animals;
import com.projeto.max.repository.AnimalsRepository;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class AnimalsService extends GenericService<Animals, Long> {

    private final AnimalsRepository animalsRepository;
    public AnimalsService(AnimalsRepository animalsRepository) {
        super(animalsRepository);
        this.animalsRepository = animalsRepository;
    }

	public List<Animals> encontrarPorIdanimal(Long idanimal) { return this.animalsRepository.findByIdanimal(idanimal); };
	public List<Animals> encontrarPorNameContendo(String name) { return this.animalsRepository.findByNameContaining(name); };
	public List<Animals> encontrarPorName(String name) { return this.animalsRepository.findByName(name); };
	public List<Animals> encontrarPorDataCadastro(LocalDateTime dataCadastro) { return this.animalsRepository.findByDataCadastro(dataCadastro); };
	public List<Animals> encontrarPorDataAtualizacao(LocalDateTime dataAtualizacao) { return this.animalsRepository.findByDataAtualizacao(dataAtualizacao); };

}
