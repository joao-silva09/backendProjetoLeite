package com.projeto.max.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.projeto.max.generic.*;
import com.projeto.max.domain.UserTypes;
import com.projeto.max.domain.Users;
import com.projeto.max.service.UsersService;
import com.projeto.max.domain.Collections;
import com.projeto.max.service.CollectionsService;
import com.projeto.max.domain.Farms;
import com.projeto.max.service.FarmsService;

@RestController
@RequestMapping("/api/collectors/")
public class CollectorsRestController extends UsersRestController {
    @Autowired
    UsersService usersService;
	@Autowired
    FarmsService farmsService;
	@Autowired
    CollectionsService collectionsService;


    @GetMapping("/{id}/collections/history/")
	public ResponseEntity<Map<String, Object>> collections(@PathVariable Long id, HttpServletRequest request) {
        String pFarmid = request.getParameter("farmid");
        String pPage = request.getParameter("page");
        String pLimit = request.getParameter("limit");
        String pStartDate = request.getParameter("startDate");
        String pEndDate = request.getParameter("endDate");

        Users collector = null;
        Farms farm = null;
        Integer page = 1;
        Integer limit = 0;
        try {
            collector = usersService.id(id);
			if(collector == null) return ReturnJson.errorGeral(400, "collector nao encontrado.");

            UserTypes typeProducer = collector.getUser();
			if(typeProducer.getIduserTypes() != Generic.coletor) return ReturnJson.errorGeral(400, "collector deve ter cargo de produtor.");

            if(pPage != null) {
                if(!Generic.validNumber(pPage)) return ReturnJson.errorGeral(400, "page deve ser um numero valido.");
                page = Integer.parseInt(pPage);
            }
            if(pLimit != null){
                limit = Integer.parseInt(pLimit);
                if(!Generic.validNumber(pLimit)) return ReturnJson.errorGeral(400, "limit deve ser um numero valido.");
            }
            if(pFarmid != null) {
                if(!Generic.validNumber(pFarmid)) return ReturnJson.errorGeral(400, "farmid deve ser um numero valido.");
                farm = farmsService.id(Long.parseLong(pFarmid));
			    if(farm == null) return ReturnJson.errorGeral(400, "farm não encontrada.");
            }
            if(pStartDate != null) {
                if(!Generic.validLocalDate(pStartDate)) return ReturnJson.errorGeral(400, "startDate deve ter uma data valida.");
            }
            if(pEndDate != null) {
                if(!Generic.validLocalDate(pEndDate)) return ReturnJson.errorGeral(400, "endDate deve ter uma data valida.");
            }
        } catch (Exception e) {
            return ReturnJson.error(500, e, "Erro ao validar parametros.");
        }

        try {
            List<Collections> collections = collectionsService.encontrarPorCollector(collector);
            List<Collections> filtredCollections = new ArrayList<>();
            List<Collections> finalCollections = new ArrayList<>();

            for (int i = 0; i < collections.size(); i++) {
                Collections col = collections.get(i);
                Boolean filtred = false;

                if(pFarmid != null){
                    if(col.getFarm().getIdfarm() != farm.getIdfarm()) filtred = true;
                }
                if(pStartDate != null){
                    LocalDateTime startDate = Generic.formatLocalDate(pStartDate).atStartOfDay();
                    if(col.getCollectionDate().isBefore(startDate)) filtred = true;
                }
                if(pEndDate != null){
                    LocalDateTime endDate = Generic.formatLocalDate(pEndDate).atTime(LocalTime.MAX);
                    if(col.getCollectionDate().isAfter(endDate)) filtred = true;
                }

                if(!filtred) filtredCollections.add(col);
            }

            if(pLimit != null){
                Integer curPage = 1;
                Integer index = 0;
                for (int i = 0; i < filtredCollections.size(); i++) {
                    index++;
                    Collections col = filtredCollections.get(i);
                    if(page == curPage){
                        finalCollections.add(col);
                    }

                    if(index == limit) {
                        index = 0;
                        curPage++;
                    }
                }
            } else {
                finalCollections = filtredCollections;
            }

            return ReturnJson.success(202, finalCollections, "Historico de collector encontrado com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(500, e, "Erro ao encontrar historico de collector.");
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
