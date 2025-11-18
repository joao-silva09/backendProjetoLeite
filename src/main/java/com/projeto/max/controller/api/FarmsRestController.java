package com.projeto.max.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.projeto.max.generic.*;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.max.domain.Farms;
import com.projeto.max.service.FarmsService;
import com.projeto.max.domain.Users;
import com.projeto.max.service.UsersService;

@RestController
@RequestMapping("/api/farms/")
public class FarmsRestController {
    @Autowired
    FarmsService farmsService;
	@Autowired
	UsersService usersService;


	// ---------------------------- FUNÇÕES INICIAIS ----------------------------
	@GetMapping("/ping/")
	public ResponseEntity<Map<String, Object>> ping() {return ReturnJson.success(200, null, "Pong Farms!"); }

	// ---------------------------------- CRUD ----------------------------------
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<Map<String, Object>> encontrarFarms(@PathVariable Long id, HttpServletRequest request) {
        try {
            Farms findFarms = farmsService.id(id);
            if(findFarms == null) return ReturnJson.errorGeral(406, "Farms com id '" + id + "' não existe.");
            return ReturnJson.success(202, findFarms, "Farms encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao procurar Farms.");
        }
    }

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> buscarFarms() {
        try {
            List<Farms> findFarms = farmsService.listar();

            return ReturnJson.success(202, findFarms, "Lista de Farms encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao encontrar lista de Farms.");
        }
    }

	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> salvarFarms(HttpServletRequest request) {
                        
        String pIdfarm = request.getParameter("idfarm");
		String pName = request.getParameter("name");
		String pProducerId = request.getParameter("producerid");
		String pActive = "true";
        
        try {
            String paramatersAreNull = Generic.checkParamatersNull(Arrays.asList("name", "producerid"), request);
            if(paramatersAreNull != null) return ReturnJson.errorGeral(400, paramatersAreNull);
            if(!Generic.validNumber(pProducerId)) return ReturnJson.errorGeral(400, "producerid deve ser um numero valido.");
			if(!"false".equals(pActive.toLowerCase()) && !"true".equals(pActive.toLowerCase())) return ReturnJson.errorGeral(400, "active deve ser um numero valido.");
        } catch (Exception e) {
            return ReturnJson.error(500, e, "Erro ao validar parametros.");
        }

        try {
            Farms curFarms;
            String name = pName.toLowerCase();
			Long producerid = Long.parseLong(pProducerId);
			Users producer = usersService.id(producerid);
			if(producer == null) return ReturnJson.errorGeral(400, "producer nao encontrado.");
			Boolean active = Boolean.parseBoolean(pActive);

            if(!Generic.stringHasContent(pIdfarm)){ // Criando Farms
                curFarms = new Farms(name, producer, active);

                farmsService.salvar(curFarms);
                return ReturnJson.success(202, curFarms, "Farms salva com sucesso.");
            }

            Long idfarm = Long.parseLong(pIdfarm);
            curFarms = new Farms(name, producer, active);
            if(farmsService.id(idfarm) == null){
                return ReturnJson.errorGeral(400, "Erro ao encontrar 'Farms' para atualizar.");
            }
            curFarms.setIdfarm(idfarm);
            farmsService.salvar(curFarms);
            return ReturnJson.success(202, curFarms, "Farms atualizada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao atualizar Farms.");
        }
    }

	@PatchMapping("/")
	public ResponseEntity<Map<String, Object>> atualizarFarms(HttpServletRequest request) {
        return this.salvarFarms(request);
    }

	@DeleteMapping("/")
	public ResponseEntity<Map<String, Object>> deletarFarms(HttpServletRequest request) {
            try {
                String pIdfarm = request.getParameter("idfarm");
                if(!Generic.stringHasContent(pIdfarm)) return ReturnJson.errorGeral(400, "Chave primaria 'idfarm' não pode ser nula.");
                if(!Generic.validNumber(pIdfarm)) return ReturnJson.errorGeral(400, "Chave primaria 'idfarm' deve ser um numero valido.");
                Long id = Long.parseLong(pIdfarm);

                Farms findFarms = farmsService.id(id);
                if(findFarms == null) return ReturnJson.errorGeral(406, "Farms com id '" + id + "' não existe.");

                farmsService.deletar(id);
                return ReturnJson.success(202, findFarms, "Farms deletada com sucesso.");
            } catch (Exception e) {
                return ReturnJson.error(400, e, "Erro ao deletar Farms.");
            }
        }

	// ----------------------- REQUISICOES  CUSTOMIZAVEIS -----------------------
	@GetMapping("/contar/")
	public ResponseEntity<Map<String, Object>> contarFarms() {
        try {
            Long countFarms = farmsService.contar();

            return ReturnJson.success(202, countFarms, "Sucesso ao buscar quantidade.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao buscar quantidade.");
        }
    }

    @GetMapping("/qrcode/{id:\\d+}")
	public ResponseEntity<Map<String, Object>> gerarQrCode(@PathVariable Long id, HttpServletRequest request) {
        try {
            Farms findFarms = farmsService.id(id);
            if(findFarms == null) return ReturnJson.errorGeral(406, "Farms com id '" + id + "' não existe.");

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES);
            String jsonString = objectMapper.writeValueAsString(findFarms);
            System.out.println(jsonString);

            String base64 = GeradorQrCode.gerarQrCodeBase64(jsonString, 500, 500);
            return ReturnJson.success(202, base64, "Qr code gerado com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao gerar qr code de Farm.");
        }
    }
}
