package com.projeto.max.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.projeto.max.generic.*;
import com.projeto.max.domain.Users;
import com.projeto.max.service.UsersService;
import com.projeto.max.domain.UserTypes;
import com.projeto.max.service.UserTypesService;

@RestController
@RequestMapping("/api/users/")
public class UsersRestController {
    @Autowired
    UsersService usersService;
	@Autowired
	UserTypesService userTypesService;


	// ---------------------------- FUNÇÕES INICIAIS ----------------------------
	@GetMapping("/ping/")
	public ResponseEntity<Map<String, Object>> ping() {return ReturnJson.success(200, null, "Pong Users!"); }

	// ---------------------------------- CRUD ----------------------------------
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<Map<String, Object>> encontrarUsers(@PathVariable Long id, HttpServletRequest request) {
        try {
            Users findUsers = usersService.id(id);
            if(findUsers == null) return ReturnJson.errorGeral(406, "Users com id '" + id + "' não existe.");
            return ReturnJson.success(202, findUsers, "Users encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao procurar Users.");
        }
    }

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> buscarUsers() {
        try {
            List<Users> findUsers = usersService.listar();

            return ReturnJson.success(202, findUsers, "Lista de Users encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao encontrar lista de Users.");
        }
    }

	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> salvarUsers(HttpServletRequest request) {
                        
        String pIduser = request.getParameter("iduser");
		String pName = request.getParameter("name");
		String pDocument = request.getParameter("document");
		String pEmail = request.getParameter("email");
		String pPasswordHash = request.getParameter("passwordHash");
		String pType = request.getParameter("type");
		String pActive = "true";
        
        try {
            String paramatersAreNull = Generic.checkParamatersNull(Arrays.asList("name", "document", "email", "passwordHash", "type"), request);
            if(paramatersAreNull != null) return ReturnJson.errorGeral(400, paramatersAreNull);
            pDocument = Generic.cleanDocumento(pDocument);
            if(!Generic.validCpf(pDocument) && !Generic.validCnpj(pDocument)) return ReturnJson.errorGeral(400, "document deve ser um documento valido.");
            if(!Generic.validNumber(pType)) return ReturnJson.errorGeral(400, "type deve ser um numero valido.");
			if(!"false".equals(pActive.toLowerCase()) && !"true".equals(pActive.toLowerCase())) return ReturnJson.errorGeral(400, "active deve ser um numero valido.");
        } catch (Exception e) {
            return ReturnJson.error(500, e, "Erro ao validar parametros.");
        }

        try {
            Users curUsers;
            String name = pName.toLowerCase();
			String document = pDocument.toLowerCase();
			String email = pEmail.toLowerCase();
			String passwordHash = pPasswordHash.toLowerCase();
			Long type = Long.parseLong(pType);
			UserTypes user = userTypesService.id(type);
			if(user == null) return ReturnJson.errorGeral(400, "user nao encontrado.");
			Boolean active = Boolean.parseBoolean(pActive);

            if(!Generic.stringHasContent(pIduser)){ // Criando Users
                curUsers = new Users(name, document, email, passwordHash, user, active);

                usersService.salvar(curUsers);
                return ReturnJson.success(202, curUsers, "Users salva com sucesso.");
            }

            Long iduser = Long.parseLong(pIduser);
            curUsers = new Users(name, document, email, passwordHash, user, active);
            if(usersService.id(iduser) == null){
                return ReturnJson.errorGeral(400, "Erro ao encontrar 'Users' para atualizar.");
            }
            curUsers.setIduser(iduser);
            usersService.salvar(curUsers);
            return ReturnJson.success(202, curUsers, "Users atualizada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao atualizar Users.");
        }
    }

	@PatchMapping("/")
	public ResponseEntity<Map<String, Object>> atualizarUsers(HttpServletRequest request) {
        return this.salvarUsers(request);
    }

	@DeleteMapping("/")
	public ResponseEntity<Map<String, Object>> deletarUsers(HttpServletRequest request) {
            try {
                String pIduser = request.getParameter("iduser");
                if(!Generic.stringHasContent(pIduser)) return ReturnJson.errorGeral(400, "Chave primaria 'iduser' não pode ser nula.");
                if(!Generic.validNumber(pIduser)) return ReturnJson.errorGeral(400, "Chave primaria 'iduser' deve ser um numero valido.");
                Long id = Long.parseLong(pIduser);

                Users findUsers = usersService.id(id);
                if(findUsers == null) return ReturnJson.errorGeral(406, "Users com id '" + id + "' não existe.");

                usersService.deletar(id);
                return ReturnJson.success(202, findUsers, "Users deletada com sucesso.");
            } catch (Exception e) {
                return ReturnJson.error(400, e, "Erro ao deletar Users.");
            }
        }

	// ----------------------- REQUISICOES  CUSTOMIZAVEIS -----------------------
    @GetMapping("/ativar/{id:\\d+}")
	public ResponseEntity<Map<String, Object>> ativarUser(@PathVariable Long id, HttpServletRequest request) {
        try {
            Users findUsers = usersService.id(id);
            if(findUsers == null) return ReturnJson.errorGeral(406, "Users com id '" + id + "' não existe.");
            findUsers.setActive(true);
            return ReturnJson.success(202, findUsers, "User ativo com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao ativar User.");
        }
    }
    @GetMapping("/desativar/{id:\\d+}")
	public ResponseEntity<Map<String, Object>> desativarUser(@PathVariable Long id, HttpServletRequest request) {
        try {
            Users findUsers = usersService.id(id);
            if(findUsers == null) return ReturnJson.errorGeral(406, "Users com id '" + id + "' não existe.");
            findUsers.setActive(false);
            return ReturnJson.success(202, findUsers, "User desativar com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao desativar User.");
        }
    }

	@GetMapping("/contar/")
	public ResponseEntity<Map<String, Object>> contarUsers() {
        try {
            Long countUsers = usersService.contar();

            return ReturnJson.success(202, countUsers, "Sucesso ao buscar quantidade.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao buscar quantidade.");
        }
    }

}
