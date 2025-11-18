package com.projeto.max.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import com.projeto.max.generic.*;
import com.projeto.max.domain.UserDairy;
import com.projeto.max.service.UserDairyService;
import com.projeto.max.domain.UserDairyKey;
import com.projeto.max.domain.Dairys;
import com.projeto.max.service.DairysService;
import com.projeto.max.domain.Users;
import com.projeto.max.service.UsersService;

@RestController
@RequestMapping("/api/userdairy/")
public class UserDairyRestController {
    @Autowired
    UserDairyService userDairyService;
	@Autowired
	DairysService dairysService;
	@Autowired
	UsersService usersService;


	// ---------------------------- FUNÇÕES INICIAIS ----------------------------
	@GetMapping("/ping/")
	public ResponseEntity<Map<String, Object>> ping() {return ReturnJson.success(200, null, "Pong UserDairy!"); }

	// ---------------------------------- CRUD ----------------------------------
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<Map<String, Object>> encontrarUserDairy(@PathVariable Long idDairy, @PathVariable Long idUser, HttpServletRequest request) {
        try {
            Dairys firstKey = dairysService.id(idDairy);
            if(firstKey == null){
                return ReturnJson.errorGeral(400, "Dairy n�o encontrado.");
            }

            Users secondKey = usersService.id(idUser);
            if(secondKey == null){
                return ReturnJson.errorGeral(400, "User n�o encontrado.");
            }
            
            UserDairyKey id = new UserDairyKey(firstKey, secondKey);
            UserDairy findUserDairy = userDairyService.id(id);
            if(findUserDairy == null) return ReturnJson.errorGeral(406, "UserDairy com id '" + id + "' não existe.");
            return ReturnJson.success(202, findUserDairy, "UserDairy encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao procurar UserDairy.");
        }
    }

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> buscarUserDairy() {
        try {
            List<UserDairy> findUserDairy = userDairyService.listar();

            return ReturnJson.success(202, findUserDairy, "Lista de UserDairy encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao encontrar lista de UserDairy.");
        }
    }

	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> salvarUserDairy(HttpServletRequest request) {
                        
        String pDairy = request.getParameter("dairy");
		String pUser = request.getParameter("user");

        try {
            UserDairy curUserDairy;
            

            /*if(!Generic.stringHasContent(pDairy) && !Generic.stringHasContent(pUser)){
                curUserDairy = new UserDairy();

                userDairyService.salvar(curUserDairy);
                return ReturnJson.success(202, curUserDairy, "UserDairy salva com sucesso.");
            }*/

            if(!Generic.stringHasContent(pDairy)){
                return ReturnJson.errorGeral(400, "Informe o parametro 'dairy'.");
            }
            Long dairy = Long.parseLong(pDairy);
            Dairys firstKey = dairysService.id(dairy);
            if(firstKey == null){
                return ReturnJson.errorGeral(400, "Dairy n�o encontrado.");
            }

            if(!Generic.stringHasContent(pUser)){
                return ReturnJson.errorGeral(400, "Informe o parametro 'user'.");
            }
            Long user = Long.parseLong(pUser);
            Users secondKey = usersService.id(user);
            if(secondKey == null){
                return ReturnJson.errorGeral(400, "User n�o encontrado.");
            }

            curUserDairy = new UserDairy();
            Boolean exists = false;
            if(userDairyService.id(new UserDairyKey(firstKey, secondKey)) == null){
                exists = true;
            }
            curUserDairy.setUserDairyKey(new UserDairyKey(firstKey, secondKey));
            userDairyService.salvar(curUserDairy);
            if(exists){
                return ReturnJson.success(202, curUserDairy, "UserDairy salva com sucesso.");
            }
            return ReturnJson.success(202, curUserDairy, "UserDairy atualizada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao atualizar UserDairy.");
        }
    }

	@PatchMapping("/")
	public ResponseEntity<Map<String, Object>> atualizarUserDairy(HttpServletRequest request) {
        return this.salvarUserDairy(request);
    }

	@DeleteMapping("/")
	public ResponseEntity<Map<String, Object>> deletarUserDairy(HttpServletRequest request) {
            try {
                String pDairys = request.getParameter("dairys");
                if(!Generic.stringHasContent(pDairys)) return ReturnJson.errorGeral(400, "Chave primaria 'dairys' não pode ser nula.");
                if(!Generic.validNumber(pDairys)) return ReturnJson.errorGeral(400, "Chave primaria 'dairys' deve ser um numero valido.");

                String pUsers = request.getParameter("users");
                if(!Generic.stringHasContent(pUsers)) return ReturnJson.errorGeral(400, "Chave primaria 'users' não pode ser nula.");
                if(!Generic.validNumber(pUsers)) return ReturnJson.errorGeral(400, "Chave primaria 'users' deve ser um numero valido.");

                Dairys firstKey = dairysService.id(Long.parseLong(pDairys));
                Users secondKey = usersService.id(Long.parseLong(pUsers));
                UserDairyKey id = new UserDairyKey(firstKey, secondKey);

                UserDairy findUserDairy = userDairyService.id(id);
                if(findUserDairy == null) return ReturnJson.errorGeral(406, "UserDairy com id '" + id + "' não existe.");

                userDairyService.deletar(id);
                return ReturnJson.success(202, findUserDairy, "UserDairy deletada com sucesso.");
            } catch (Exception e) {
                return ReturnJson.error(400, e, "Erro ao deletar UserDairy.");
            }
        }

	// ----------------------- REQUISICOES  CUSTOMIZAVEIS -----------------------
	@GetMapping("/contar/")
	public ResponseEntity<Map<String, Object>> contarUserDairy() {
        try {
            Long countUserDairy = userDairyService.contar();

            return ReturnJson.success(202, countUserDairy, "Sucesso ao buscar quantidade.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao buscar quantidade.");
        }
    }

}
