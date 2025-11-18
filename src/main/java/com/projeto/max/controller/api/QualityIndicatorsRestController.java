package com.projeto.max.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.projeto.max.generic.*;
import com.projeto.max.domain.QualityIndicators;
import com.projeto.max.service.QualityIndicatorsService;

@RestController
@RequestMapping("/api/qualityindicators/")
public class QualityIndicatorsRestController {
    @Autowired
    QualityIndicatorsService qualityIndicatorsService;


	// ---------------------------- FUNÇÕES INICIAIS ----------------------------
	@GetMapping("/ping/")
	public ResponseEntity<Map<String, Object>> ping() {return ReturnJson.success(200, null, "Pong QualityIndicators!"); }

	// ---------------------------------- CRUD ----------------------------------
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<Map<String, Object>> encontrarQualityIndicators(@PathVariable Long id, HttpServletRequest request) {
        try {
            QualityIndicators findQualityIndicators = qualityIndicatorsService.id(id);
            if(findQualityIndicators == null) return ReturnJson.errorGeral(406, "QualityIndicators com id '" + id + "' não existe.");
            return ReturnJson.success(202, findQualityIndicators, "QualityIndicators encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao procurar QualityIndicators.");
        }
    }

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> buscarQualityIndicators() {
        try {
            List<QualityIndicators> findQualityIndicators = qualityIndicatorsService.listar();

            return ReturnJson.success(202, findQualityIndicators, "Lista de QualityIndicators encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao encontrar lista de QualityIndicators.");
        }
    }

	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> salvarQualityIndicators(HttpServletRequest request) {
                        
        String pIdqualityIndicator = request.getParameter("idqualityIndicator");
		String pCollectionId = request.getParameter("collectionId");
		String pAntibiotic = request.getParameter("antibiotic");
		String pFat = request.getParameter("fat");
		String pApproved = request.getParameter("approved");
		String pQuality = request.getParameter("quality");
        
        try {
            String paramatersAreNull = Generic.checkParamatersNull(Arrays.asList("collectionId", "antibiotic", "fat", "approved", "quality"), request);
            if(paramatersAreNull != null) return ReturnJson.errorGeral(400, paramatersAreNull);
            if(!Generic.validNumber(pCollectionId)) return ReturnJson.errorGeral(400, "collectionId deve ser um numero valido.");
			if(!"false".equals(pApproved.toLowerCase()) && !"true".equals(pApproved.toLowerCase())) return ReturnJson.errorGeral(400, "approved deve ser um numero valido.");
        } catch (Exception e) {
            return ReturnJson.error(500, e, "Erro ao validar parametros.");
        }

        try {
            QualityIndicators curQualityIndicators;
            Long collectionId = Long.parseLong(pCollectionId);
			String antibiotic = pAntibiotic.toLowerCase();
			String fat = pFat.toLowerCase();
			Boolean approved = Boolean.parseBoolean(pApproved);
			String quality = pQuality.toLowerCase();

            if(!Generic.stringHasContent(pIdqualityIndicator)){ // Criando QualityIndicators
                curQualityIndicators = new QualityIndicators(collectionId, antibiotic, fat, approved, quality);

                qualityIndicatorsService.salvar(curQualityIndicators);
                return ReturnJson.success(202, curQualityIndicators, "QualityIndicators salva com sucesso.");
            }

            Long idqualityIndicator = Long.parseLong(pIdqualityIndicator);
            curQualityIndicators = new QualityIndicators(collectionId, antibiotic, fat, approved, quality);
            if(qualityIndicatorsService.id(idqualityIndicator) == null){
                return ReturnJson.errorGeral(400, "Erro ao encontrar 'QualityIndicators' para atualizar.");
            }
            curQualityIndicators.setIdqualityIndicator(idqualityIndicator);
            qualityIndicatorsService.salvar(curQualityIndicators);
            return ReturnJson.success(202, curQualityIndicators, "QualityIndicators atualizada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao atualizar QualityIndicators.");
        }
    }

	@PatchMapping("/")
	public ResponseEntity<Map<String, Object>> atualizarQualityIndicators(HttpServletRequest request) {
        return this.salvarQualityIndicators(request);
    }

	@DeleteMapping("/")
	public ResponseEntity<Map<String, Object>> deletarQualityIndicators(HttpServletRequest request) {
            try {
                String pIdqualityIndicator = request.getParameter("idqualityIndicator");
                if(!Generic.stringHasContent(pIdqualityIndicator)) return ReturnJson.errorGeral(400, "Chave primaria 'idqualityIndicator' não pode ser nula.");
                if(!Generic.validNumber(pIdqualityIndicator)) return ReturnJson.errorGeral(400, "Chave primaria 'idqualityIndicator' deve ser um numero valido.");
                Long id = Long.parseLong(pIdqualityIndicator);

                QualityIndicators findQualityIndicators = qualityIndicatorsService.id(id);
                if(findQualityIndicators == null) return ReturnJson.errorGeral(406, "QualityIndicators com id '" + id + "' não existe.");

                qualityIndicatorsService.deletar(id);
                return ReturnJson.success(202, findQualityIndicators, "QualityIndicators deletada com sucesso.");
            } catch (Exception e) {
                return ReturnJson.error(400, e, "Erro ao deletar QualityIndicators.");
            }
        }

	// ----------------------- REQUISICOES  CUSTOMIZAVEIS -----------------------
	@GetMapping("/contar/")
	public ResponseEntity<Map<String, Object>> contarQualityIndicators() {
        try {
            Long countQualityIndicators = qualityIndicatorsService.contar();

            return ReturnJson.success(202, countQualityIndicators, "Sucesso ao buscar quantidade.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao buscar quantidade.");
        }
    }

}
