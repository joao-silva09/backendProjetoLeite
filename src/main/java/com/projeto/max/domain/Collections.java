package com.projeto.max.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Entity
public class Collections {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idcollection;

	@ManyToOne
	@JoinColumn(referencedColumnName = "idanimal", nullable = false)
	private Animals animal;

	@ManyToOne
	@JoinColumn(referencedColumnName = "idfarm", nullable = false)
	private Farms farm;

	@ManyToOne
	@JoinColumn(referencedColumnName = "iduser", nullable = false)
	private Users producer;

	@ManyToOne
	@JoinColumn(referencedColumnName = "iduser", nullable = false)
	private Users collector;

	@Column(nullable = false)
	private Float quantity;

	@Column(nullable = false)
	private Float temperature;

	@Column(nullable = false)
	private Float acidity;

	@Column(nullable = false)
	private Boolean producerPresent;

	@Column(nullable = true)
	private String observations;

	@Column(nullable = false, columnDefinition = "boolean default false")
	private Boolean edited = false;

	@Column(nullable = false, columnDefinition = "integer default 0")
	private Integer editCount = 0;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime collectionDate;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime dataCadastro;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime dataAtualizacao;

    public Collections() {
		this.dataCadastro = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
    }
    public Collections(Animals animal, Farms farm, Users producer, Users collector, Float quantity, Float temperature, Float acidity, Boolean producerPresent, String observations, LocalDateTime collectionDate) {
		this.animal = animal; // fk
		this.farm = farm; // fk
		this.producer = producer; // fk
		this.collector = collector; // fk
		this.quantity = quantity;
		this.temperature = temperature;
		this.acidity = acidity;
		this.producerPresent = producerPresent;
		this.observations = observations;
		this.collectionDate = collectionDate;
    
		this.dataCadastro = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
    }


	public Long getIdcollection() { return idcollection; }
	public void setIdcollection(Long idcollection) { this.idcollection = idcollection; }

	public Animals getAnimal() { return animal; }
	public void setAnimal(Animals animal) { this.animal = animal; }

	public Farms getFarm() { return farm; }
	public void setFarm(Farms farm) { this.farm = farm; }

	public Users getProducer() { return producer; }
	public void setProducer(Users producer) { this.producer = producer; }

	public Users getCollector() { return collector; }
	public void setCollector(Users collector) { this.collector = collector; }

	public Float getQuantity() { return quantity; }
	public void setQuantity(Float quantity) { this.quantity = quantity; }

	public Float getTemperature() { return temperature; }
	public void setTemperature(Float temperature) { this.temperature = temperature; }

	public Float getAcidity() { return acidity; }
	public void setAcidity(Float acidity) { this.acidity = acidity; }

	public Boolean getProducerPresent() { return producerPresent; }
	public void setProducerPresent(Boolean producerPresent) { this.producerPresent = producerPresent; }

	public String getObservations() { return observations; }
	public void setObservations(String observations) { this.observations = observations; }

	public LocalDateTime getCollectionDate() { return collectionDate; }
	public void setCollectionDate(LocalDateTime collectionDate) { this.collectionDate = collectionDate; }

	public LocalDateTime getDataCadastro() { return dataCadastro; }
	public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

	public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
	public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

	public Boolean getEdited() { return edited; }
	public void setEdited(Boolean edited) { this.edited = edited; }

	public Integer getEditCount() { return editCount; }
	public void setEditCount(Integer editCount) { this.editCount = editCount; }

}
