package com.projeto.max.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.max.domain.UserTypes;
import java.util.List;
import java.time.LocalDateTime;

public interface UserTypesRepository extends JpaRepository<UserTypes, Long> {

	List<UserTypes> findByIduserTypes(Long iduserTypes);
	List<UserTypes> findByNameContaining(String name);
	List<UserTypes> findByName(String name);
	List<UserTypes> findByDataCadastro(LocalDateTime dataCadastro);
	List<UserTypes> findByDataAtualizacao(LocalDateTime dataAtualizacao);

}
