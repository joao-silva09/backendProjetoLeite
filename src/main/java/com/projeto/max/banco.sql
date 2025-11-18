DROP DATABASE IF EXISTS projetoleite;
CREATE DATABASE IF NOT EXISTS projetoleite;
USE projetoleite;


CREATE TABLE user_types (
    iduser_types BIGINT,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (iduser_types)
);

CREATE TABLE animals (
    idanimal BIGINT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (idanimal)
);

CREATE TABLE dairys (
    iddairy BIGINT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (iddairy)
);

CREATE TABLE users (
    iduser BIGINT,
    name VARCHAR(150) NOT NULL,
    document VARCHAR(20) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    user_type_id BIGINT NOT NULL,
    active BOOLEAN,
    
    PRIMARY KEY (iduser),
    FOREIGN KEY (user_type_id) REFERENCES user_types(iduser_types)
);

CREATE TABLE user_dairy (
    dairy_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (dairy_id, user_id),
    FOREIGN KEY (dairy_id) REFERENCES dairys(iddairy),
    FOREIGN KEY (user_id) REFERENCES users(iduser)
);

CREATE TABLE farms (
    idfarm BIGINT,
    name VARCHAR(150) NOT NULL,
    producer_id BIGINT NOT NULL,
    active BOOLEAN,

    PRIMARY KEY (idfarm),
    FOREIGN KEY (producer_id) REFERENCES producers(idproducer)
);

CREATE TABLE collections (
    idcollection BIGINT,
    animal_id BIGINT NOT NULL,
    farm_id BIGINT NOT NULL,
    producer_id BIGINT NOT NULL,
    collector_id BIGINT NOT NULL,
    quantity FLOAT NOT NULL,
    temperature FLOAT NOT NULL,
    acidity FLOAT NOT NULL,
    producer_present BOOLEAN NOT NULL,
    observations TEXT,
    collection_date TIMESTAMP NOT NULL,

    PRIMARY KEY (idcollection),
    FOREIGN KEY (animal_id) REFERENCES animals(idanimal),
    FOREIGN KEY (farm_id) REFERENCES farms(idfarm),
    FOREIGN KEY (producer_id) REFERENCES producers(idproducer),
    FOREIGN KEY (collector_id) REFERENCES collectors(idcollector)
);

CREATE TABLE logs_collection (
    idlog_collection BIGINT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(600) NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (idlog_collection),
    FOREIGN KEY (user_id) REFERENCES users(iduser)
);

CREATE TABLE quality_indicators (
    idquality_indicator BIGINT,
    collection_id BIGINT NOT NULL,
    antibiotic VARCHAR(50),
    fat VARCHAR(20),
    approved BOOLEAN,
    quality VARCHAR(20),

    PRIMARY KEY (idquality_indicator),
    FOREIGN KEY (collection_id) REFERENCES collections(idcollection) ON DELETE CASCADE
);

CREATE TABLE quality_indicator_extra (
    idquality_indicator_extra BIGINT,
    indicator_id BIGINT NOT NULL,
    key VARCHAR(100) NOT NULL,
    value VARCHAR(100),

    PRIMARY KEY (idquality_indicator_extra),
    FOREIGN KEY (indicator_id) REFERENCES quality_indicators(idquality_indicator) ON DELETE CASCADE
);