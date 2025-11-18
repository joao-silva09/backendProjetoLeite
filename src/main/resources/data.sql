/* Definição de animais */
INSERT INTO animals (idanimal, name, data_cadastro, data_atualizacao) VALUES (1, 'vaca', NOW(), NOW()) ON CONFLICT (idanimal) DO NOTHING;
INSERT INTO animals (idanimal, name, data_cadastro, data_atualizacao) VALUES (2, 'cabra', NOW(), NOW()) ON CONFLICT (idanimal) DO NOTHING;

/* Definição de tipos de usuarios */
INSERT INTO user_types (iduser_types, name, data_cadastro, data_atualizacao) VALUES (1, 'coletor', NOW(), NOW()) ON CONFLICT (iduser_types) DO NOTHING;
INSERT INTO user_types (iduser_types, name, data_cadastro, data_atualizacao) VALUES (2, 'produtor', NOW(), NOW()) ON CONFLICT (iduser_types) DO NOTHING;
INSERT INTO user_types (iduser_types, name, data_cadastro, data_atualizacao) VALUES (3, 'laticionio', NOW(), NOW()) ON CONFLICT (iduser_types) DO NOTHING;
INSERT INTO user_types (iduser_types, name, data_cadastro, data_atualizacao) VALUES (4, 'admin', NOW(), NOW()) ON CONFLICT (iduser_types) DO NOTHING;