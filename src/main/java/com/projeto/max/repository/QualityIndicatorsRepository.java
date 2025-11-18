package com.projeto.max.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.max.domain.QualityIndicators;
import java.util.List;
import java.time.LocalDateTime;

public interface QualityIndicatorsRepository extends JpaRepository<QualityIndicators, Long> {

	List<QualityIndicators> findByIdqualityIndicator(Long idqualityIndicator);
	List<QualityIndicators> findByCollectionId(Long collectionId);
	List<QualityIndicators> findByAntibioticContaining(String antibiotic);
	List<QualityIndicators> findByAntibiotic(String antibiotic);
	List<QualityIndicators> findByFatContaining(String fat);
	List<QualityIndicators> findByFat(String fat);
	List<QualityIndicators> findByApproved(Boolean approved);
	List<QualityIndicators> findByQualityContaining(String quality);
	List<QualityIndicators> findByQuality(String quality);
	List<QualityIndicators> findByDataCadastro(LocalDateTime dataCadastro);
	List<QualityIndicators> findByDataAtualizacao(LocalDateTime dataAtualizacao);

}
