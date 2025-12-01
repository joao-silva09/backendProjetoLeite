package com.projeto.max.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Entity
public class LogsCollection {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idlogCollection;

	@ManyToOne
	@JoinColumn(referencedColumnName = "idcollection", nullable = true)
	private Collections collection;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 600)
	private String description;

	@ManyToOne
	@JoinColumn(referencedColumnName = "iduser", nullable = false)
	private Users user;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime dataCadastro;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime dataAtualizacao;

    public LogsCollection() {
		this.dataCadastro = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
    }

    // Construtor sem collection (para logs genéricos)
    public LogsCollection(String name, String description, Users user) {
		this.name = name;
		this.description = description;
		this.user = user; // fk
    
		this.dataCadastro = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
    }

    // Construtor com collection (para logs de edição de coleta)
    public LogsCollection(Collections collection, String name, String description, Users user) {
		this.collection = collection; // fk
		this.name = name;
		this.description = description;
		this.user = user; // fk
    
		this.dataCadastro = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
    }


	public Long getIdlogCollection() { return idlogCollection; }
	public void setIdlogCollection(Long idlogCollection) { this.idlogCollection = idlogCollection; }

	public Collections getCollection() { return collection; }
	public void setCollection(Collections collection) { this.collection = collection; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public Users getUser() { return user; }
	public void setUser(Users user) { this.user = user; }

	public LocalDateTime getDataCadastro() { return dataCadastro; }
	public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

	public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
	public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

}
