package com.projeto.max.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.projeto.max.generic.*;
import com.projeto.max.domain.Dairys;
import com.projeto.max.service.DairysService;

@RestController
@RequestMapping("/api/dairys/")
public class DairysRestController {
    @Autowired
    DairysService dairysService;


	// ---------------------------- FUNÇÕES INICIAIS ----------------------------
	@GetMapping("/ping/")
	public ResponseEntity<Map<String, Object>> ping() {return ReturnJson.success(200, null, "Pong Dairys!"); }

	// ---------------------------------- CRUD ----------------------------------
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<Map<String, Object>> encontrarDairys(@PathVariable Long id, HttpServletRequest request) {
        try {
            Dairys findDairys = dairysService.id(id);
            if(findDairys == null) return ReturnJson.errorGeral(406, "Dairys com id '" + id + "' não existe.");
            return ReturnJson.success(202, findDairys, "Dairys encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao procurar Dairys.");
        }
    }

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> buscarDairys() {
        try {
            List<Dairys> findDairys = dairysService.listar();

            return ReturnJson.success(202, findDairys, "Lista de Dairys encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao encontrar lista de Dairys.");
        }
    }

	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> salvarDairys(HttpServletRequest request) {
                        
        String pIddairy = request.getParameter("iddairy");
		String pName = request.getParameter("name");
        
        try {
            String paramatersAreNull = Generic.checkParamatersNull(Arrays.asList("name"), request);
            if(paramatersAreNull != null) return ReturnJson.errorGeral(400, paramatersAreNull);
            
        } catch (Exception e) {
            return ReturnJson.error(500, e, "Erro ao validar parametros.");
        }

        try {
            Dairys curDairys;
            String name = pName.toLowerCase();

            if(!Generic.stringHasContent(pIddairy)){ // Criando Dairys
                curDairys = new Dairys(name);

                dairysService.salvar(curDairys);
                return ReturnJson.success(202, curDairys, "Dairys salva com sucesso.");
            }

            Long iddairy = Long.parseLong(pIddairy);
            curDairys = new Dairys(name);
            if(dairysService.id(iddairy) == null){
                return ReturnJson.errorGeral(400, "Erro ao encontrar 'Dairys' para atualizar.");
            }
            curDairys.setIddairy(iddairy);
            dairysService.salvar(curDairys);
            return ReturnJson.success(202, curDairys, "Dairys atualizada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao atualizar Dairys.");
        }
    }

	@PatchMapping("/")
	public ResponseEntity<Map<String, Object>> atualizarDairys(HttpServletRequest request) {
        return this.salvarDairys(request);
    }

	@DeleteMapping("/")
	public ResponseEntity<Map<String, Object>> deletarDairys(HttpServletRequest request) {
            try {
                String pIddairy = request.getParameter("iddairy");
                if(!Generic.stringHasContent(pIddairy)) return ReturnJson.errorGeral(400, "Chave primaria 'iddairy' não pode ser nula.");
                if(!Generic.validNumber(pIddairy)) return ReturnJson.errorGeral(400, "Chave primaria 'iddairy' deve ser um numero valido.");
                Long id = Long.parseLong(pIddairy);

                Dairys findDairys = dairysService.id(id);
                if(findDairys == null) return ReturnJson.errorGeral(406, "Dairys com id '" + id + "' não existe.");

                dairysService.deletar(id);
                return ReturnJson.success(202, findDairys, "Dairys deletada com sucesso.");
            } catch (Exception e) {
                return ReturnJson.error(400, e, "Erro ao deletar Dairys.");
            }
        }

	// ----------------------- REQUISICOES  CUSTOMIZAVEIS -----------------------
	@GetMapping("/contar/")
	public ResponseEntity<Map<String, Object>> contarDairys() {
        try {
            Long countDairys = dairysService.contar();

            return ReturnJson.success(202, countDairys, "Sucesso ao buscar quantidade.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao buscar quantidade.");
        }
    }

}
