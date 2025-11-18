# Endpoints da API (Spring Boot)

Este documento descreve os endpoints disponíveis, seus objetivos, parâmetros esperados e o padrão de resposta.

## Padrão de Resposta
- Estrutura base: `{ success, message, content, status, time }`.
- Sucesso: `status 202` na maioria das operações; `status 200` em `ping`.
- Erros de validação/processamento: `status 400`.
- Não encontrado: `status 406`.
- Erro de validação de parâmetros: `status 500` quando ocorre exceção durante checagem.

## Animals
- GET `/api/animals/ping/`: verificação de disponibilidade.
- GET `/api/animals/{id}`: busca um `Animal` por id.
- GET `/api/animals/`: lista todos os `Animals`.
- POST `/api/animals/`: cria ou atualiza `Animal`.
  - Parâmetros obrigatórios: `name`.
  - Atualização: enviar `idanimal` para atualizar; sem `idanimal` cria.
- PATCH `/api/animals/`: alias de `POST` para atualizar/criar.
- DELETE `/api/animals/`: exclui um `Animal`.
  - Parâmetros obrigatórios: `idanimal`.
- GET `/api/animals/contar/`: retorna quantidade total.

### Saída (`content`)
`Animals` (`src\\main\\java\\com\\projeto\\max\\domain\\Animals.java:7`): `{ idanimal, name, dataCadastro, dataAtualizacao }`.

## Collections
- GET `/api/collections/ping/`: verificação de disponibilidade.
- GET `/api/collections/{id}`: busca uma `Collection` por id.
- GET `/api/collections/`: lista todas as `Collections`.
- POST `/api/collections/`: cria ou atualiza `Collection`.
  - Parâmetros obrigatórios: `farmid`, `producerid`, `collectorid`, `quantity`, `temperature`, `acidity`, `producerPresent` (boolean), `collectionDate` (datetime ISO).
  - Parâmetros opcionais: `observations`.
  - Regras: `producer` deve ser do tipo produtor; `collector` deve ser do tipo coletor.
  - Atualização: enviar `idcollection` para atualizar; sem `idcollection` cria.
- PATCH `/api/collections/`: alias de `POST` para atualizar/criar.
- DELETE `/api/collections/`: exclui uma `Collection`.
  - Parâmetros obrigatórios: `idcollection`.
- GET `/api/collections/contar/`: retorna quantidade total.

### Saída (`content`)
`Collections` (`src\\main\\java\\com\\projeto\\max\\domain\\Collections.java:7`): `{ idcollection, animal, farm, producer, collector, quantity, temperature, acidity, producerPresent, observations, collectionDate, dataCadastro, dataAtualizacao }`
- `animal`: objeto `Animals`.
- `farm`: objeto `Farms`.
- `producer`: objeto `Users`.
- `collector`: objeto `Users`.

## Dairys
- GET `/api/dairys/ping/`: verificação de disponibilidade.
- GET `/api/dairys/{id}`: busca um `Dairy` por id.
- GET `/api/dairys/`: lista todos os `Dairys`.
- POST `/api/dairys/`: cria ou atualiza `Dairy`.
  - Parâmetros obrigatórios: `name`.
  - Atualização: enviar `iddairy` para atualizar; sem `iddairy` cria.
- PATCH `/api/dairys/`: alias de `POST` para atualizar/criar.
- DELETE `/api/dairys/`: exclui um `Dairy`.
  - Parâmetros obrigatórios: `iddairy`.
- GET `/api/dairys/contar/`: retorna quantidade total.

### Saída (`content`)
`Dairys` (`src\\main\\java\\com\\projeto\\max\\domain\\Dairys.java:7`): `{ iddairy, name, dataCadastro, dataAtualizacao }`.

## Farms
- GET `/api/farms/ping/`: verificação de disponibilidade.
- GET `/api/farms/{id}`: busca uma `Farm` por id.
- GET `/api/farms/`: lista todas as `Farms`.
- POST `/api/farms/`: cria ou atualiza `Farm`.
  - Parâmetros obrigatórios: `name`, `producerid` (deve existir e ser do tipo produtor).
  - Parâmetro opcional: `active` (boolean, default `true`).
  - Atualização: enviar `idfarm` para atualizar; sem `idfarm` cria.
- PATCH `/api/farms/`: alias de `POST` para atualizar/criar.
- DELETE `/api/farms/`: exclui uma `Farm`.
  - Parâmetros obrigatórios: `idfarm`.
- GET `/api/farms/contar/`: retorna quantidade total.
- GET `/api/farms/qrcode/{id}`: gera QR Code (base64) contendo o JSON da `Farm`.

### Saída (`content`)
`Farms` (`src\\main\\java\\com\\projeto\\max\\domain\\Farms.java:7`): `{ idfarm, name, producer, active, dataCadastro, dataAtualizacao }`
- `producer`: objeto `Users`.
- `qrcode/{id}`: `content` é uma `String` base64.

## LogsCollection
- GET `/api/logscollection/ping/`: verificação de disponibilidade.
- GET `/api/logscollection/{id}`: busca um `LogsCollection` por id.
- GET `/api/logscollection/`: lista todos os `LogsCollection`.
- POST `/api/logscollection/`: cria ou atualiza `LogsCollection`.
  - Parâmetros obrigatórios: `name`, `description`, `userid`.
  - Atualização: enviar `idlogCollection` para atualizar; sem `idlogCollection` cria.
- PATCH `/api/logscollection/`: alias de `POST` para atualizar/criar.
- DELETE `/api/logscollection/`: exclui um `LogsCollection`.
  - Parâmetros obrigatórios: `idlogCollection`.
- GET `/api/logscollection/contar/`: retorna quantidade total.

### Saída (`content`)
`LogsCollection` (`src\\main\\java\\com\\projeto\\max\\domain\\LogsCollection.java:7`): `{ idlogCollection, name, description, user, dataCadastro, dataAtualizacao }`
- `user`: objeto `Users`.

## Users
- GET `/api/users/ping/`: verificação de disponibilidade.
- GET `/api/users/{id}`: busca um `User` por id.
- GET `/api/users/`: lista todos os `Users`.
- POST `/api/users/`: cria ou atualiza `User`.
  - Parâmetros obrigatórios: `name`, `document` (CPF/CNPJ válido), `email`, `passwordHash`, `type` (id de `UserTypes`).
  - Parâmetro opcional: `active` (boolean, default `true`).
  - Atualização: enviar `iduser` para atualizar; sem `iduser` cria.
- PATCH `/api/users/`: alias de `POST` para atualizar/criar.
- DELETE `/api/users/`: exclui um `User`.
  - Parâmetros obrigatórios: `iduser`.
- GET `/api/users/ativar/{id}`: marca `User.active = true`.
- GET `/api/users/desativar/{id}`: marca `User.active = false`.
- GET `/api/users/contar/`: retorna quantidade total.

### Saída (`content`)
`Users` (`src\\main\\java\\com\\projeto\\max\\domain\\Users.java:7`): `{ iduser, name, document, email, passwordHash, user, active, dataCadastro, dataAtualizacao }`
- `user`: objeto `UserTypes` (`src\\main\\java\\com\\projeto\\max\\domain\\UserTypes.java:7`).
- `ativar/{id}` e `desativar/{id}`: retornam `Users` com `active` ajustado.

## UserTypes
- GET `/api/usertypes/ping/`: verificação de disponibilidade.
- GET `/api/usertypes/{id}`: busca um `UserTypes` por id.
- GET `/api/usertypes/`: lista todos os `UserTypes`.
- POST `/api/usertypes/`: cria ou atualiza `UserTypes`.
  - Parâmetros obrigatórios: `name`.
  - Atualização: enviar `iduserTypes` para atualizar; sem `iduserTypes` cria.
- PATCH `/api/usertypes/`: alias de `POST` para atualizar/criar.
- DELETE `/api/usertypes/`: exclui um `UserTypes`.
  - Parâmetros obrigatórios: `iduserTypes`.
- GET `/api/usertypes/contar/`: retorna quantidade total.

### Saída (`content`)
`UserTypes` (`src\\main\\java\\com\\projeto\\max\\domain\\UserTypes.java:7`): `{ iduserTypes, name, dataCadastro, dataAtualizacao }`.

## QualityIndicators
- GET `/api/qualityindicators/ping/`: verificação de disponibilidade.
- GET `/api/qualityindicators/{id}`: busca um `QualityIndicators` por id.
- GET `/api/qualityindicators/`: lista todos os `QualityIndicators`.
- POST `/api/qualityindicators/`: cria ou atualiza `QualityIndicators`.
  - Parâmetros obrigatórios: `collectionId`, `antibiotic`, `fat`, `approved` (boolean), `quality`.
  - Atualização: enviar `idqualityIndicator` para atualizar; sem `idqualityIndicator` cria.
- PATCH `/api/qualityindicators/`: alias de `POST` para atualizar/criar.
- DELETE `/api/qualityindicators/`: exclui um `QualityIndicators`.
  - Parâmetros obrigatórios: `idqualityIndicator`.
- GET `/api/qualityindicators/contar/`: retorna quantidade total.

### Saída (`content`)
`QualityIndicators` (`src\\main\\java\\com\\projeto\\max\\domain\\QualityIndicators.java:7`): `{ idqualityIndicator, collectionId, antibiotic, fat, approved, quality, dataCadastro, dataAtualizacao }`.

## QualityIndicatorExtra
- GET `/api/qualityindicatorextra/ping/`: verificação de disponibilidade.
- GET `/api/qualityindicatorextra/{id}`: busca um `QualityIndicatorExtra` por id.
- GET `/api/qualityindicatorextra/`: lista todos os `QualityIndicatorExtra`.
- POST `/api/qualityindicatorextra/`: cria ou atualiza `QualityIndicatorExtra`.
  - Parâmetros obrigatórios: `indicatorId`, `key`, `value`.
  - Atualização: enviar `idqualityIndicatorExtra` para atualizar; sem `idqualityIndicatorExtra` cria.
- PATCH `/api/qualityindicatorextra/`: alias de `POST` para atualizar/criar.
- DELETE `/api/qualityindicatorextra/`: exclui um `QualityIndicatorExtra`.
  - Parâmetros obrigatórios: `idqualityIndicatorExtra`.
- GET `/api/qualityindicatorextra/contar/`: retorna quantidade total.

### Saída (`content`)
`QualityIndicatorExtra` (`src\\main\\java\\com\\projeto\\max\\domain\\QualityIndicatorExtra.java:7`): `{ idqualityIndicatorExtra, indicatorId, key, value, dataCadastro, dataAtualizacao }`.

## UserDairy
- GET `/api/userdairy/{id}`: endpoint para buscar por chave composta (Dairy, User).
  - Observação: a assinatura atual do método espera duas chaves (`idDairy`, `idUser`), mas o caminho mapeado só possui um `{id}`.
- GET `/api/userdairy/`: lista todos os `UserDairy`.
- POST `/api/userdairy/`: cria ou atualiza `UserDairy` (chave composta).
  - Parâmetros obrigatórios: `dairy` (id de `Dairys`), `user` (id de `Users`).
- PATCH `/api/userdairy/`: alias de `POST` para atualizar/criar.
- DELETE `/api/userdairy/`: exclui um `UserDairy` pela chave composta.
  - Parâmetros obrigatórios: `dairys`, `users`.
- GET `/api/userdairy/contar/`: retorna quantidade total.

### Saída (`content`)
`UserDairy` (`src\\main\\java\\com\\projeto\\max\\domain\\UserDairy.java:7`): `{ userDairyKey, dataCadastro, dataAtualizacao }`
- `userDairyKey`: `{ dairy, user }`, onde `dairy` é `Dairys` e `user` é `Users` (`src\\main\\java\\com\\projeto\\max\\domain\\UserDairyKey.java:7`).

## Collectors
- GET `/api/collectors/stats/`: estatísticas do mês corrente para um produtor.
  - Parâmetros obrigatórios: `producerid`.
  - Retorno `content`: `{ collectionsToday, lastCollection }`.

### Saída (`content`)
- `collectionsToday`: `Integer`.
- `lastCollection`: objeto `Collections`.

## Producers
- GET `/api/producers/{id}/collections/`: lista `Collections` de um produtor.
- GET `/api/producers/stats/`: estatísticas agregadas e últimos 30 dias.
  - Parâmetros obrigatórios: `producerid`.
  - Parâmetros opcionais: `year`, `month`.
  - Retorno `content`: `{ currentMonth: { totalLiters, averageDaily, averageAcidity, averageTemperature }, last30Days: [{ date, quantity }] }`.

### Saída (`content`)
- `collections` (por id): lista de `Collections` do produtor.
- `stats`:
  - `currentMonth`: `{ totalLiters: Float, averageDaily: Float, averageAcidity: Float, averageTemperature: Float }`.
  - `last30Days`: `[{ date: String (dd/MM/yyyy HH:mm:ss), quantity: Float }]`.

## Admin
- POST `/api/admin/load/`: popula dados básicos (`Animals` e `UserTypes`).