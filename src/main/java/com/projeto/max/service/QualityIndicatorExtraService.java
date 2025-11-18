package com.projeto.max.service;

import org.springframework.stereotype.Service;
import com.projeto.max.domain.QualityIndicatorExtra;
import com.projeto.max.repository.QualityIndicatorExtraRepository;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class QualityIndicatorExtraService extends GenericService<QualityIndicatorExtra, Long> {

    private final QualityIndicatorExtraRepository qualityIndicatorExtraRepository;
    public QualityIndicatorExtraService(QualityIndicatorExtraRepository qualityIndicatorExtraRepository) {
        super(qualityIndicatorExtraRepository);
        this.qualityIndicatorExtraRepository = qualityIndicatorExtraRepository;
    }

	public List<QualityIndicatorExtra> encontrarPorIdqualityIndicatorExtra(Long idqualityIndicatorExtra) { return this.qualityIndicatorExtraRepository.findByIdqualityIndicatorExtra(idqualityIndicatorExtra); };
	public List<QualityIndicatorExtra> encontrarPorIndicatorId(Long indicatorId) { return this.qualityIndicatorExtraRepository.findByIndicatorId(indicatorId); };
	public List<QualityIndicatorExtra> encontrarPorKeyContendo(String key) { return this.qualityIndicatorExtraRepository.findByKeyContaining(key); };
	public List<QualityIndicatorExtra> encontrarPorKey(String key) { return this.qualityIndicatorExtraRepository.findByKey(key); };
	public List<QualityIndicatorExtra> encontrarPorValueContendo(String value) { return this.qualityIndicatorExtraRepository.findByValueContaining(value); };
	public List<QualityIndicatorExtra> encontrarPorValue(String value) { return this.qualityIndicatorExtraRepository.findByValue(value); };
	public List<QualityIndicatorExtra> encontrarPorDataCadastro(LocalDateTime dataCadastro) { return this.qualityIndicatorExtraRepository.findByDataCadastro(dataCadastro); };
	public List<QualityIndicatorExtra> encontrarPorDataAtualizacao(LocalDateTime dataAtualizacao) { return this.qualityIndicatorExtraRepository.findByDataAtualizacao(dataAtualizacao); };

}
