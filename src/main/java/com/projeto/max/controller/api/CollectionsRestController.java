package com.projeto.max.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.projeto.max.generic.*;
import com.projeto.max.domain.Collections;
import com.projeto.max.service.CollectionsService;
import com.projeto.max.domain.Animals;
import com.projeto.max.service.AnimalsService;
import com.projeto.max.domain.Farms;
import com.projeto.max.service.FarmsService;
import com.projeto.max.domain.Users;
import com.projeto.max.service.UsersService;
import com.projeto.max.domain.UserTypes;
import com.projeto.max.domain.LogsCollection;
import com.projeto.max.service.LogsCollectionService;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/collections/")
public class CollectionsRestController {
    @Autowired
    CollectionsService collectionsService;
	@Autowired
	AnimalsService animalsService;
	@Autowired
	FarmsService farmsService;
	@Autowired
	UsersService usersService;
	@Autowired
	LogsCollectionService logsCollectionService;


	// ---------------------------- FUNÇÕES INICIAIS ----------------------------
	@GetMapping("/ping/")
	public ResponseEntity<Map<String, Object>> ping() {return ReturnJson.success(200, null, "Pong Collections!"); }

	// ---------------------------------- CRUD ----------------------------------
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<Map<String, Object>> encontrarCollections(@PathVariable Long id, HttpServletRequest request) {
        try {
            Collections findCollections = collectionsService.id(id);
            if(findCollections == null) return ReturnJson.errorGeral(406, "Collections com id '" + id + "' não existe.");
            return ReturnJson.success(202, findCollections, "Collections encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao procurar Collections.");
        }
    }

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> buscarCollections() {
        try {
            List<Collections> findCollections = collectionsService.listar();

            return ReturnJson.success(202, findCollections, "Lista de Collections encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao encontrar lista de Collections.");
        }
    }

	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> salvarCollections(HttpServletRequest request) {
                        
        String pIdcollection = request.getParameter("idcollection");
		String pAnimalId = request.getParameter("animalid");
		if(pAnimalId == null || pAnimalId.isEmpty()) pAnimalId = "1"; // Default: vaca
		String pFarmId = request.getParameter("farmid");
		String pProducerId = request.getParameter("producerid");
		String pCollectorId = request.getParameter("collectorid");
		String pQuantity = request.getParameter("quantity");
		String pTemperature = request.getParameter("temperature");
		String pAcidity = request.getParameter("acidity");
		String pProducerPresent = request.getParameter("producerPresent");
		String pObservations = request.getParameter("observations");
		String pCollectionDate = request.getParameter("collectionDate");
        
        try {
            String paramatersAreNull = Generic.checkParamatersNull(Arrays.asList("farmid", "producerid", "collectorid", "quantity", "temperature", "acidity", "producerPresent", "collectionDate"), request);
            if(paramatersAreNull != null) return ReturnJson.errorGeral(400, paramatersAreNull);
            if(!Generic.validNumber(pAnimalId)) return ReturnJson.errorGeral(400, "animalid deve ser um numero valido.");
			if(!Generic.validNumber(pFarmId)) return ReturnJson.errorGeral(400, "farmid deve ser um numero valido.");
			if(!Generic.validNumber(pProducerId)) return ReturnJson.errorGeral(400, "producerid deve ser um numero valido.");
			if(!Generic.validNumber(pCollectorId)) return ReturnJson.errorGeral(400, "collectorid deve ser um numero valido.");
			if(!Generic.validNumber(pQuantity)) return ReturnJson.errorGeral(400, "quantity deve ser um numero valido.");
			if(!Generic.validNumber(pTemperature)) return ReturnJson.errorGeral(400, "temperature deve ser um numero valido.");
			if(!Generic.validNumber(pAcidity)) return ReturnJson.errorGeral(400, "acidity deve ser um numero valido.");
			if(!"false".equals(pProducerPresent.toLowerCase()) && !"true".equals(pProducerPresent.toLowerCase())) return ReturnJson.errorGeral(400, "producerPresent deve ser um numero valido.");
			if(!Generic.validLocalDateTime(pCollectionDate)) return ReturnJson.errorGeral(400, "collectionDate deve ter uma data valida.");
        } catch (Exception e) {
            return ReturnJson.error(500, e, "Erro ao validar parametros.");
        }

        try {
            Collections curCollections;
            Long animalid = Long.parseLong(pAnimalId);
			Animals animal = animalsService.id(animalid);
			if(animal == null) return ReturnJson.errorGeral(400, "animal nao encontrado.");
			Long farmid = Long.parseLong(pFarmId);
			Farms farm = farmsService.id(farmid);
			if(farm == null) return ReturnJson.errorGeral(400, "farm nao encontrado.");
			Long producerid = Long.parseLong(pProducerId);
			Users producer = usersService.id(producerid);
			if(producer == null) return ReturnJson.errorGeral(400, "producer nao encontrado.");

            UserTypes typeProducer = producer.getUser();
			if(typeProducer.getIduserTypes() != Generic.produtor) return ReturnJson.errorGeral(400, "producer deve ter cargo de produtor.");

			Long collectorid = Long.parseLong(pCollectorId);
			Users collector = usersService.id(collectorid);
			if(collector == null) return ReturnJson.errorGeral(400, "collector nao encontrado.");

            UserTypes typeCollector = collector.getUser();
			if(typeCollector.getIduserTypes() != Generic.coletor) return ReturnJson.errorGeral(400, "collector deve ter cargo de coletor.");

			Float quantity = Float.parseFloat(pQuantity);
			Float temperature = Float.parseFloat(pTemperature);
			Float acidity = Float.parseFloat(pAcidity);
			Boolean producerPresent = Boolean.parseBoolean(pProducerPresent);
			String observations = "";
            if(pObservations != null) {
                if(!(pObservations.trim().isEmpty())) observations = pObservations.toLowerCase();
            }
			LocalDateTime collectionDate = Generic.formatLocalDateTime(pCollectionDate);

            if(!Generic.stringHasContent(pIdcollection)){ // Criando Collections
                curCollections = new Collections(animal, farm, producer, collector, quantity, temperature, acidity, producerPresent, observations, collectionDate);

                collectionsService.salvar(curCollections);
                return ReturnJson.success(202, curCollections, "Collections salva com sucesso.");
            }

            // Atualizando Collections existente
            Long idcollection = Long.parseLong(pIdcollection);
            Collections existingCollection = collectionsService.id(idcollection);
            if(existingCollection == null){
                return ReturnJson.errorGeral(400, "Erro ao encontrar 'Collections' para atualizar.");
            }

            // Gerar descrição das alterações para o log
            StringBuilder logDescription = new StringBuilder();
            logDescription.append("Edição #").append(existingCollection.getEditCount() + 1).append(" - Alterações: ");
            
            if (!existingCollection.getQuantity().equals(quantity)) {
                logDescription.append("Quantidade: ").append(existingCollection.getQuantity()).append(" -> ").append(quantity).append("; ");
            }
            if (!existingCollection.getTemperature().equals(temperature)) {
                logDescription.append("Temperatura: ").append(existingCollection.getTemperature()).append(" -> ").append(temperature).append("; ");
            }
            if (!existingCollection.getAcidity().equals(acidity)) {
                logDescription.append("Acidez: ").append(existingCollection.getAcidity()).append(" -> ").append(acidity).append("; ");
            }
            if (!existingCollection.getProducerPresent().equals(producerPresent)) {
                logDescription.append("Produtor presente: ").append(existingCollection.getProducerPresent()).append(" -> ").append(producerPresent).append("; ");
            }
            if (!existingCollection.getAnimal().getIdanimal().equals(animal.getIdanimal())) {
                logDescription.append("Animal: ").append(existingCollection.getAnimal().getName()).append(" -> ").append(animal.getName()).append("; ");
            }
            if (!existingCollection.getFarm().getIdfarm().equals(farm.getIdfarm())) {
                logDescription.append("Fazenda: ").append(existingCollection.getFarm().getName()).append(" -> ").append(farm.getName()).append("; ");
            }
            String existingObs = existingCollection.getObservations() != null ? existingCollection.getObservations() : "";
            if (!existingObs.equals(observations)) {
                logDescription.append("Observações alteradas; ");
            }

            curCollections = new Collections(animal, farm, producer, collector, quantity, temperature, acidity, producerPresent, observations, collectionDate);
            curCollections.setIdcollection(idcollection);
            curCollections.setDataCadastro(existingCollection.getDataCadastro());
            curCollections.setEdited(true);
            curCollections.setEditCount(existingCollection.getEditCount() + 1);
            
            collectionsService.salvar(curCollections);

            // Registrar log da edição
            LogsCollection log = new LogsCollection(
                curCollections,
                "EDIÇÃO DE COLETA",
                logDescription.toString(),
                collector // Quem fez a edição
            );
            logsCollectionService.salvar(log);

            return ReturnJson.success(202, curCollections, "Collections atualizada com sucesso. Log registrado.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao atualizar Collections.");
        }
    }

	@PatchMapping("/")
	public ResponseEntity<Map<String, Object>> atualizarCollections(HttpServletRequest request) {
        return this.salvarCollections(request);
    }

	@DeleteMapping("/")
	public ResponseEntity<Map<String, Object>> deletarCollections(HttpServletRequest request) {
            try {
                String pIdcollection = request.getParameter("idcollection");
                if(!Generic.stringHasContent(pIdcollection)) return ReturnJson.errorGeral(400, "Chave primaria 'idcollection' não pode ser nula.");
                if(!Generic.validNumber(pIdcollection)) return ReturnJson.errorGeral(400, "Chave primaria 'idcollection' deve ser um numero valido.");
                Long id = Long.parseLong(pIdcollection);

                Collections findCollections = collectionsService.id(id);
                if(findCollections == null) return ReturnJson.errorGeral(406, "Collections com id '" + id + "' não existe.");

                collectionsService.deletar(id);
                return ReturnJson.success(202, findCollections, "Collections deletada com sucesso.");
            } catch (Exception e) {
                return ReturnJson.error(400, e, "Erro ao deletar Collections.");
            }
        }

	// ----------------------- REQUISICOES  CUSTOMIZAVEIS -----------------------
	@GetMapping("/contar/")
	public ResponseEntity<Map<String, Object>> contarCollections() {
        try {
            Long countCollections = collectionsService.contar();

            return ReturnJson.success(202, countCollections, "Sucesso ao buscar quantidade.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao buscar quantidade.");
        }
    }

}
