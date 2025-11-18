package com.projeto.max.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Entity
public class QualityIndicatorExtra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idqualityIndicatorExtra;

	@Column(nullable = false)
	private Long indicatorId;

	@Column(nullable = false, length = 100)
	private String key;

	@Column(length = 100)
	private String value;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime dataCadastro;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime dataAtualizacao;

    public QualityIndicatorExtra() {
		this.dataCadastro = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
    }
    public QualityIndicatorExtra(Long indicatorId, String key, String value) {
		this.indicatorId = indicatorId;
		this.key = key;
		this.value = value;
    
		this.dataCadastro = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
    }


	public Long getIdqualityIndicatorExtra() { return idqualityIndicatorExtra; }
	public void setIdqualityIndicatorExtra(Long idqualityIndicatorExtra) { this.idqualityIndicatorExtra = idqualityIndicatorExtra; }

	public Long getIndicatorId() { return indicatorId; }
	public void setIndicatorId(Long indicatorId) { this.indicatorId = indicatorId; }

	public String getKey() { return key; }
	public void setKey(String key) { this.key = key; }

	public String getValue() { return value; }
	public void setValue(String value) { this.value = value; }

	public LocalDateTime getDataCadastro() { return dataCadastro; }
	public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

	public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
	public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

}
