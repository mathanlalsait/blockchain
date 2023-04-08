package com.globalsoftwaresupport.cryptocurrency;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;

public class CryptographyHelper {

	// SHA-256 hash (256 bits = 64 hexadecimal characters)
	public static String generateHash(String data) {
		try {
			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(data.getBytes("UTF-8"));

			//we want to end up with hexadecimal values not bytes
			StringBuilder hexadecimalString = new StringBuilder();
		
			for (int i = 0; i < hash.length; i++) {
				String hexadecimal = Integer.toHexString(hash[i] & 0xff);
				if (hexadecimal.length() == 1) hexadecimalString.append('0');
				hexadecimalString.append(hexadecimal);
			}
			
			return hexadecimalString.toString();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	// generate public key and private key
	// private key: 256 bits long random integer
	// public key: point on the elliptic curve
	// (x,y) - both of these values are 256 bits long
	public static KeyPair ellipticCurveCrypto() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
			ECGenParameterSpec params = new ECGenParameterSpec("prime256v1");
			keyPairGenerator.initialize(params);
			return keyPairGenerator.generateKeyPair();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	// ECC to sign the given transaction (message)
	// elliptic curve digital signature algorithm (ECDSA)
	public static byte[] sign(PrivateKey privateKey, String input) {
		Signature signature;
		byte[] output = new byte[0];
		
		try {
			// we use bouncy castle for ECC
			signature = Signature.getInstance("ECDSA", "BC");
			signature.initSign(privateKey);
			signature.update(input.getBytes());
			output = signature.sign();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		return output;
	}
	
	// checks whether the given transaction belongs to the sender based on the signature
	public static boolean verify(PublicKey publicKey, String data, byte[] signature) {
		try {
			Signature ecdsaSignature = Signature.getInstance("ECDSA", "BC");
			ecdsaSignature.initVerify(publicKey);
			ecdsaSignature.update(data.getBytes());
			return ecdsaSignature.verify(signature);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}







