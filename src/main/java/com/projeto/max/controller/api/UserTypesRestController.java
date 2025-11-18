package com.projeto.max.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.projeto.max.generic.*;
import com.projeto.max.domain.UserTypes;
import com.projeto.max.service.UserTypesService;

@RestController
@RequestMapping("/api/usertypes/")
public class UserTypesRestController {
    @Autowired
    UserTypesService userTypesService;


	// ---------------------------- FUNÇÕES INICIAIS ----------------------------
	@GetMapping("/ping/")
	public ResponseEntity<Map<String, Object>> ping() {return ReturnJson.success(200, null, "Pong UserTypes!"); }

	// ---------------------------------- CRUD ----------------------------------
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<Map<String, Object>> encontrarUserTypes(@PathVariable Long id, HttpServletRequest request) {
        try {
            UserTypes findUserTypes = userTypesService.id(id);
            if(findUserTypes == null) return ReturnJson.errorGeral(406, "UserTypes com id '" + id + "' não existe.");
            return ReturnJson.success(202, findUserTypes, "UserTypes encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao procurar UserTypes.");
        }
    }

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> buscarUserTypes() {
        try {
            List<UserTypes> findUserTypes = userTypesService.listar();

            return ReturnJson.success(202, findUserTypes, "Lista de UserTypes encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao encontrar lista de UserTypes.");
        }
    }

	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> salvarUserTypes(HttpServletRequest request) {
                        
        String pIduserTypes = request.getParameter("iduserTypes");
		String pName = request.getParameter("name");
        
        try {
            String paramatersAreNull = Generic.checkParamatersNull(Arrays.asList("name"), request);
            if(paramatersAreNull != null) return ReturnJson.errorGeral(400, paramatersAreNull);
            
        } catch (Exception e) {
            return ReturnJson.error(500, e, "Erro ao validar parametros.");
        }

        try {
            UserTypes curUserTypes;
            String name = pName.toLowerCase();

            if(!Generic.stringHasContent(pIduserTypes)){ // Criando UserTypes
                curUserTypes = new UserTypes(name);

                userTypesService.salvar(curUserTypes);
                return ReturnJson.success(202, curUserTypes, "UserTypes salva com sucesso.");
            }

            Long iduserTypes = Long.parseLong(pIduserTypes);
            curUserTypes = new UserTypes(name);
            if(userTypesService.id(iduserTypes) == null){
                return ReturnJson.errorGeral(400, "Erro ao encontrar 'UserTypes' para atualizar.");
            }
            curUserTypes.setIduserTypes(iduserTypes);
            userTypesService.salvar(curUserTypes);
            return ReturnJson.success(202, curUserTypes, "UserTypes atualizada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao atualizar UserTypes.");
        }
    }

	@PatchMapping("/")
	public ResponseEntity<Map<String, Object>> atualizarUserTypes(HttpServletRequest request) {
        return this.salvarUserTypes(request);
    }

	@DeleteMapping("/")
	public ResponseEntity<Map<String, Object>> deletarUserTypes(HttpServletRequest request) {
            try {
                String pIduserTypes = request.getParameter("iduserTypes");
                if(!Generic.stringHasContent(pIduserTypes)) return ReturnJson.errorGeral(400, "Chave primaria 'iduserTypes' não pode ser nula.");
                if(!Generic.validNumber(pIduserTypes)) return ReturnJson.errorGeral(400, "Chave primaria 'iduserTypes' deve ser um numero valido.");
                Long id = Long.parseLong(pIduserTypes);

                UserTypes findUserTypes = userTypesService.id(id);
                if(findUserTypes == null) return ReturnJson.errorGeral(406, "UserTypes com id '" + id + "' não existe.");

                userTypesService.deletar(id);
                return ReturnJson.success(202, findUserTypes, "UserTypes deletada com sucesso.");
            } catch (Exception e) {
                return ReturnJson.error(400, e, "Erro ao deletar UserTypes.");
            }
        }

	// ----------------------- REQUISICOES  CUSTOMIZAVEIS -----------------------
	@GetMapping("/contar/")
	public ResponseEntity<Map<String, Object>> contarUserTypes() {
        try {
            Long countUserTypes = userTypesService.contar();

            return ReturnJson.success(202, countUserTypes, "Sucesso ao buscar quantidade.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao buscar quantidade.");
        }
    }

}
