package com.projeto.max.service;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.Id;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class GenericService<T, ID> {

    protected final JpaRepository<T, ID> repository;

    public GenericService(JpaRepository<T, ID> repository) { this.repository = repository; }

    public List<T> listar() { return repository.findAll(); }

    public Long contar(){ return repository.count(); }

    public T id(ID id) { return repository.findById(id).orElse(null); }
    public T findById(ID id) { return repository.findById(id).orElse(null); }
    
    public void deletar(ID id) { repository.deleteById(id); }

    public T salvar(T entidade) {
        try {
            Object id = null;
            Class<?> clazz = entidade.getClass();
            String nomeMetodo = "";
            boolean existeCampoAtivo = Arrays.stream(clazz.getDeclaredFields()).anyMatch(field -> "ativo".equalsIgnoreCase(field.getName()));

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    nomeMetodo = "get" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
                    id = clazz.getMethod(nomeMetodo).invoke(entidade);
                    break;
                }
            }

            T entidadeBanco = id((ID) id);
            var setDataCadastro = entidade.getClass().getMethod("setDataCadastro", LocalDateTime.class);
            var setDataAtualizacao = entidade.getClass().getMethod("setDataAtualizacao", LocalDateTime.class);

            if(existeCampoAtivo && entidadeBanco != null){
                var setAtivo = entidade.getClass().getMethod("setAtivo", Boolean.class);
                var getAtivo = entidadeBanco.getClass().getMethod("getAtivo");
                setAtivo.invoke(entidade, (Boolean) getAtivo.invoke(entidadeBanco));
            }

            if (entidadeBanco != null) {
                var getDataCadastro = entidadeBanco.getClass().getMethod("getDataCadastro");
                setDataCadastro.invoke(entidade, (LocalDateTime) getDataCadastro.invoke(entidadeBanco));
            } else {
                setDataCadastro.invoke(entidade, LocalDateTime.now());
            }

            setDataAtualizacao.invoke(entidade, LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return repository.save(entidade);
    }

}
