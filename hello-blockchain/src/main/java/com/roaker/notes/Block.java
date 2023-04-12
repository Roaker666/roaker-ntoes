package com.roaker.notes;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.flogger.Flogger;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
@Flogger
public class Block {
    private String hash;
    private String previousHash;
    private String merkleRoot;
    private List<Transaction> transactions = new ArrayList<>();
    private long timeStamp;
    private int nonce;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = System.currentTimeMillis();
        this.hash = calculateHash();
    }


    public String calculateHash() {
        return applySha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + merkleRoot);
    }

    public void mineBlock(int difficulty) {
        merkleRoot = getMerkleRoot(transactions);
        //Create a string with difficulty * "0"
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        log.atInfo().log("Block Mined!!! : %s", hash);
    }

    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore
        if (transaction == null) {
            return false;
        }
        if (!StringUtils.equals(previousHash, "0")) {
            if (!transaction.processTransaction()) {
                log.atInfo().log("Transaction failed to process, Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        log.atInfo().log("Transaction Successfully added to Block");
        return true;
    }


    @SneakyThrows
    public static String applySha256(String input) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    @SneakyThrows
    public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
        Signature dsa = Signature.getInstance("ECDSA", "BC");
        dsa.initSign(privateKey);
        dsa.update(input.getBytes());
        return dsa.sign();
    }

    @SneakyThrows
    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(data.getBytes());
        return ecdsaVerify.verify(signature);
    }

    public static String getMerkleRoot(List<Transaction> transactionList) {
        int count = transactionList.size();
        List<String> previousTreeLayer = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            previousTreeLayer.add(transaction.getTransactionId());
        }
        List<String> treeLayer = previousTreeLayer;
        while (count > 1){
            treeLayer = new ArrayList<>();
            for (int i = 1; i < previousTreeLayer.size(); i++) {
                treeLayer.add(applySha256(previousTreeLayer.get(i - 1) + previousTreeLayer.get(i)));
            }
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }
        return treeLayer.size() == 1? treeLayer.get(0) : "";
    }
}
