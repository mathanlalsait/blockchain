package com.globalsoftwaresupport.blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.globalsoftwaresupport.constants.Constants;
import com.globalsoftwaresupport.cryptocurrency.CryptographyHelper;
import com.globalsoftwaresupport.cryptocurrency.Transaction;

public class Block {

	private int id;
	private int nonce;
	private long timeStamp;
	private String hash;
	private String previousHash;
	// Ethereum every block stores 1500-2000 transactions
	public List<Transaction> transactions;
	
	public Block(String previousHash ) {
		this.transactions = new ArrayList<>();
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		generateHash(); 
	}
	
	public void generateHash() {
		String dataToHash = Integer.toString(id)+previousHash+Long.toString(timeStamp)+
				transactions.toString()+Integer.toString(nonce);
		this.hash = CryptographyHelper.generateHash(dataToHash);
	}
	
	// check if the given transaction is valid or not
	public boolean addTransaction(Transaction transaction) {
			
		if(transaction == null) return false;
		
		// if the block is the genesis block we do not process
		if((!previousHash.equals(Constants.GENESIS_PREV_HASH))) {
			if((!transaction.verifyTransaction())) {
				System.out.println("Transaction is not valid...");
				return false;
			}
		}
		
		transactions.add(transaction);
		System.out.println("Transaction is valid and it's added to the block "+this);
		return true;
	}
	
	// the nonce is the only parameter the miners can tune (change)
	public void incrementNonce() {
		this.nonce++;
	}
	
	// this SHA-256 hash identifies the block
	public String getHash() {
		return this.hash;
	}
}




