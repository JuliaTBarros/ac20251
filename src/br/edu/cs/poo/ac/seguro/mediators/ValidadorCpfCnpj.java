package br.edu.cs.poo.ac.seguro.mediators;

public class ValidadorCpfCnpj {

    public static boolean ehCnpjValido(String cnpj) {
        if (cnpj == null || cnpj.length() != 14 || !StringUtils.temSomenteNumeros(cnpj)) {
            return false;
        }

        if (temDigitosIguais(cnpj)) {
            return false;
        }

        int dv1 = calcularDigitoVerificador(cnpj, 0, 12, new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});

        int[] multiplicador2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += (cnpj.charAt(i) - '0') * multiplicador2[i];
        }
        soma += dv1 * multiplicador2[12];
        int resto = soma % 11;
        int dv2 = resto < 2 ? 0 : 11 - resto;

        return (cnpj.charAt(12) - '0' == dv1) && (cnpj.charAt(13) - '0' == dv2);
    }

    public static boolean ehCpfValido(String cpf) {
        if (cpf == null || cpf.length() != 11 || !StringUtils.temSomenteNumeros(cpf)) {
            return false;
        }

        if (temDigitosIguais(cpf)) {
            return false;
        }

        int dv1 = calcularDigitoVerificador(cpf, 0, 9, new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2});

        int[] multiplicador2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * multiplicador2[i];
        }
        soma += dv1 * multiplicador2[9];
        int resto = soma % 11;
        int dv2 = resto < 2 ? 0 : 11 - resto;

        return (cpf.charAt(9) - '0' == dv1) && (cpf.charAt(10) - '0' == dv2);
    }

    private static boolean temDigitosIguais(String documento) {
        char primeiro = documento.charAt(0);
        for (int i = 1; i < documento.length(); i++) {
            if (documento.charAt(i) != primeiro) {
                return false;
            }
        }
        return true;
    }

    private static int calcularDigitoVerificador(String documento, int inicio, int fim, int[] multiplicadores) {
        int soma = 0;
        for (int i = inicio, j = 0; i < fim; i++, j++) {
            soma += (documento.charAt(i) - '0') * multiplicadores[j];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
