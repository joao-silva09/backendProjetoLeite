package com.projeto.max.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.projeto.max.generic.*;
import com.projeto.max.domain.Collections;
import com.projeto.max.domain.UserTypes;
import com.projeto.max.domain.Users;
import com.projeto.max.service.UsersService;
import com.projeto.max.service.CollectionsService;

@RestController
@RequestMapping("/api/collectors/")
public class CollectorsRestController extends UsersRestController {
    @Autowired
    UsersService usersService;
	@Autowired
    CollectionsService collectionsService;


    @GetMapping("/{id}/collections/history/")
	public ResponseEntity<Map<String, Object>> collections(@PathVariable Long id, HttpServletRequest request) {
        try {
            Users collector = usersService.id(id);
			if(collector == null) return ReturnJson.errorGeral(400, "collector nao encontrado.");

            UserTypes typeProducer = collector.getUser();
			if(typeProducer.getIduserTypes() != Generic.coletor) return ReturnJson.errorGeral(400, "collector deve ter cargo de produtor.");
        } catch (Exception e) {
            return ReturnJson.error(500, e, "Erro ao validar parametros.");
        }

        try {
            List<Collections> collections = collectionsService.encontrarPorCollector(collector);
            List<Collections> filtredCollections;

            for (int i = 0; i < collections.size(); i++) {
                Collections col = collections.get(i);
                filtredCollections.add(col);
            }

            return ReturnJson.success(202, filtredCollections, "Lista de Collections encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(500, e, "Erro ao validar parametros.");
        }
    }

	@GetMapping("/stats/")
	public ResponseEntity<Map<String, Object>> stats(HttpServletRequest request) {
        String pProducerId = request.getParameter("producerid");
        try {
            String paramatersAreNull = Generic.checkParamatersNull(Arrays.asList("producerid"), request);
            if(paramatersAreNull != null) return ReturnJson.errorGeral(400, paramatersAreNull);
			if(!Generic.validNumber(pProducerId)) return ReturnJson.errorGeral(400, "producerid deve ser um numero valido.");
        } catch (Exception e) {
            return ReturnJson.error(500, e, "Erro ao validar parametros.");
        }
        
        try {
            Long producerid = Long.parseLong(pProducerId);
			Users producer = usersService.id(producerid);
			if(producer == null) return ReturnJson.errorGeral(400, "producer nao encontrado.");
            
            UserTypes typeProducer = producer.getUser();
			if(typeProducer.getIduserTypes() != Generic.produtor) return ReturnJson.errorGeral(400, "producer deve ter cargo de produtor.");

            LocalDateTime startDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();
            LocalDateTime endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);

            List<Collections> findCollections = collectionsService.produtorEntre(producer, startDate, endDate);            
            
            Map<String, Object> jsonData = new HashMap<>();
            jsonData.put("collectionsToday", findCollections.size());
            jsonData.put("lastCollection", findCollections.get(findCollections.size()-1));

            return ReturnJson.success(202, jsonData, "Stats de collector encontrados com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao encontrar stats de collector.");
        }
    }
}
