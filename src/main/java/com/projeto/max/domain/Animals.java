package com.projeto.max.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Entity
public class Animals {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idanimal;

	@Column(nullable = false, length = 100)
	private String name;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime dataCadastro;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime dataAtualizacao;

    public Animals() {
		this.dataCadastro = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
    }
    public Animals(String name) {
		this.name = name;
    
		this.dataCadastro = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
    }


	public Long getIdanimal() { return idanimal; }
	public void setIdanimal(Long idanimal) { this.idanimal = idanimal; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public LocalDateTime getDataCadastro() { return dataCadastro; }
	public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

	public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
	public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

}
