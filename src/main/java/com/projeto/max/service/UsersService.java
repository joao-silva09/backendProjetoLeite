package com.projeto.max.service;

import org.springframework.stereotype.Service;
import com.projeto.max.domain.Users;
import com.projeto.max.repository.UsersRepository;
import java.util.List;
import com.projeto.max.domain.UserTypes;
import java.time.LocalDateTime;

@Service
public class UsersService extends GenericService<Users, Long> {

    private final UsersRepository usersRepository;
    public UsersService(UsersRepository usersRepository) {
        super(usersRepository);
        this.usersRepository = usersRepository;
    }

	public List<Users> encontrarPorIduser(Long iduser) { return this.usersRepository.findByIduser(iduser); };
	public List<Users> encontrarPorNameContendo(String name) { return this.usersRepository.findByNameContaining(name); };
	public List<Users> encontrarPorName(String name) { return this.usersRepository.findByName(name); };
	public List<Users> encontrarPorDocumentContendo(String document) { return this.usersRepository.findByDocumentContaining(document); };
	public List<Users> encontrarPorDocument(String document) { return this.usersRepository.findByDocument(document); };
	public List<Users> encontrarPorEmailContendo(String email) { return this.usersRepository.findByEmailContaining(email); };
	public List<Users> encontrarPorEmail(String email) { return this.usersRepository.findByEmail(email); };
	public List<Users> encontrarPorPasswordHashContendo(String passwordHash) { return this.usersRepository.findByPasswordHashContaining(passwordHash); };
	public List<Users> encontrarPorPasswordHash(String passwordHash) { return this.usersRepository.findByPasswordHash(passwordHash); };
	public List<Users> encontrarPorUser(UserTypes user) { return this.usersRepository.findByUser(user); };
	public List<Users> encontrarPorActive(Boolean active) { return this.usersRepository.findByActive(active); };
	public List<Users> encontrarPorDataCadastro(LocalDateTime dataCadastro) { return this.usersRepository.findByDataCadastro(dataCadastro); };
	public List<Users> encontrarPorDataAtualizacao(LocalDateTime dataAtualizacao) { return this.usersRepository.findByDataAtualizacao(dataAtualizacao); };

}
