package com.projeto.max.service;

import org.springframework.stereotype.Service;
import com.projeto.max.domain.Dairys;
import com.projeto.max.repository.DairysRepository;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class DairysService extends GenericService<Dairys, Long> {

    private final DairysRepository dairysRepository;
    public DairysService(DairysRepository dairysRepository) {
        super(dairysRepository);
        this.dairysRepository = dairysRepository;
    }

	public List<Dairys> encontrarPorIddairy(Long iddairy) { return this.dairysRepository.findByIddairy(iddairy); };
	public List<Dairys> encontrarPorNameContendo(String name) { return this.dairysRepository.findByNameContaining(name); };
	public List<Dairys> encontrarPorName(String name) { return this.dairysRepository.findByName(name); };
	public List<Dairys> encontrarPorDataCadastro(LocalDateTime dataCadastro) { return this.dairysRepository.findByDataCadastro(dataCadastro); };
	public List<Dairys> encontrarPorDataAtualizacao(LocalDateTime dataAtualizacao) { return this.dairysRepository.findByDataAtualizacao(dataAtualizacao); };

}
