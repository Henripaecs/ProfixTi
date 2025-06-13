package com.tiprofix.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    // Gerar o hash da senha com MD5
    public static String hashMD5(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(senha.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));  // Converte para hexadecimal
            }
            return sb.toString();  // Retorna o hash gerado
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash MD5", e);
        }
    }

    // Verificar se a senha fornecida corresponde ao hash armazenado no banco
    public static boolean verificarSenha(String senha, String senhaHashArmazenada) {
        // Gera o hash MD5 da senha fornecida
        String senhaHash = hashMD5(senha);
        
        // Compara o hash gerado da senha fornecida com o hash armazenado no banco
        return senhaHash.equals(senhaHashArmazenada);
    }
}
