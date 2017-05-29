package com.demo.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

public class AESEncryption {

	public static SecretKey encryKey = null;
	
	public static SecretKey secKey = null;
	
	private static String bytesToHex(byte[] hash) {
		return DatatypeConverter.printHexBinary(hash);
	}
	
	public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
		Cipher aesCipher = Cipher.getInstance("AES");
		aesCipher.init(Cipher.DECRYPT_MODE, secKey);
		byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
		return new String(bytePlainText);
	}

	public static byte[] encryptText(String plainText, SecretKey secKey) throws Exception {
		// AES defaults to AES/ECB/PKCS5Padding in Java 7
		Cipher aesCipher = Cipher.getInstance("AES");
		aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
		byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
		return byteCipherText;
	}

	public static SecretKey getSecretEncryptionKey() throws Exception {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(128); // The AES key size in number of bits
		SecretKey secKey = generator.generateKey();
		return secKey;
	}

	private static byte[] HexToBytes(String hex) {
		return DatatypeConverter.parseHexBinary(hex);
	}

	public AESEncryption() {
		try {
			if (secKey == null) {
				secKey = getSecretEncryptionKey();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String DecryptToken(String cipherTextHex) throws Exception {

		HexToBytes(cipherTextHex);
		String decryptedText = decryptText(HexToBytes(cipherTextHex), secKey);
		return decryptedText;
	}

	public String EncryptToken(String plainText) throws Exception {
		getSecretEncryptionKey();

		byte[] cipherText = encryptText(plainText, secKey);
		return bytesToHex(cipherText);
	}
}
