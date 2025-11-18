package com.projeto.max.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.max.domain.Users;
import java.util.List;
import com.projeto.max.domain.UserTypes;
import java.time.LocalDateTime;

public interface UsersRepository extends JpaRepository<Users, Long> {

	List<Users> findByIduser(Long iduser);
	List<Users> findByNameContaining(String name);
	List<Users> findByName(String name);
	List<Users> findByDocumentContaining(String document);
	List<Users> findByDocument(String document);
	List<Users> findByEmailContaining(String email);
	List<Users> findByEmail(String email);
	List<Users> findByPasswordHashContaining(String passwordHash);
	List<Users> findByPasswordHash(String passwordHash);
	List<Users> findByUser(UserTypes user);
	List<Users> findByActive(Boolean active);
	List<Users> findByDataCadastro(LocalDateTime dataCadastro);
	List<Users> findByDataAtualizacao(LocalDateTime dataAtualizacao);

}
