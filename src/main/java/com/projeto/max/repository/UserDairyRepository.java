package com.projeto.max.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.max.domain.UserDairy;
import java.util.List;
import com.projeto.max.domain.UserDairyKey;
import java.time.LocalDateTime;

public interface UserDairyRepository extends JpaRepository<UserDairy, UserDairyKey> {

	List<UserDairy> findByUserDairyKey(UserDairyKey userDairyKey);
	List<UserDairy> findByDataCadastro(LocalDateTime dataCadastro);
	List<UserDairy> findByDataAtualizacao(LocalDateTime dataAtualizacao);

}
