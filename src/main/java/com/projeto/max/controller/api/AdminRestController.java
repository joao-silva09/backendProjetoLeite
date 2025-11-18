package com.projeto.max.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import com.projeto.max.generic.*;
import com.projeto.max.domain.UserTypes;
import com.projeto.max.domain.Animals;
import com.projeto.max.service.AnimalsService;
import com.projeto.max.service.UserTypesService;

@RestController
@RequestMapping("/api/admin/")
public class AdminRestController {
    @Autowired
    AnimalsService animalsService;
    @Autowired
    UserTypesService userTypesService;

	@PostMapping("/load/")
	public ResponseEntity<Map<String, Object>> load(HttpServletRequest request) {
                        
        try {
            Animals vaca = new Animals("vaca");
            animalsService.salvar(vaca);
            
            Animals cabra = new Animals("cabra");
            animalsService.salvar(cabra);
            
            UserTypes coletor = new UserTypes("coletor");
            userTypesService.salvar(coletor);

            UserTypes produtor = new UserTypes("produtor");
            userTypesService.salvar(produtor);
            
            UserTypes laticionio = new UserTypes("laticionio");
            userTypesService.salvar(laticionio);

            UserTypes admin = new UserTypes("admin");
            userTypesService.salvar(admin);

            return ReturnJson.success(202, null, "Load carregado com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao dar load.");
        }
    }

}
