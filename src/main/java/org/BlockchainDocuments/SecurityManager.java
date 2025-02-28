package org.BlockchainDocuments;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityManager {

    /**
     * This Method hashes the Input using the SHA-256-Algorithm
     * @param input The String that should be hashed
     * @return The hexadecimal interpretation of the hash
     */
    public static String hash(String input){
        try {
            MessageDigest hashfunc = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = hashfunc.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexStringOut = new StringBuilder(2 * hashBytes.length);
            for(int i = 0; i < hashBytes.length; i++){
                String hex = Integer.toHexString(hashBytes[i]);
                if(hex.length() == 1) hex = "0" + hex;
                hex = hex.substring(hex.length()-2);
                hexStringOut.append(hex);
            }
            return hexStringOut.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
