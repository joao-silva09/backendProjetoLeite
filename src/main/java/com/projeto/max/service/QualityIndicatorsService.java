package com.projeto.max.service;

import org.springframework.stereotype.Service;
import com.projeto.max.domain.QualityIndicators;
import com.projeto.max.repository.QualityIndicatorsRepository;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class QualityIndicatorsService extends GenericService<QualityIndicators, Long> {

    private final QualityIndicatorsRepository qualityIndicatorsRepository;
    public QualityIndicatorsService(QualityIndicatorsRepository qualityIndicatorsRepository) {
        super(qualityIndicatorsRepository);
        this.qualityIndicatorsRepository = qualityIndicatorsRepository;
    }

	public List<QualityIndicators> encontrarPorIdqualityIndicator(Long idqualityIndicator) { return this.qualityIndicatorsRepository.findByIdqualityIndicator(idqualityIndicator); };
	public List<QualityIndicators> encontrarPorCollectionId(Long collectionId) { return this.qualityIndicatorsRepository.findByCollectionId(collectionId); };
	public List<QualityIndicators> encontrarPorAntibioticContendo(String antibiotic) { return this.qualityIndicatorsRepository.findByAntibioticContaining(antibiotic); };
	public List<QualityIndicators> encontrarPorAntibiotic(String antibiotic) { return this.qualityIndicatorsRepository.findByAntibiotic(antibiotic); };
	public List<QualityIndicators> encontrarPorFatContendo(String fat) { return this.qualityIndicatorsRepository.findByFatContaining(fat); };
	public List<QualityIndicators> encontrarPorFat(String fat) { return this.qualityIndicatorsRepository.findByFat(fat); };
	public List<QualityIndicators> encontrarPorApproved(Boolean approved) { return this.qualityIndicatorsRepository.findByApproved(approved); };
	public List<QualityIndicators> encontrarPorQualityContendo(String quality) { return this.qualityIndicatorsRepository.findByQualityContaining(quality); };
	public List<QualityIndicators> encontrarPorQuality(String quality) { return this.qualityIndicatorsRepository.findByQuality(quality); };
	public List<QualityIndicators> encontrarPorDataCadastro(LocalDateTime dataCadastro) { return this.qualityIndicatorsRepository.findByDataCadastro(dataCadastro); };
	public List<QualityIndicators> encontrarPorDataAtualizacao(LocalDateTime dataAtualizacao) { return this.qualityIndicatorsRepository.findByDataAtualizacao(dataAtualizacao); };

}
