package com.projeto.max.service;

import org.springframework.stereotype.Service;
import com.projeto.max.domain.UserTypes;
import com.projeto.max.repository.UserTypesRepository;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class UserTypesService extends GenericService<UserTypes, Long> {

    private final UserTypesRepository userTypesRepository;
    public UserTypesService(UserTypesRepository userTypesRepository) {
        super(userTypesRepository);
        this.userTypesRepository = userTypesRepository;
    }

	public List<UserTypes> encontrarPorIduserTypes(Long iduserTypes) { return this.userTypesRepository.findByIduserTypes(iduserTypes); };
	public List<UserTypes> encontrarPorNameContendo(String name) { return this.userTypesRepository.findByNameContaining(name); };
	public List<UserTypes> encontrarPorName(String name) { return this.userTypesRepository.findByName(name); };
	public List<UserTypes> encontrarPorDataCadastro(LocalDateTime dataCadastro) { return this.userTypesRepository.findByDataCadastro(dataCadastro); };
	public List<UserTypes> encontrarPorDataAtualizacao(LocalDateTime dataAtualizacao) { return this.userTypesRepository.findByDataAtualizacao(dataAtualizacao); };

}
