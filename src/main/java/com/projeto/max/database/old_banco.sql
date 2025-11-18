DROP DATABASE IF EXISTS projetoleite;
CREATE DATABASE IF NOT EXISTS projetoleite;
USE projetoleite;

CREATE TABLE animal (
  idanimal INT NOT NULL,
  nome VARCHAR(100) NOT NULL,
  PRIMARY KEY (idanimal)
);

CREATE TABLE cargo (
  idcargo INT NOT NULL,
  nome VARCHAR(45) NOT NULL,
  PRIMARY KEY (idcargo)
);

CREATE TABLE usuario (
  idusuario INT NOT NULL,
  nome VARCHAR(100) NOT NULL,
  cpf CHAR(11) NOT NULL,
  dt_cadastro DATETIME NOT NULL,
  cargo_idcargo INT NOT NULL,
  PRIMARY KEY (idusuario),
  FOREIGN KEY (cargo_idcargo) REFERENCES cargo (idcargo)
);

CREATE TABLE laticionio (
  idlaticionio INT NOT NULL,
  nome VARCHAR(200) NOT NULL,
  PRIMARY KEY (idlaticionio)
);

CREATE TABLE fazenda (
  idfazenda INT NOT NULL,
  nome VARCHAR(100) NOT NULL,
  descricao VARCHAR(300) NOT NULL,
  PRIMARY KEY (idfazenda)
);

CREATE TABLE log_coleta (
  idcoleta_leite INT NOT NULL,
  descricao VARCHAR(200) NOT NULL,
  dt_coleta DATETIME NOT NULL,
  temperatura FLOAT NOT NULL,
  acidez FLOAT NOT NULL,
  animal_idanimal INT NOT NULL DEFAULT 1,
  fazenda_idfazenda INT NOT NULL,
  coletor_idcoletor INT NOT NULL,
  produtor_idprodutor INT NOT NULL,
  PRIMARY KEY (idcoleta_leite),
  FOREIGN KEY (animal_idanimal) REFERENCES animal (idanimal),
  FOREIGN KEY (fazenda_idfazenda) REFERENCES fazenda (idfazenda),
  FOREIGN KEY (coletor_idcoletor) REFERENCES usuario (idusuario),
  FOREIGN KEY (produtor_idprodutor) REFERENCES usuario (idusuario)
);

CREATE TABLE rel_laticionio (
  laticionio_idlaticionio INT NOT NULL,
  usuario_idusuario INT NOT NULL,
  PRIMARY KEY (laticionio_idlaticionio, usuario_idusuario),
  FOREIGN KEY (laticionio_idlaticionio) REFERENCES laticionio (idlaticionio),
  FOREIGN KEY (usuario_idusuario) REFERENCES usuario (idusuario)
);

CREATE TABLE indicadores_qualidade (
  idindicadores_qualidade INT NOT NULL,
  nome VARCHAR(100) NOT NULL,
  PRIMARY KEY (idindicadores_qualidade)
);

CREATE TABLE indicadores_leite (
  valor VARCHAR(100) NOT NULL,
  dt_cadastro DATETIME NOT NULL,
  dt_atualizacao DATETIME NOT NULL,
  coleta_leite_idcoleta_leite INT NOT NULL,
  indicadores_qualidade_idindicadores_qualidade INT NOT NULL,
  PRIMARY KEY (coleta_leite_idcoleta_leite, indicadores_qualidade_idindicadores_qualidade),
  FOREIGN KEY (coleta_leite_idcoleta_leite) REFERENCES log_coleta (idcoleta_leite),
  FOREIGN KEY (indicadores_qualidade_idindicadores_qualidade) REFERENCES indicadores_qualidade (idindicadores_qualidade)
);
