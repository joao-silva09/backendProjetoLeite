package com.projeto.max.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Entity
public class QualityIndicators {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idqualityIndicator;

	@Column(nullable = false)
	private Long collectionId;

	@Column(length = 50)
	private String antibiotic;

	@Column(length = 20)
	private String fat;

	@Column()
	private Boolean approved;

	@Column(length = 20)
	private String quality;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime dataCadastro;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime dataAtualizacao;

    public QualityIndicators() {
		this.dataCadastro = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
    }
    public QualityIndicators(Long collectionId, String antibiotic, String fat, Boolean approved, String quality) {
		this.collectionId = collectionId;
		this.antibiotic = antibiotic;
		this.fat = fat;
		this.approved = approved;
		this.quality = quality;
    
		this.dataCadastro = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
    }


	public Long getIdqualityIndicator() { return idqualityIndicator; }
	public void setIdqualityIndicator(Long idqualityIndicator) { this.idqualityIndicator = idqualityIndicator; }

	public Long getCollectionId() { return collectionId; }
	public void setCollectionId(Long collectionId) { this.collectionId = collectionId; }

	public String getAntibiotic() { return antibiotic; }
	public void setAntibiotic(String antibiotic) { this.antibiotic = antibiotic; }

	public String getFat() { return fat; }
	public void setFat(String fat) { this.fat = fat; }

	public Boolean getApproved() { return approved; }
	public void setApproved(Boolean approved) { this.approved = approved; }

	public String getQuality() { return quality; }
	public void setQuality(String quality) { this.quality = quality; }

	public LocalDateTime getDataCadastro() { return dataCadastro; }
	public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

	public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
	public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

}
