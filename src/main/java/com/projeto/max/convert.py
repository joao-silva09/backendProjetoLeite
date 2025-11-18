import re
import json

tables = []
types_mapping = {
    "INT": "Long",
    "BIGINT": "Long",
    "BOOLEAN": "Boolean",
    "VARCHAR": "String",
    "CHAR": "String",
    "TEXT": "String",
    "TIMESTAMP": "LocalDateTime",
    "DATETIME": "LocalDateTime",
    "DATE": "LocalDate",
    "FLOAT": "Float",
    "DOUBLE": "Double",
    "NUMERIC": "Double",
    "DECIMAL": "Double"
}

def download_tables():
    with open("tabelas.json", "w", encoding="utf-8") as f:
        json.dump(tables, f, indent=2, ensure_ascii=False)

def clear_sql(sql: str) -> str:
    sql = sql.replace("\n", "")
    sql = sql.replace(", ", ",")
    sql = sql.replace("; ", ";")
    sql = sql.replace(" ( ", "(")
    sql = sql.replace(" ) ", ")")
    sql = sql.strip().upper()
    return sql

def clear_word(s: str) -> str:
    s = s.replace("(", "")
    s = s.replace(")", "")
    return s.strip()

def find_end(s: str, pos: int, start="(", end=")"):
    count = 0
    for i in range(pos, len(s)):
        if s[i] == start:
            count += 1
        if s[i] == end:
            count -= 1
        if count == 0:
            return i
    return 0

def create_primary_keys(cur_table, pk_column):
    for col in cur_table["columns"]:
        if col["name"] == pk_column:
            col["pk"] = True
            break

def create_foreign_keys(cur_table, fk_column, ref_table, ref_field):
    for col in cur_table["columns"]:
        if col["name"] == fk_column:
            table = None
            field = None
            for t in tables:
                if t["name"] == ref_table:
                    table = t
                    for c in t["columns"]:
                        if(c["name"] == ref_field):
                            field = c
                            break
                    break
            col["fk"] = {"refTableName": ref_table, "refTable": table, "refCampo": ref_field, "campo": field}
            break

def create_sql_object(sql: str):
    clean_sql = clear_sql(sql)

    loop_index = 0
    cur_word = ""
    words = []

    while True:
        if loop_index >= len(clean_sql):
            break
        cur_letter = clean_sql[loop_index]

        if cur_letter == ")":
            loop_index += 1
            continue

        cur_word += cur_letter
        cur_word = clear_word(cur_word)

        if cur_letter in [" ", "(", ";"]:
            words.append(cur_word)

            if len(words) >= 3 and cur_letter == "(":
                if words[-3] == "CREATE" and words[-2] == "TABLE":
                    cur_table = {"name": cur_word, "columns": [], "fks": []}

                    end_idx = find_end(clean_sql, loop_index, "(", ")")
                    cur_columns_str = clean_sql[loop_index + 1:end_idx].strip()
                    cur_columns = cur_columns_str.split(",")

                    for col_def in cur_columns:
                        col_parts = col_def.strip().split(" ")
                        col = {"name": "", "type": "", "pk": False, "fk": None, "notNull": False}

                        if col_parts[0] == "PRIMARY" and col_parts[1] == "KEY":
                            create_primary_keys(cur_table, clear_word(col_parts[2]))
                            continue
                        elif col_parts[0] == "FOREIGN" and col_parts[1] == "KEY":
                            if len(col_parts) == 6:
                                create_foreign_keys(cur_table,
                                    clear_word(col_parts[2]),
                                    clear_word(col_parts[4]),
                                    clear_word(col_parts[5]))
                            elif len(col_parts) == 5:
                                create_foreign_keys(cur_table,
                                    clear_word(col_parts[2]),
                                    clear_word(col_parts[4].split('(')[0]),
                                    clear_word(col_parts[4].split('(')[1]))
                            continue
                        else:
                            if len(col_parts) == 1:
                                create_primary_keys(cur_table, clear_word(col_parts[0]))
                                continue

                            col["name"] = clear_word(col_parts[0])
                            raw_type = col_parts[1].split("(")[0]
                            col["rawType"] = raw_type
                            col["type"] = types_mapping.get(raw_type, raw_type)

                            if "NOT NULL" in col_def:
                                col["notNull"] = True
                            if "PRIMARY KEY" in col_def:
                                col["pk"] = True
                            if "AUTO_INCREMENT" in col_def:
                                col["autoIncrement"] = True
                            if col["type"] == "String" and "(" in col_parts[1]:
                                col["len"] = int(re.findall(r"\d+", col_parts[1])[0])

                        if len(col["name"].strip()) == 0:
                            raise Exception("Coluna sem nome detectada")

                        cur_table["columns"].append(col)

                    del cur_table["fks"]
                    tables.append(cur_table)
                    loop_index += len(cur_columns_str)
            cur_word = ""
        loop_index += 1

    # print(json.dumps(tables, indent=2, ensure_ascii=False))yy
    # print(clean_sql)


file_path = "banco.sql"
try:
    with open(file_path, 'r', encoding='utf-8') as file:
        file_content = file.read()
    create_sql_object(file_content)
    download_tables()
except FileNotFoundError:
    print(f"Error: The file '{file_path}' was not found.")
except Exception as e:
    print(f"An error occurred: {e}")
