package com.projeto.max.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.projeto.max.generic.*;
import com.projeto.max.domain.Users;
import com.projeto.max.service.UsersService;
import com.projeto.max.domain.Collections;
import com.projeto.max.domain.UserTypes;
import com.projeto.max.service.CollectionsService;

@RestController
@RequestMapping("/api/producers/")
public class ProducerRestController extends UsersRestController {
    @Autowired
    UsersService usersService;
    @Autowired
    CollectionsService collectionsService;

    @GetMapping("/{id}/collections/")
	public ResponseEntity<Map<String, Object>> collections(@PathVariable Long id, HttpServletRequest request) {
        try {
            Users producer = usersService.id(id);
			if(producer == null) return ReturnJson.errorGeral(400, "producer nao encontrado.");

            UserTypes typeProducer = producer.getUser();
			if(typeProducer.getIduserTypes() != Generic.produtor) return ReturnJson.errorGeral(400, "producer deve ter cargo de produtor.");

            List<Collections> collections = collectionsService.encontrarPorProducer(producer);
            return ReturnJson.success(202, collections, "Lista de Collections encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao encontrar Collections de producer.");
        }
    }

	@GetMapping("/stats/")
	public ResponseEntity<Map<String, Object>> stats(HttpServletRequest request) {
        String pProducerId = request.getParameter("producerid");
        String pYear = request.getParameter("year");
        String pMonth = request.getParameter("month");
        try {
            String paramatersAreNull = Generic.checkParamatersNull(Arrays.asList("producerid"), request);
            if(paramatersAreNull != null) return ReturnJson.errorGeral(400, paramatersAreNull);
			if(!Generic.validNumber(pProducerId)) return ReturnJson.errorGeral(400, "producerid deve ser um numero valido.");
            
            if(pYear != null){
                if(!Generic.validInt(pYear)) return ReturnJson.errorGeral(400, "year deve ser um numero valido.");
            }
            if(pMonth != null){
                if(!Generic.validInt(pMonth)) return ReturnJson.errorGeral(400, "month deve ser um numero valido.");
                if(Long.parseLong(pMonth) < 0 || Long.parseLong(pMonth) > 12){
                    if(!Generic.validNumber(pMonth)) return ReturnJson.errorGeral(400, "month invalido.");
                }
            }
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

            if(pYear != null){
                Integer year = Integer.parseInt(pYear);
                startDate = startDate.withYear(year).withDayOfMonth(1);
                endDate = endDate.withYear(year).withDayOfMonth(endDate.withYear(year).toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
            }
            if(pMonth != null){
                Integer month = Integer.parseInt(pMonth);
                startDate = startDate.withMonth(month).withDayOfMonth(1);
                endDate = endDate.withMonth(month).withDayOfMonth(endDate.withMonth(month).toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
            }

            List<Collections> findCollections = collectionsService.produtorEntre(producer, startDate, endDate);

            Float totalLiters = 0.0f;
            Float averageDaily = 0.0f;
            Float averageAcidity = 0.0f;
            Float averageTemperature = 0.0f;
            Integer collectionsCount = findCollections.size();
            List<Map<String, Object>> last30Days = new ArrayList<>();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            for (Collections c : findCollections) {
                totalLiters += c.getQuantity();
                averageDaily += c.getQuantity();
                averageAcidity += c.getAcidity();
                averageTemperature += c.getTemperature();

                Map<String, Object> obj = new HashMap<>();
                obj.put("date", c.getCollectionDate().format(formatter));
                obj.put("quantity", c.getQuantity());
                last30Days.add(obj);
            }
            if(averageDaily != 0.0) averageDaily /= collectionsCount;
            if(averageAcidity != 0.0) averageAcidity /= collectionsCount;
            if(averageTemperature != 0.0) averageTemperature /= collectionsCount;


            Map<String, Object> currentMonth = new HashMap<>();
            currentMonth.put("totalLiters", totalLiters);
            currentMonth.put("averageDaily", averageDaily);
            currentMonth.put("averageAcidity", averageAcidity);
            currentMonth.put("averageTemperature", averageTemperature);
            
            Map<String, Object> jsonData = new HashMap<>();
            jsonData.put("currentMonth", currentMonth);
            jsonData.put("last30Days", last30Days);
            // jsonData.put("last30DaysRaw", findCollections);

            return ReturnJson.success(202, jsonData, "Stats de producer encontrados com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao encontrar stats de producer.");
        }
    }
}
