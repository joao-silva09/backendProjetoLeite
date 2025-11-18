package com.projeto.max.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.max.domain.QualityIndicatorExtra;
import java.util.List;
import java.time.LocalDateTime;

public interface QualityIndicatorExtraRepository extends JpaRepository<QualityIndicatorExtra, Long> {

	List<QualityIndicatorExtra> findByIdqualityIndicatorExtra(Long idqualityIndicatorExtra);
	List<QualityIndicatorExtra> findByIndicatorId(Long indicatorId);
	List<QualityIndicatorExtra> findByKeyContaining(String key);
	List<QualityIndicatorExtra> findByKey(String key);
	List<QualityIndicatorExtra> findByValueContaining(String value);
	List<QualityIndicatorExtra> findByValue(String value);
	List<QualityIndicatorExtra> findByDataCadastro(LocalDateTime dataCadastro);
	List<QualityIndicatorExtra> findByDataAtualizacao(LocalDateTime dataAtualizacao);

}
