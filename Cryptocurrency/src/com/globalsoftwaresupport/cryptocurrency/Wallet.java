package com.globalsoftwaresupport.cryptocurrency;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.globalsoftwaresupport.blockchain.Blockchain;

public class Wallet {

	// users of the network
	// used for signature
	private PrivateKey privateKey;
	// verification
	// address: RIPMD public key (160 bits)
	private PublicKey publicKey;
	
	public Wallet() {
		KeyPair keyPair = CryptographyHelper.ellipticCurveCrypto();
		this.privateKey = keyPair.getPrivate();
		this.publicKey = keyPair.getPublic();
	}
	
	// WE ARE ABLE TO TRANSFER MONEY !!!
	// miners of the blockchain will put this transaction into the blockchain
	public Transaction transferMoney(PublicKey receiver, double amount) {
		
		if(calculateBalance() < amount) {
			System.out.println("Invalid transactin because of not enough money...");
			return null;
		}
		
		//we store the inputs for the transaction in this array
		List<TransactionInput> inputs = new ArrayList<TransactionInput>();
		
		// let's find our unspent transactions (the blockchain stores all the UTXOs)
		for (Map.Entry<String, TransactionOutput> item: Blockchain.UTXOs.entrySet()) {
			TransactionOutput UTXO = item.getValue();
			
			if(UTXO.isMine(this.publicKey))
				inputs.add(new TransactionInput(UTXO.getId()));
		}
		
		//let's create the new transaction
		Transaction newTransaction = new Transaction(publicKey, receiver , amount, inputs);
		//the sender signs the transaction
		newTransaction.generateSignature(privateKey);
		
		return newTransaction;
	}
	
	// there is no balance associated with the users
	// UTXOs and consider all the transactions in the past
	public double calculateBalance() {
		
		double balance = 0;	
		
		for (Map.Entry<String, TransactionOutput> item: Blockchain.UTXOs.entrySet()) {
			TransactionOutput transactionOutput = item.getValue();
			
			if(transactionOutput.isMine(publicKey))
				balance += transactionOutput.getAmount() ;
		}
		
		return balance;
	}
	
	public PublicKey getPublicKey() {
		return publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}
}






