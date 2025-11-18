package com.projeto.max.generic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public class Generic {
    public static Long coletor = 1L;
    public static Long produtor = 2L;
    public static Long laticionio = 3L;
    public static Long admin = 4L;

    public static Boolean validInt(String string){
        try {
            Integer.parseInt(string.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean validNumber(String string){
        Boolean validFloat = false;
        Boolean validDouble = false;
        Boolean validLong = false;

        try {
            Float.parseFloat(string.trim());
            validFloat = true;
        } catch (Exception e) {
            validFloat = false;
        }

        try {
            Double.parseDouble(string.trim());
            validDouble = true;
        } catch (Exception e) {
            validDouble = false;
        }

        try {
            Long.parseLong(string.trim());
            validLong = true;
        } catch (Exception e) {
            validLong = false;
        }

        if(validFloat || validDouble || validLong) return true;
        return false;
    }

    public static Boolean validLocalDate(String string){
        try {
            formatLocalDate(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static Boolean validLocalDateTime(String string){
        try {
            formatLocalDateTime(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validEmail(String email) {
        return true;
    }

    public static LocalDate formatLocalDate(String string){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(string, formatter);
    }
    public static LocalDateTime formatLocalDateTime(String string){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return LocalDateTime.parse(string, formatter);
    }

    public static void log(String text){
        System.out.println(text);
    }

    public static String getString(Map<String, Object> obj, String string){
        return (String) obj.get(string);
    }

    public static Boolean stringHasContent(String string){
        if(string == null) return false;
        if(string.trim().length() == 0) return false;
        return true;
    }

    public static String checkParamatersNull(List<String> strings, HttpServletRequest request){
        for (int i = 0; i < strings.size(); i++) {
            String curParameter = strings.get(i);
            // String parameter = "p" + curParameter.substring(0, 1).toUpperCase() + curParameter.substring(1);
            if(!stringHasContent(request.getParameter(curParameter))) return "Parametro '" + curParameter + "' não pode ser nulo.";
        }
        return null;
    }

    public static String checkParamatersLength(Boolean big, List<String> strings, List<Integer> sizes, HttpServletRequest request){
        for (int i = 0; i < strings.size(); i++) {
            String curParameter = strings.get(i);
            String curValue = request.getParameter(curParameter);

            if(big == true) {
                if(curValue.length() > sizes.get(i)) return "Parametro '" + curParameter + "' não pode ter mais de " + sizes.get(i) + " caracteres.";
            } else {
                if(curValue.length() < sizes.get(i)) return "Parametro '" + curParameter + "' não pode ter menos de " + sizes.get(i) + " caracteres.";
            }
        }
        return null;
    }

    public static String cleanDocumento(String documento) {
        if (documento == null) return null;
        return documento.replaceAll("[^0-9]", "");
    }

    public static boolean validCpf(String cpf) {
        if (cpf == null) return false;
        cpf = cleanDocumento(cpf);

        if (cpf.length() != 11) return false;
        if (cpf.matches("(\\d)\\1{10}")) return false;

        return true;
    }

    public static boolean validCnpj(String cnpj) {
        if (cnpj == null) return false;

        cnpj = cleanDocumento(cnpj);

        if (cnpj.length() != 14) return false;
        if (cnpj.matches("(\\d)\\1{13}")) return false;

        return true;
    }

}
