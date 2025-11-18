package com.projeto.max.service;

import org.springframework.stereotype.Service;
import com.projeto.max.domain.UserDairy;
import com.projeto.max.repository.UserDairyRepository;
import java.util.List;
import com.projeto.max.domain.UserDairyKey;
import java.time.LocalDateTime;

@Service
public class UserDairyService extends GenericService<UserDairy, UserDairyKey> {

    private final UserDairyRepository userDairyRepository;
    public UserDairyService(UserDairyRepository userDairyRepository) {
        super(userDairyRepository);
        this.userDairyRepository = userDairyRepository;
    }

	public List<UserDairy> encontrarPorUserDairyKey(UserDairyKey userDairyKey) { return this.userDairyRepository.findByUserDairyKey(userDairyKey); };
	public List<UserDairy> encontrarPorDataCadastro(LocalDateTime dataCadastro) { return this.userDairyRepository.findByDataCadastro(dataCadastro); };
	public List<UserDairy> encontrarPorDataAtualizacao(LocalDateTime dataAtualizacao) { return this.userDairyRepository.findByDataAtualizacao(dataAtualizacao); };

}
