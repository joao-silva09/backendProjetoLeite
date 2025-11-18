package com.projeto.max.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Entity
public class Farms {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idfarm;

	@Column(nullable = false, length = 150)
	private String name;

	@ManyToOne
	@JoinColumn(referencedColumnName = "iduser", nullable = false)
	private Users producer;

	@Column()
	private Boolean active;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime dataCadastro;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime dataAtualizacao;

    public Farms() {
		this.dataCadastro = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
    }
    public Farms(String name, Users producer, Boolean active) {
		this.name = name;
		this.producer = producer; // fk
		this.active = active;
    
		this.dataCadastro = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
    }


	public Long getIdfarm() { return idfarm; }
	public void setIdfarm(Long idfarm) { this.idfarm = idfarm; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public Users getProducer() { return producer; }
	public void setProducer(Users producer) { this.producer = producer; }

	public Boolean getActive() { return active; }
	public void setActive(Boolean active) { this.active = active; }

	public LocalDateTime getDataCadastro() { return dataCadastro; }
	public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

	public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
	public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

}
