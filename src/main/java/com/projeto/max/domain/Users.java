package com.projeto.max.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Entity
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long iduser;

	@Column(nullable = false, length = 150)
	private String name;

	@Column(nullable = false, length = 20)
	private String document;

	@Column(nullable = false, length = 150)
	private String email;

	@Column(nullable = false, length = 255)
	private String passwordHash;

	@ManyToOne
	@JoinColumn(referencedColumnName = "iduserTypes", nullable = false)
	private UserTypes user;

	@Column()
	private Boolean active;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime dataCadastro;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(nullable = false)
	private LocalDateTime dataAtualizacao;

    public Users() {
		this.dataCadastro = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
    }
    public Users(String name, String document, String email, String passwordHash, UserTypes user, Boolean active) {
		this.name = name;
		this.document = document;
		this.email = email;
		this.passwordHash = passwordHash;
		this.user = user; // fk
		this.active = active;
    
		this.dataCadastro = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
    }


	public Long getIduser() { return iduser; }
	public void setIduser(Long iduser) { this.iduser = iduser; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getDocument() { return document; }
	public void setDocument(String document) { this.document = document; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getPasswordHash() { return passwordHash; }
	public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

	public UserTypes getUser() { return user; }
	public void setUser(UserTypes user) { this.user = user; }

	public Boolean getActive() { return active; }
	public void setActive(Boolean active) { this.active = active; }

	public LocalDateTime getDataCadastro() { return dataCadastro; }
	public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

	public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
	public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

}
