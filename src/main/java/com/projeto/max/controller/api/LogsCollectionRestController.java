package com.projeto.max.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.projeto.max.generic.*;
import com.projeto.max.domain.LogsCollection;
import com.projeto.max.service.LogsCollectionService;
import com.projeto.max.domain.Users;
import com.projeto.max.service.UsersService;

@RestController
@RequestMapping("/api/logscollection/")
public class LogsCollectionRestController {
    @Autowired
    LogsCollectionService logsCollectionService;
	@Autowired
	UsersService usersService;


	// ---------------------------- FUNÇÕES INICIAIS ----------------------------
	@GetMapping("/ping/")
	public ResponseEntity<Map<String, Object>> ping() {return ReturnJson.success(200, null, "Pong LogsCollection!"); }

	// ---------------------------------- CRUD ----------------------------------
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<Map<String, Object>> encontrarLogsCollection(@PathVariable Long id, HttpServletRequest request) {
        try {
            LogsCollection findLogsCollection = logsCollectionService.id(id);
            if(findLogsCollection == null) return ReturnJson.errorGeral(406, "LogsCollection com id '" + id + "' não existe.");
            return ReturnJson.success(202, findLogsCollection, "LogsCollection encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao procurar LogsCollection.");
        }
    }

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> buscarLogsCollection() {
        try {
            List<LogsCollection> findLogsCollection = logsCollectionService.listar();

            return ReturnJson.success(202, findLogsCollection, "Lista de LogsCollection encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao encontrar lista de LogsCollection.");
        }
    }

	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> salvarLogsCollection(HttpServletRequest request) {
                        
        String pIdlogCollection = request.getParameter("idlogCollection");
		String pName = request.getParameter("name");
		String pDescription = request.getParameter("description");
		String pUserId = request.getParameter("userid");
        
        try {
            String paramatersAreNull = Generic.checkParamatersNull(Arrays.asList("name", "description", "id"), request);
            if(paramatersAreNull != null) return ReturnJson.errorGeral(400, paramatersAreNull);
            if(!Generic.validNumber(pUserId)) return ReturnJson.errorGeral(400, "userid deve ser um numero valido.");
        } catch (Exception e) {
            return ReturnJson.error(500, e, "Erro ao validar parametros.");
        }

        try {
            LogsCollection curLogsCollection;
            String name = pName.toLowerCase();
			String description = pDescription.toLowerCase();
			Long userid = Long.parseLong(pUserId);
			Users user = usersService.id(userid);
			if(user == null) return ReturnJson.errorGeral(400, "user nao encontrado.");

            if(!Generic.stringHasContent(pIdlogCollection)){ // Criando LogsCollection
                curLogsCollection = new LogsCollection(name, description, user);

                logsCollectionService.salvar(curLogsCollection);
                return ReturnJson.success(202, curLogsCollection, "LogsCollection salva com sucesso.");
            }

            Long idlogCollection = Long.parseLong(pIdlogCollection);
            curLogsCollection = new LogsCollection(name, description, user);
            if(logsCollectionService.id(idlogCollection) == null){
                return ReturnJson.errorGeral(400, "Erro ao encontrar 'LogsCollection' para atualizar.");
            }
            curLogsCollection.setIdlogCollection(idlogCollection);
            logsCollectionService.salvar(curLogsCollection);
            return ReturnJson.success(202, curLogsCollection, "LogsCollection atualizada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao atualizar LogsCollection.");
        }
    }

	@PatchMapping("/")
	public ResponseEntity<Map<String, Object>> atualizarLogsCollection(HttpServletRequest request) {
        return this.salvarLogsCollection(request);
    }

	@DeleteMapping("/")
	public ResponseEntity<Map<String, Object>> deletarLogsCollection(HttpServletRequest request) {
            try {
                String pIdlogCollection = request.getParameter("idlogCollection");
                if(!Generic.stringHasContent(pIdlogCollection)) return ReturnJson.errorGeral(400, "Chave primaria 'idlogCollection' não pode ser nula.");
                if(!Generic.validNumber(pIdlogCollection)) return ReturnJson.errorGeral(400, "Chave primaria 'idlogCollection' deve ser um numero valido.");
                Long id = Long.parseLong(pIdlogCollection);

                LogsCollection findLogsCollection = logsCollectionService.id(id);
                if(findLogsCollection == null) return ReturnJson.errorGeral(406, "LogsCollection com id '" + id + "' não existe.");

                logsCollectionService.deletar(id);
                return ReturnJson.success(202, findLogsCollection, "LogsCollection deletada com sucesso.");
            } catch (Exception e) {
                return ReturnJson.error(400, e, "Erro ao deletar LogsCollection.");
            }
        }

	// ----------------------- REQUISICOES  CUSTOMIZAVEIS -----------------------
	@GetMapping("/contar/")
	public ResponseEntity<Map<String, Object>> contarLogsCollection() {
        try {
            Long countLogsCollection = logsCollectionService.contar();

            return ReturnJson.success(202, countLogsCollection, "Sucesso ao buscar quantidade.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao buscar quantidade.");
        }
    }

}
