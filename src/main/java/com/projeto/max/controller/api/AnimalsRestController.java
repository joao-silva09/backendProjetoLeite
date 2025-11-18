package com.projeto.max.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.projeto.max.generic.*;
import com.projeto.max.domain.Animals;
import com.projeto.max.service.AnimalsService;

@RestController
@RequestMapping("/api/animals/")
public class AnimalsRestController {
    @Autowired
    AnimalsService animalsService;


	// ---------------------------- FUNÇÕES INICIAIS ----------------------------
	@GetMapping("/ping/")
	public ResponseEntity<Map<String, Object>> ping() {return ReturnJson.success(200, null, "Pong Animals!"); }

	// ---------------------------------- CRUD ----------------------------------
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<Map<String, Object>> encontrarAnimals(@PathVariable Long id, HttpServletRequest request) {
        try {
            Animals findAnimals = animalsService.id(id);
            if(findAnimals == null) return ReturnJson.errorGeral(406, "Animals com id '" + id + "' não existe.");
            return ReturnJson.success(202, findAnimals, "Animals encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao procurar Animals.");
        }
    }

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> buscarAnimals() {
        try {
            List<Animals> findAnimals = animalsService.listar();

            return ReturnJson.success(202, findAnimals, "Lista de Animals encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao encontrar lista de Animals.");
        }
    }

	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> salvarAnimals(HttpServletRequest request) {
                        
        String pIdanimal = request.getParameter("idanimal");
		String pName = request.getParameter("name");
        
        try {
            String paramatersAreNull = Generic.checkParamatersNull(Arrays.asList("name"), request);
            if(paramatersAreNull != null) return ReturnJson.errorGeral(400, paramatersAreNull);
            
        } catch (Exception e) {
            return ReturnJson.error(500, e, "Erro ao validar parametros.");
        }

        try {
            Animals curAnimals;
            String name = pName.toLowerCase();

            if(!Generic.stringHasContent(pIdanimal)){ // Criando Animals
                curAnimals = new Animals(name);

                animalsService.salvar(curAnimals);
                return ReturnJson.success(202, curAnimals, "Animals salva com sucesso.");
            }

            Long idanimal = Long.parseLong(pIdanimal);
            curAnimals = new Animals(name);
            if(animalsService.id(idanimal) == null){
                return ReturnJson.errorGeral(400, "Erro ao encontrar 'Animals' para atualizar.");
            }
            curAnimals.setIdanimal(idanimal);
            animalsService.salvar(curAnimals);
            return ReturnJson.success(202, curAnimals, "Animals atualizada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao atualizar Animals.");
        }
    }

	@PatchMapping("/")
	public ResponseEntity<Map<String, Object>> atualizarAnimals(HttpServletRequest request) {
        return this.salvarAnimals(request);
    }

	@DeleteMapping("/")
	public ResponseEntity<Map<String, Object>> deletarAnimals(HttpServletRequest request) {
            try {
                String pIdanimal = request.getParameter("idanimal");
                if(!Generic.stringHasContent(pIdanimal)) return ReturnJson.errorGeral(400, "Chave primaria 'idanimal' não pode ser nula.");
                if(!Generic.validNumber(pIdanimal)) return ReturnJson.errorGeral(400, "Chave primaria 'idanimal' deve ser um numero valido.");
                Long id = Long.parseLong(pIdanimal);

                Animals findAnimals = animalsService.id(id);
                if(findAnimals == null) return ReturnJson.errorGeral(406, "Animals com id '" + id + "' não existe.");

                animalsService.deletar(id);
                return ReturnJson.success(202, findAnimals, "Animals deletada com sucesso.");
            } catch (Exception e) {
                return ReturnJson.error(400, e, "Erro ao deletar Animals.");
            }
        }

	// ----------------------- REQUISICOES  CUSTOMIZAVEIS -----------------------
	@GetMapping("/contar/")
	public ResponseEntity<Map<String, Object>> contarAnimals() {
        try {
            Long countAnimals = animalsService.contar();

            return ReturnJson.success(202, countAnimals, "Sucesso ao buscar quantidade.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao buscar quantidade.");
        }
    }

}
