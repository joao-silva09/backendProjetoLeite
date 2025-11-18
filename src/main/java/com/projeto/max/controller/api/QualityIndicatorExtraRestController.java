package com.projeto.max.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.projeto.max.generic.*;
import com.projeto.max.domain.QualityIndicatorExtra;
import com.projeto.max.service.QualityIndicatorExtraService;

@RestController
@RequestMapping("/api/qualityindicatorextra/")
public class QualityIndicatorExtraRestController {
    @Autowired
    QualityIndicatorExtraService qualityIndicatorExtraService;


	// ---------------------------- FUNÇÕES INICIAIS ----------------------------
	@GetMapping("/ping/")
	public ResponseEntity<Map<String, Object>> ping() {return ReturnJson.success(200, null, "Pong QualityIndicatorExtra!"); }

	// ---------------------------------- CRUD ----------------------------------
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<Map<String, Object>> encontrarQualityIndicatorExtra(@PathVariable Long id, HttpServletRequest request) {
        try {
            QualityIndicatorExtra findQualityIndicatorExtra = qualityIndicatorExtraService.id(id);
            if(findQualityIndicatorExtra == null) return ReturnJson.errorGeral(406, "QualityIndicatorExtra com id '" + id + "' não existe.");
            return ReturnJson.success(202, findQualityIndicatorExtra, "QualityIndicatorExtra encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao procurar QualityIndicatorExtra.");
        }
    }

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> buscarQualityIndicatorExtra() {
        try {
            List<QualityIndicatorExtra> findQualityIndicatorExtra = qualityIndicatorExtraService.listar();

            return ReturnJson.success(202, findQualityIndicatorExtra, "Lista de QualityIndicatorExtra encontrada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao encontrar lista de QualityIndicatorExtra.");
        }
    }

	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> salvarQualityIndicatorExtra(HttpServletRequest request) {
                        
        String pIdqualityIndicatorExtra = request.getParameter("idqualityIndicatorExtra");
		String pIndicatorId = request.getParameter("indicatorId");
		String pKey = request.getParameter("key");
		String pValue = request.getParameter("value");
        
        try {
            String paramatersAreNull = Generic.checkParamatersNull(Arrays.asList("indicatorId", "key", "value"), request);
            if(paramatersAreNull != null) return ReturnJson.errorGeral(400, paramatersAreNull);
            if(!Generic.validNumber(pIndicatorId)) return ReturnJson.errorGeral(400, "indicatorId deve ser um numero valido.");
        } catch (Exception e) {
            return ReturnJson.error(500, e, "Erro ao validar parametros.");
        }

        try {
            QualityIndicatorExtra curQualityIndicatorExtra;
            Long indicatorId = Long.parseLong(pIndicatorId);
			String key = pKey.toLowerCase();
			String value = pValue.toLowerCase();

            if(!Generic.stringHasContent(pIdqualityIndicatorExtra)){ // Criando QualityIndicatorExtra
                curQualityIndicatorExtra = new QualityIndicatorExtra(indicatorId, key, value);

                qualityIndicatorExtraService.salvar(curQualityIndicatorExtra);
                return ReturnJson.success(202, curQualityIndicatorExtra, "QualityIndicatorExtra salva com sucesso.");
            }

            Long idqualityIndicatorExtra = Long.parseLong(pIdqualityIndicatorExtra);
            curQualityIndicatorExtra = new QualityIndicatorExtra(indicatorId, key, value);
            if(qualityIndicatorExtraService.id(idqualityIndicatorExtra) == null){
                return ReturnJson.errorGeral(400, "Erro ao encontrar 'QualityIndicatorExtra' para atualizar.");
            }
            curQualityIndicatorExtra.setIdqualityIndicatorExtra(idqualityIndicatorExtra);
            qualityIndicatorExtraService.salvar(curQualityIndicatorExtra);
            return ReturnJson.success(202, curQualityIndicatorExtra, "QualityIndicatorExtra atualizada com sucesso.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao atualizar QualityIndicatorExtra.");
        }
    }

	@PatchMapping("/")
	public ResponseEntity<Map<String, Object>> atualizarQualityIndicatorExtra(HttpServletRequest request) {
        return this.salvarQualityIndicatorExtra(request);
    }

	@DeleteMapping("/")
	public ResponseEntity<Map<String, Object>> deletarQualityIndicatorExtra(HttpServletRequest request) {
            try {
                String pIdqualityIndicatorExtra = request.getParameter("idqualityIndicatorExtra");
                if(!Generic.stringHasContent(pIdqualityIndicatorExtra)) return ReturnJson.errorGeral(400, "Chave primaria 'idqualityIndicatorExtra' não pode ser nula.");
                if(!Generic.validNumber(pIdqualityIndicatorExtra)) return ReturnJson.errorGeral(400, "Chave primaria 'idqualityIndicatorExtra' deve ser um numero valido.");
                Long id = Long.parseLong(pIdqualityIndicatorExtra);

                QualityIndicatorExtra findQualityIndicatorExtra = qualityIndicatorExtraService.id(id);
                if(findQualityIndicatorExtra == null) return ReturnJson.errorGeral(406, "QualityIndicatorExtra com id '" + id + "' não existe.");

                qualityIndicatorExtraService.deletar(id);
                return ReturnJson.success(202, findQualityIndicatorExtra, "QualityIndicatorExtra deletada com sucesso.");
            } catch (Exception e) {
                return ReturnJson.error(400, e, "Erro ao deletar QualityIndicatorExtra.");
            }
        }

	// ----------------------- REQUISICOES  CUSTOMIZAVEIS -----------------------
	@GetMapping("/contar/")
	public ResponseEntity<Map<String, Object>> contarQualityIndicatorExtra() {
        try {
            Long countQualityIndicatorExtra = qualityIndicatorExtraService.contar();

            return ReturnJson.success(202, countQualityIndicatorExtra, "Sucesso ao buscar quantidade.");
        } catch (Exception e) {
            return ReturnJson.error(400, e, "Erro ao buscar quantidade.");
        }
    }

}
