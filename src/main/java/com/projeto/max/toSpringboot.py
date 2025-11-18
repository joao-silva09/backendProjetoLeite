import json
import re
import os

package = "com.projeto.max"
file_path = "tabelas.json"

domain_path = "domain/"
repository_path = "repository/"
dto_path = "dto/"
service_path = "service/"
controller_path = "controller/api/"
ext = ".java"

allowDoublePks = True

systemColumns = ["DATA_CADASTRO", "DATA_ATUALIZACAO"]

def to_camel_case(name: str) -> str:
    """Converte snake_case ou minúsculas para camelCase"""
    parts = re.split(r'[\s_-]+', name)
    return parts[0].lower() + ''.join(p.capitalize() for p in parts[1:])

def primeira_maiuscula(texto: str) -> str:
    if not texto:
        return texto
    if "_" in texto or texto.upper() == texto:
        texto = to_camel_case(texto)
    return texto[0].upper() + texto[1:]
def to_pascal_case(name: str) -> str:
    """Converte nome de tabela em PascalCase"""
    parts = re.split(r'[_\s]+', to_camel_case(name))
    return ''.join(p.capitalize() for p in parts)

def returnPks(table):
    pks = []
    for c in table["columns"]:
        if c["pk"]:
            pks.append(c)
    return pks
def isMultiPk(col, pks):
    for p in pks:
        if col["name"] == p["name"]:
            return True
    return False

def fkCampoName(col):
    return to_camel_case(col["name"].split('_')[0])
def fkRealCampoName(col):
    a = col["name"].split('_')
    if len(a) <= 1:
        return to_camel_case(a[0])
    return to_camel_case(a[1])

def startProcess(dados):
    for i in dados:
        i["columns"].append({"name": "DATA_CADASTRO", "type": "LocalDateTime", "rawType": "DATETIME", "pk": False, "fk": None, "notNull": True})
        i["columns"].append({"name": "DATA_ATUALIZACAO", "type": "LocalDateTime", "rawType": "DATETIME", "pk": False, "fk": None, "notNull": True})

    toDomain(dados)
    toRepository(dados)
    toService(dados)
    toController(dados)

def addImport(imports:list, i:str):
    if i not in imports:
        imports.append(i)
    return imports
def addService(services:list, s:str):
    if s not in services:
        services.append(s)
    return services

def createFile(path, conteudo):
    try:
        with open(path, "w", encoding="cp1252") as file:
            file.write(conteudo)
    except FileNotFoundError:
        print(f"Error: The file '{path}' was not found.")
    except Exception as e:
        print(f"An error occurred: {e}")

# --------------------------------------- TO CONTROLLER
def toService(dados):
    for item in dados:
        className = to_camel_case(item["name"])
        repName = f"{className}Service"

        imports = ["org.springframework.stereotype.Service", f"{package}.domain.{primeira_maiuscula(className)}", f"{package}.repository.{primeira_maiuscula(className)}Repository", "java.util.List"]
        importsTxt = []

        findByLists = []
        campoPkType = ""

        doublePks = returnPks(item)

        for col in item["columns"]:
            if col["pk"] is True:
                campoPkType = col["type"]

        createdMultiPk = False
        for col in item["columns"]:
            if col["pk"] is True and col["name"] == 'ID':
                continue
            if "DATE" in col["rawType"] or "TIMESTAMP" == col["rawType"]:
                addImport(imports, f"java.time.{col["type"]}")

            property = []
            if col["fk"] is None:    
                if col["type"].lower() == "string":       
                    findByLists.append(f"\tList<{primeira_maiuscula(className)}> encontrarPor{primeira_maiuscula(col["name"])}Contendo({col["type"]} {to_camel_case(col["name"])}) {{ return this.{className}Repository.findBy{primeira_maiuscula(col["name"])}Containing({to_camel_case(col["name"])}); }};")
                findByLists.append(f"\tList<{primeira_maiuscula(className)}> encontrarPor{primeira_maiuscula(col["name"])}({col["type"]} {to_camel_case(col["name"])}) {{ return this.{className}Repository.findBy{primeira_maiuscula(col["name"])}({to_camel_case(col["name"])}); }};")
            else:
                if(len(doublePks) > 1 and isMultiPk(col, doublePks)):
                    if not createdMultiPk:
                        createdMultiPk = True
                        campoPkType = primeira_maiuscula(className) + "Key"
                        addImport(imports, f"{package}.domain.{primeira_maiuscula(className)}Key")
                        findByLists.append(f"\tList<{primeira_maiuscula(className)}> encontrarPor{primeira_maiuscula(className)}Key({primeira_maiuscula(className)}Key {className}Key) {{ return this.{className}Repository.findBy{primeira_maiuscula(className)}Key({className}Key); }};")
        
        for i in imports:
            importsTxt.append(f"import {i};")
        conteudo = f"""package {package}.service;

{"\n".join(importsTxt)}

@Service
public class {primeira_maiuscula(repName)} extends GenericService<{primeira_maiuscula(className)}, {campoPkType}> {{

    private final {primeira_maiuscula(className)}Repository {className}Repository;
    public {primeira_maiuscula(className)}Service({primeira_maiuscula(className)}Repository {className}Repository) {{
        super({className}Repository);
        this.{className}Repository = {className}Repository;
    }}

{"\n".join(findByLists)}

}}
"""
        createFile(f"{service_path}{primeira_maiuscula(repName)}{ext}", conteudo)

# --------------------------------------- TO CONTROLLER
def toController(dados):
    for item in dados:
        className = primeira_maiuscula(item["name"])
        repName = f"{className}RestController"
        serviceName = f"{to_camel_case(item["name"])}Service"
        mappings = []

        imports = ["org.springframework.web.bind.annotation.*", "org.springframework.beans.factory.annotation.Autowired", "org.springframework.http.ResponseEntity", "jakarta.servlet.http.HttpServletRequest", "java.util.Arrays", "java.util.List", "java.util.Map", f"{package}.generic.*"]
        addImport(imports, f"{package}.domain.{className}")
        addImport(imports, f"{package}.service.{primeira_maiuscula(serviceName)}")
        importsTxt = []

        colPk = ""

        doublePks = returnPks(item)

        for col in item["columns"]:
            if col["pk"] is True:
                colPk = col["name"]
                break

        if len(doublePks) > 1:
            addImport(imports, f"{package}.domain.{className}Key")
            addImport(imports, f"{package}.domain.{primeira_maiuscula(doublePks[0]["fk"]["refTableName"])}")
            addImport(imports, f"{package}.service.{primeira_maiuscula(doublePks[0]["fk"]["refTableName"])}Service")
            addImport(imports, f"{package}.domain.{primeira_maiuscula(doublePks[1]["fk"]["refTableName"])}")
            addImport(imports, f"{package}.service.{primeira_maiuscula(doublePks[1]["fk"]["refTableName"])}Service")

        mappings.append('\n\t// ---------------------------- FUNÇÕES INICIAIS ----------------------------')
        mappings.append('\t@GetMapping("/ping/")')
        mappings.append(f'\tpublic ResponseEntity<Map<String, Object>> ping() {{return ReturnJson.success(200, null, "Pong {primeira_maiuscula(className)}!"); }}')

        mappings.append('\n\t// ---------------------------------- CRUD ----------------------------------')
        mappings.append('\t@GetMapping("/{id:\\\\d+}")')
        mappings.append(f'''\tpublic ResponseEntity<Map<String, Object>> deletar{className}(@PathVariable Long id, HttpServletRequest request) {{
        try {{
            {className} find{className} = {serviceName}.id(id);
            if(find{className} == null) return ReturnJson.errorGeral(406, "{className} com id '" + id + "' não existe.");
            return ReturnJson.success(202, find{className}, "{className} encontrada com sucesso.");
        }} catch (Exception e) {{
            return ReturnJson.error(400, e, "Erro ao procurar {className}.");
        }}
    }}''')
        mappings.append('')

        mappings.append('\t@GetMapping("/")')
        mappings.append(f'''\tpublic ResponseEntity<Map<String, Object>> buscar{className}() {{
        try {{
            List<{className}> find{className} = {serviceName}.listar();

            return ReturnJson.success(202, find{className}, "Lista de {className} encontrada com sucesso.");
        }} catch (Exception e) {{
            return ReturnJson.error(400, e, "Erro ao encontrar lista de {className}.");
        }}
    }}''')
        
        rawParameters = []
        finalRawParameters = []
        parameters = []
        parametersChecks = []
        validParameters = []
        afterParameters = []
        services = []
        services.append(f'{primeira_maiuscula(serviceName)} {serviceName};')

        if len(doublePks) > 1:
            addService(services, f'{primeira_maiuscula(doublePks[0]["fk"]["refTableName"])}Service {to_camel_case(doublePks[0]["fk"]["refTableName"])}Service;')
            addService(services, f'{primeira_maiuscula(doublePks[1]["fk"]["refTableName"])}Service {to_camel_case(doublePks[1]["fk"]["refTableName"])}Service;')

        createdMultiPk = False
        for col in item["columns"]:
            if(col["name"] in systemColumns):
                continue
            curType = col["type"]
            curName = col["name"]
            if col["fk"] is not None:
                curType = col["fk"]["campo"]["type"]
                curName = fkRealCampoName(col)
                if curName == 'id':
                    curName = to_camel_case(col["name"])

            if len(doublePks) > 1 and isMultiPk(col, doublePks):
                curName = fkCampoName(col)

            parameters.append(f'String p{primeira_maiuscula(curName)} = request.getParameter("{to_camel_case(curName)}");')
            if curType == "Long" or curType == "Double" or curType == "Float":
                if col["pk"] == False:
                    parametersChecks.append(f'if(!Generic.validNumber(p{primeira_maiuscula(curName)})) return ReturnJson.errorGeral(400, "{to_camel_case(curName)} deve ser um numero valido.");')
                    validParameters.append(f'{curType} {to_camel_case(curName)} = {curType}.parse{curType}(p{primeira_maiuscula(curName)});')
                else:
                    afterParameters.append(f'{curType} {to_camel_case(curName)} = {curType}.parse{curType}(p{primeira_maiuscula(curName)});')
            elif "DATE" in col["rawType"] or "TIMESTAMP" == col["rawType"]:
                addImport(imports, f"java.time.{col["type"]}")
                parametersChecks.append(f'if(!Generic.valid{curType}(p{primeira_maiuscula(curName)})) return ReturnJson.errorGeral(400, "{to_camel_case(curName)} deve ter uma data valida.");')
                validParameters.append(f'{curType} {to_camel_case(curName)} = Generic.format{curType}(p{primeira_maiuscula(curName)});')
            elif curType.lower() == "string":
                validParameters.append(f'{curType} {to_camel_case(curName)} = p{primeira_maiuscula(curName)}.toLowerCase();')
            elif curType.lower() == "boolean":
                parametersChecks.append(f'if(!"false".equals(p{primeira_maiuscula(curName)}.toLowerCase()) && !"true".equals(p{primeira_maiuscula(curName)}.toLowerCase())) return ReturnJson.errorGeral(400, "{to_camel_case(curName)} deve ser um numero valido.");')
                # "false".equals(lowerCaseS)
                # "true".equals(lowerCaseS)
                validParameters.append(f'{curType} {to_camel_case(curName)} = Boolean.parseBoolean(p{primeira_maiuscula(curName)});')
            
            if col["pk"] == True:
                continue
            
            if col["fk"] is not None:
                rawParameters.append(to_camel_case(fkRealCampoName(col)))
                finalRawParameters.append(to_camel_case(fkCampoName(col)))
                addImport(imports, f"{package}.domain.{primeira_maiuscula(col["fk"]["refTableName"])}")
                addImport(imports, f"{package}.service.{primeira_maiuscula(col["fk"]["refTableName"])}Service")
                addService(services, f'{primeira_maiuscula(col["fk"]["refTableName"])}Service {to_camel_case(col["fk"]["refTableName"])}Service;')

                validParameters.append(f'{primeira_maiuscula(col["fk"]["refTableName"])} {fkCampoName(col)} = {to_camel_case(col["fk"]["refTableName"])}Service.id({to_camel_case(curName)});')
                validParameters.append(f'if({fkCampoName(col)} == null) return ReturnJson.errorGeral(400, "{fkCampoName(col)} nao encontrado.");')
            else:
                rawParameters.append(to_camel_case(curName))
                finalRawParameters.append(to_camel_case(curName))

        mappings.append('')
        mappings.append('\t@PostMapping("/")')
        if len(doublePks) > 1:
            mappings.append(f'''\tpublic ResponseEntity<Map<String, Object>> salvar{className}(HttpServletRequest request) {{
                        
        {"\n\t\t".join(parameters)}

        try {{
            String paramatersAreNull = Generic.checkParamatersNull(Arrays.asList("{'", "'.join(rawParameters)}"), request);
            if(paramatersAreNull != null) return ReturnJson.errorGeral(400, paramatersAreNull);
            {"\n\t\t\t".join(parametersChecks)}
        }} catch (Exception e) {{
            return ReturnJson.error(500, e, "Erro ao validar parametros.");
        }}

        try {{
            {className} cur{className};
            {"\n\t\t\t".join(validParameters)}

            /*if(!Generic.stringHasContent(p{primeira_maiuscula(fkCampoName(doublePks[0]))}) && !Generic.stringHasContent(p{primeira_maiuscula(fkCampoName(doublePks[1]))})){{
                cur{className} = new {className}({", ".join(finalRawParameters)});

                {serviceName}.salvar(cur{className});
                return ReturnJson.success(202, cur{className}, "{className} salva com sucesso.");
            }}*/

            if(!Generic.stringHasContent(p{primeira_maiuscula(fkCampoName(doublePks[0]))})){{
                return ReturnJson.errorGeral(400, "Informe o parametro '{to_camel_case(fkCampoName(doublePks[0]))}'.");
            }}
            Long {to_camel_case(fkCampoName(doublePks[0]))} = Long.parseLong(p{primeira_maiuscula(fkCampoName(doublePks[0]))});
            {primeira_maiuscula(doublePks[0]["fk"]["refTableName"])} firstKey = {to_camel_case(doublePks[0]["fk"]["refTableName"])}Service.id({to_camel_case(fkCampoName(doublePks[0]))});
            if(firstKey == null){{
                return ReturnJson.errorGeral(400, "{primeira_maiuscula(fkCampoName(doublePks[0]))} não encontrado.");
            }}

            if(!Generic.stringHasContent(p{primeira_maiuscula(fkCampoName(doublePks[1]))})){{
                return ReturnJson.errorGeral(400, "Informe o parametro '{to_camel_case(fkCampoName(doublePks[1]))}'.");
            }}
            Long {to_camel_case(fkCampoName(doublePks[1]))} = Long.parseLong(p{primeira_maiuscula(fkCampoName(doublePks[1]))});
            {primeira_maiuscula(doublePks[1]["fk"]["refTableName"])} secondKey = {to_camel_case(doublePks[1]["fk"]["refTableName"])}Service.id({to_camel_case(fkCampoName(doublePks[1]))});
            if(secondKey == null){{
                return ReturnJson.errorGeral(400, "{primeira_maiuscula(fkCampoName(doublePks[1]))} não encontrado.");
            }}

            cur{className} = new {className}({", ".join(finalRawParameters)});
            Boolean exists = false;
            if({serviceName}.id(new {primeira_maiuscula(className)}Key(firstKey, secondKey)) == null){{
                exists = true;
            }}
            cur{className}.set{primeira_maiuscula(className)}Key(new {primeira_maiuscula(className)}Key(firstKey, secondKey));
            {serviceName}.salvar(cur{className});
            if(exists){{
                return ReturnJson.success(202, cur{className}, "{className} salva com sucesso.");
            }}
            return ReturnJson.success(202, cur{className}, "{className} atualizada com sucesso.");
        }} catch (Exception e) {{
            return ReturnJson.error(400, e, "Erro ao atualizar {className}.");
        }}
    }}''')
        else:
            mappings.append(f'''\tpublic ResponseEntity<Map<String, Object>> salvar{className}(HttpServletRequest request) {{
                        
        {"\n\t\t".join(parameters)}
        
        try {{
            String paramatersAreNull = Generic.checkParamatersNull(Arrays.asList("{'", "'.join(rawParameters)}"), request);
            if(paramatersAreNull != null) return ReturnJson.errorGeral(400, paramatersAreNull);
            {"\n\t\t\t".join(parametersChecks)}
        }} catch (Exception e) {{
            return ReturnJson.error(500, e, "Erro ao validar parametros.");
        }}

        try {{
            {className} cur{className};
            {"\n\t\t\t".join(validParameters)}

            if(!Generic.stringHasContent(p{primeira_maiuscula(colPk)})){{ // Criando {className}
                cur{className} = new {className}({", ".join(finalRawParameters)});

                {serviceName}.salvar(cur{className});
                return ReturnJson.success(202, cur{className}, "{className} salva com sucesso.");
            }}

            {"\n\t\t\t".join(afterParameters)}
            cur{className} = new {className}({", ".join(finalRawParameters)});
            if({serviceName}.id({to_camel_case(colPk)}) == null){{
                return ReturnJson.errorGeral(400, "Erro ao encontrar '{className}' para atualizar.");
            }}
            cur{className}.set{primeira_maiuscula(colPk)}({to_camel_case(colPk)});
            {serviceName}.salvar(cur{className});
            return ReturnJson.success(202, cur{className}, "{className} atualizada com sucesso.");
        }} catch (Exception e) {{
            return ReturnJson.error(400, e, "Erro ao atualizar {className}.");
        }}
    }}''')
        # cur{className} = new {className}({to_camel_case(colPk)}, {", ".join(finalRawParameters)});
        # cur{className} = {serviceName}.id({to_camel_case(colPk)});
        mappings.append('')
        mappings.append('\t@PatchMapping("/")')
        mappings.append(f'''\tpublic ResponseEntity<Map<String, Object>> atualizar{className}(HttpServletRequest request) {{
        return this.salvar{className}(request);
    }}''')

        mappings.append('')
        if len(doublePks) > 1:
            firstTable = doublePks[0]["fk"]["refTableName"]
            secondTable = doublePks[1]["fk"]["refTableName"]

            firstPk = doublePks[0]["fk"]["refTableName"]
            secondPk = doublePks[1]["fk"]["refTableName"]

            mappings.append('\t@DeleteMapping("/")')
            mappings.append(f'''\tpublic ResponseEntity<Map<String, Object>> deletar{className}(HttpServletRequest request) {{
            try {{
                String p{primeira_maiuscula(firstPk)} = request.getParameter("{to_camel_case(firstPk)}");
                if(!Generic.stringHasContent(p{primeira_maiuscula(firstPk)})) return ReturnJson.errorGeral(400, "Chave primaria '{to_camel_case(firstPk)}' não pode ser nula.");
                if(!Generic.validNumber(p{primeira_maiuscula(firstPk)})) return ReturnJson.errorGeral(400, "Chave primaria '{to_camel_case(firstPk)}' deve ser um numero valido.");

                String p{primeira_maiuscula(secondPk)} = request.getParameter("{to_camel_case(secondPk)}");
                if(!Generic.stringHasContent(p{primeira_maiuscula(secondPk)})) return ReturnJson.errorGeral(400, "Chave primaria '{to_camel_case(secondPk)}' não pode ser nula.");
                if(!Generic.validNumber(p{primeira_maiuscula(secondPk)})) return ReturnJson.errorGeral(400, "Chave primaria '{to_camel_case(secondPk)}' deve ser um numero valido.");

                {primeira_maiuscula(firstTable)} firstKey = {to_camel_case(firstTable)}Service.id(Long.parseLong(p{primeira_maiuscula(firstPk)}));
                {primeira_maiuscula(secondTable)} secondKey = {to_camel_case(secondTable)}Service.id(Long.parseLong(p{primeira_maiuscula(secondPk)}));
                {primeira_maiuscula(className)}Key id = new {primeira_maiuscula(className)}Key(firstKey, secondKey);

                {className} find{className} = {serviceName}.id(id);
                if(find{className} == null) return ReturnJson.errorGeral(406, "{className} com id '" + id + "' não existe.");

                {serviceName}.deletar(id);
                return ReturnJson.success(202, find{className}, "{className} deletada com sucesso.");
            }} catch (Exception e) {{
                return ReturnJson.error(400, e, "Erro ao deletar {className}.");
            }}
        }}''')
        else:
            mappings.append('\t@DeleteMapping("/")')
            mappings.append(f'''\tpublic ResponseEntity<Map<String, Object>> deletar{className}(HttpServletRequest request) {{
            try {{
                String p{primeira_maiuscula(colPk)} = request.getParameter("{to_camel_case(colPk)}");
                if(!Generic.stringHasContent(p{primeira_maiuscula(colPk)})) return ReturnJson.errorGeral(400, "Chave primaria '{to_camel_case(colPk)}' não pode ser nula.");
                if(!Generic.validNumber(p{primeira_maiuscula(colPk)})) return ReturnJson.errorGeral(400, "Chave primaria '{to_camel_case(colPk)}' deve ser um numero valido.");
                Long id = Long.parseLong(p{primeira_maiuscula(colPk)});

                {className} find{className} = {serviceName}.id(id);
                if(find{className} == null) return ReturnJson.errorGeral(406, "{className} com id '" + id + "' não existe.");

                {serviceName}.deletar(id);
                return ReturnJson.success(202, find{className}, "{className} deletada com sucesso.");
            }} catch (Exception e) {{
                return ReturnJson.error(400, e, "Erro ao deletar {className}.");
            }}
        }}''')
        
        mappings.append('\n\t// ----------------------- REQUISICOES  CUSTOMIZAVEIS -----------------------')
        mappings.append('\t@GetMapping("/contar/")')
        mappings.append(f'''\tpublic ResponseEntity<Map<String, Object>> contar{className}() {{
        try {{
            Long count{className} = {serviceName}.contar();

            return ReturnJson.success(202, count{className}, "Sucesso ao buscar quantidade.");
        }} catch (Exception e) {{
            return ReturnJson.error(400, e, "Erro ao buscar quantidade.");
        }}
    }}''')
        
        for i in imports:
            importsTxt.append(f"import {i};")
        conteudo = f"""package {package}.controller.api;

{"\n".join(importsTxt)}

@RestController
@RequestMapping("/api/{className.lower()}/")
public class {repName} {{
    @Autowired
    {"\n\t@Autowired\n\t".join(services)}

{"\n".join(mappings)}

}}
"""
        createFile(f"{controller_path}{repName}{ext}", conteudo)

# --------------------------------------- TO REPOSITORY
def toRepository(dados):
    for item in dados:
        className = primeira_maiuscula(item["name"])
        repName = f"{className}Repository"

        imports = ["org.springframework.data.jpa.repository.JpaRepository", f"{package}.domain.{primeira_maiuscula(className)}", "java.util.List"]
        importsTxt = []

        findByLists = []
        pks = 0
        campoPkType = ""

        doublePks = returnPks(item)

        for col in item["columns"]:
            if col["pk"] is True:
                campoPkType = col["type"]
                break

        createdMultiPk = False
        for col in item["columns"]:
            if col["pk"] is True and col["name"] == 'ID':
                continue

            if "DATE" in col["rawType"]  or "TIMESTAMP" == col["rawType"]:
                addImport(imports, f"java.time.{col["type"]}")

            property = []
            if col["fk"] is None:    
                if col["type"].lower() == "string":       
                    findByLists.append(f"\tList<{className}> findBy{primeira_maiuscula(col["name"])}Containing({col["type"]} {to_camel_case(col["name"])});")
                findByLists.append(f"\tList<{className}> findBy{primeira_maiuscula(col["name"])}({col["type"]} {to_camel_case(col["name"])});")
            else:
                if(len(doublePks) > 1 and isMultiPk(col, doublePks)):
                    if not createdMultiPk:
                        createdMultiPk = True
                        campoPkType = primeira_maiuscula(className) + "Key"
                        addImport(imports, f"{package}.domain.{className}Key")
                        findByLists.append(f"\tList<{className}> findBy{className}Key({className}Key {to_camel_case(item["name"])}Key);")
                else:    
                    addImport(imports, f"{package}.domain.{primeira_maiuscula(col["fk"]["refTableName"])}")
                    campoFk = fkCampoName(col)
                    findByLists.append(f"\tList<{className}> findBy{primeira_maiuscula(campoFk)}({primeira_maiuscula(col["fk"]["refTableName"])} {campoFk});")
        
        for i in imports:
            importsTxt.append(f"import {i};")
        conteudo = f"""package {package}.repository;

{"\n".join(importsTxt)}

public interface {repName} extends JpaRepository<{className}, {campoPkType}> {{

{"\n".join(findByLists)}

}}
"""
        createFile(f"{repository_path}{repName}{ext}", conteudo)

# --------------------------------------- TO DOMAIN

def toDomain(dados):
    for item in dados:
        className = primeira_maiuscula(item["name"])
        imports = ["jakarta.persistence.*"]
        pksImports = ["jakarta.persistence.*", "java.io.Serializable"]

        importsTxt = []
        pksImportsTxt = []

        pks = []
        pksJuntos = []
        pksPreenchidos = []

        campos = []
        camposJuntos = []
        camposPreenchidos = []
        systemCampos = []
        
        doublePks = returnPks(item)
        createdMultiPk = False

        if len(doublePks) > 1 and not allowDoublePks:
            continue

        for i in item["columns"]:
            if(i["name"] in systemColumns):
                    continue
            
            # if len(doublePks) > 1 and isMultiPk(i, doublePks):
                # if not createdMultiPk:
                #     createdMultiPk = True
                #     camposJuntos.insert(0, f"{primeira_maiuscula(item["name"])}Key {primeira_maiuscula(item["name"])}Key")
                #     camposPreenchidos.insert(0, f"\t\tthis.{to_camel_case(item["name"])}Key = {to_camel_case(item["name"])}Key;")
            
            if i["fk"] is None:
                if i["pk"] == False:
                    camposJuntos.append(f"{i["type"]} {to_camel_case(i["name"])}")
                    camposPreenchidos.append(f"\t\tthis.{to_camel_case(i["name"])} = {to_camel_case(i["name"])};")
                elif i["pk"] == True:
                    pksJuntos.append(f"{i["type"]} {to_camel_case(i["name"])}")
                    pksPreenchidos.append(f"\t\tthis.{to_camel_case(i["name"])} = {to_camel_case(i["name"])};")
            else:
                if i["pk"] == False and len(doublePks) == 1:
                    campoFk = fkCampoName(i)
                    camposJuntos.append(f"{primeira_maiuscula(i["fk"]["refTableName"])} {campoFk}")
                    camposPreenchidos.append(f"\t\tthis.{campoFk} = {campoFk}; // fk")
                elif i["pk"] == True:
                    campoFk = fkCampoName(i)
                    pksJuntos.append(f"{primeira_maiuscula(i["fk"]["refTableName"])} {campoFk}")
                    pksPreenchidos.append(f"\t\tthis.{campoFk} = {campoFk};")

        systemCampos.append(f"\t\tthis.dataCadastro = LocalDateTime.now();")
        systemCampos.append(f"\t\tthis.dataAtualizacao = LocalDateTime.now();")

        gettersSetters = []
        pksGettersSetters = []
        createdMultiPk = False
        for col in item["columns"]:
            if(len(doublePks) > 1 and isMultiPk(col, doublePks)):
                if not createdMultiPk:
                    campos.insert(0,'') 
                    campos.insert(1,'\n\t@EmbeddedId') 
                    campos.insert(2, f"\tprivate {primeira_maiuscula(item["name"])}Key {to_camel_case(item["name"])}Key;")
                    campos.insert(3, "")
                    gettersSetters.insert(0, f"\tpublic {primeira_maiuscula(item["name"])}Key get{primeira_maiuscula(item["name"])}Key() {{ return {to_camel_case(item["name"])}Key; }}")
                    gettersSetters.insert(1, f"\tpublic void set{primeira_maiuscula(item["name"])}Key({primeira_maiuscula(item["name"])}Key {to_camel_case(item["name"])}Key) {{ this.{to_camel_case(item["name"])}Key = {to_camel_case(item["name"])}Key; }}")
                    gettersSetters.insert(2, "")
                    createdMultiPk = True

            if "DATE" in col["rawType"]  or "TIMESTAMP" == col["rawType"]:
                addImport(imports, f"com.fasterxml.jackson.annotation.JsonFormat")
                addImport(imports, f"java.time.{col["type"]}")

            property = []
            if col["fk"] is None:
                if col["pk"] is True:
                    campos.append(f"\t@Id")
                    campos.append(f"\t@GeneratedValue(strategy = GenerationType.IDENTITY)")
                else:
                    if col["notNull"] is True: property.append(f"nullable = false")
                    if "len" in col: property.append(f"length = {col["len"]}")

                    if col["type"] == 'LocalDate':
                        campos.append(f'\t@JsonFormat(pattern = "dd/MM/yyyy")')
                    elif col["type"] == 'LocalDateTime':
                        campos.append(f'\t@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")')
                    campos.append(f"\t@Column({", ".join(property)})")

                
                campos.append(f"\tprivate {col["type"]} {to_camel_case(col["name"])};")

                gettersSetters.append(f"\tpublic {col["type"]} get{primeira_maiuscula(col["name"])}() {{ return {to_camel_case(col["name"])}; }}")
                gettersSetters.append(f"\tpublic void set{primeira_maiuscula(col["name"])}({col["type"]} {to_camel_case(col["name"])}) {{ this.{to_camel_case(col["name"])} = {to_camel_case(col["name"])}; }}")
            else:
                if(len(doublePks) > 1 and isMultiPk(col, doublePks)):
                    campoFk = fkCampoName(col)
                    property.append(f'referencedColumnName = "{to_camel_case(col["fk"]["refCampo"])}"')
                    property.append(f"nullable = false")
                    if "len" in col: property.append(f"length = {col["len"]}")

                    # addImport(pksImports, f"{package}.domain.{primeira_maiuscula(col["fk"]["refTableName"])}")
                    pks.append(f"\t@ManyToOne")
                    pks.append(f"\t@JoinColumn({", ".join(property)})")
                    pks.append(f"\tprivate {primeira_maiuscula(col["fk"]["refTableName"])} {campoFk};")
                    pks.append(f"")

                    pksGettersSetters.append(f"\tpublic {primeira_maiuscula(col["fk"]["refTableName"])} get{primeira_maiuscula(campoFk)}() {{ return {campoFk}; }}")
                    pksGettersSetters.append(f"\tpublic void set{primeira_maiuscula(campoFk)}({primeira_maiuscula(col["fk"]["refTableName"])} {campoFk}) {{ this.{campoFk} = {campoFk}; }}")
                else:
                    campoFk = fkCampoName(col)
                    campos.append(f"\t@ManyToOne")
                    property.append(f'referencedColumnName = "{to_camel_case(col["fk"]["refCampo"])}"')
                    property.append(f"nullable = false")
                    if "len" in col: property.append(f"length = {col["len"]}")

                    campos.append(f"\t@JoinColumn({", ".join(property)})")
                    campos.append(f"\tprivate {primeira_maiuscula(col["fk"]["refTableName"])} {campoFk};")

                    gettersSetters.append(f"\tpublic {primeira_maiuscula(col["fk"]["refTableName"])} get{primeira_maiuscula(campoFk)}() {{ return {campoFk}; }}")
                    gettersSetters.append(f"\tpublic void set{primeira_maiuscula(campoFk)}({primeira_maiuscula(col["fk"]["refTableName"])} {campoFk}) {{ this.{campoFk} = {campoFk}; }}")
            if not (len(doublePks) > 1 and isMultiPk(col, doublePks)):
                campos.append("")
                gettersSetters.append("")
        
        for i in imports:
            importsTxt.append(f"import {i};")
        
        porra = ""
        if len(camposJuntos) >= 1:
            porra = f"""public {className}({", ".join(camposJuntos)}) {{
{"\n".join(camposPreenchidos)}
    
{"\n".join(systemCampos)}
    }}
"""
        conteudo = f"""package {package}.domain;

{"\n".join(importsTxt)}

@Entity
public class {className} {{

{"\n".join(campos)}
    public {className}() {{
{"\n".join(systemCampos)}
    }}
    {porra}

{"\n".join(gettersSetters)}
}}
"""
        createFile(f"{domain_path}{className}{ext}", conteudo)

        if len(doublePks) > 1:
            for i in pksImports:
                pksImportsTxt.append(f"import {i};")

            pkContent = f"""package {package}.domain;

{"\n".join(pksImportsTxt)}

@Embeddable
public class {primeira_maiuscula(item["name"])}Key implements Serializable {{
{"\n".join(pks)}

    public {primeira_maiuscula(item["name"])}Key() {{ }}
    public {primeira_maiuscula(item["name"])}Key({", ".join(pksJuntos)}) {{
{"\n".join(pksPreenchidos)}
    
    }}

{"\n".join(pksGettersSetters)}

    @Override
    public boolean equals(Object o) {{
        if (this == o) return true;
        if (!(o instanceof {primeira_maiuscula(item["name"])}Key)) return false;
        {primeira_maiuscula(item["name"])}Key that = ({primeira_maiuscula(item["name"])}Key) o;
        return {fkCampoName(doublePks[0])}.equals(that.{fkCampoName(doublePks[0])}) && {fkCampoName(doublePks[1])}.equals(that.{fkCampoName(doublePks[1])});
    }}

    @Override
    public int hashCode() {{
        return java.util.Objects.hash({fkCampoName(doublePks[0])}, {fkCampoName(doublePks[1])});
    }}
}}
"""
            createFile(f"{domain_path}{primeira_maiuscula(item["name"])}Key{ext}", pkContent)


dirs = ["domain", "service", "repository", "dto", "controller", "controller/api"]
try:
    for d in dirs:
        os.makedirs(d, exist_ok=True)

    with open(file_path, "r", encoding="utf-8") as f:
        dados = json.load(f)
    startProcess(dados)
except FileNotFoundError:
    print(f"Error: The file '{file_path}' was not found.")
except Exception as e:
    print(f"An error occurred: {e}")
