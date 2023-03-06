package com.roaker.notes;

import lombok.extern.flogger.Flogger;
import org.apache.commons.lang3.StringUtils;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lei.rao
 * @since 1.0
 */
@Flogger
public class NoobChainII {
    private static final List<Block> blockChain = new ArrayList<>();

    private static final Map<String, TransactionOutput> UTXOs = new HashMap<>();
    private static final int difficulty = 3;
    private static final float minimumTransaction = 0.1f;
    private static Wallet walletA;

    private static Wallet walletB;

    private static Transaction genesisTransaction;

    public static float getMinimumTransaction() {
        return minimumTransaction;
    }

    public static Map<String, TransactionOutput> getUTXOs() {
        return UTXOs;
    }

    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        walletA = new Wallet();
        walletB = new Wallet();
        Wallet coinbase = new Wallet();

        //create genesis transaction, which sends 100 NoobCoin to WalletA:
        genesisTransaction = new Transaction(coinbase.getPublicKey(), walletA.getPublicKey(), 100f, null);
        genesisTransaction.generateSignature(coinbase.getPrivateKey());
        genesisTransaction.setTransactionId("0");
        genesisTransaction.getOutputs().add(new TransactionOutput(genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId()));
        UTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0));

        log.atInfo().log("Creating and Mining Genesis block...");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        //testing
        Block block1 = new Block(genesis.getHash());
        log.atInfo().log("WalletA's balance is: %s", walletA.getBalance());
        log.atInfo().log("WalletA is Attemping to send funds(40) to WalletB.....");
        block1.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 40f));
        addBlock(block1);
        log.atInfo().log("WalletA's balance is: %s", walletA.getBalance());
        log.atInfo().log("WalletB's balance is: %s", walletB.getBalance());

        Block block2 = new Block(block1.getHash());
        log.atInfo().log("WalletA Attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 1000f));
        addBlock(block2);
        log.atInfo().log("WalletA's balance is: %s", walletA.getBalance());
        log.atInfo().log("WalletB's balance is: %s", walletB.getBalance());


        Block block3 = new Block(block2.getHash());
        log.atInfo().log("WalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletB.sendFunds(walletA.getPublicKey(), 20f));
        log.atInfo().log("WalletA's balance is: %s", walletA.getBalance());
        log.atInfo().log("WalletB's balance is: %s", walletB.getBalance());


        isChainValid();
    }


    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        Map<String, TransactionOutput> tempUTXOs = new HashMap<>();
        tempUTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0));

        //loop through blockchain to check hashes
        for (int i = 1; i < blockChain.size(); i++) {
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i - 1);

            if (!StringUtils.equals(currentBlock.getHash(), currentBlock.calculateHash())) {
                log.atWarning().log("#Current Hashes not equal");
                return false;
            }

            if (!StringUtils.equals(previousBlock.getHash(), currentBlock.getPreviousHash())) {
                log.atWarning().log("#Previous Hashes not equal");
                return false;
            }

            if (!StringUtils.substring(currentBlock.getHash(), 0, difficulty).equals(hashTarget)) {
                log.atWarning().log("#This block hasn't been mined");
                return false;
            }

            //loop through blockchains transactions:
            TransactionOutput tempOutput;
            for (int t = 0; t < currentBlock.getTransactions().size(); t++) {
                Transaction currentTransaction = currentBlock.getTransactions().get(t);
                if (!currentTransaction.verifySignature()) {
                    log.atInfo().log("#Signature on Transaction(%s) is Invalid", t);
                    return false;
                }
                if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    log.atInfo().log("#Inputs are not equal to outputs On Transaction(%s)", t);
                    return false;
                }

                for (TransactionInput input : currentTransaction.getInputs()) {
                    tempOutput = tempUTXOs.get(input.getTransactionOutputId());
                    if (tempOutput == null) {
                        log.atInfo().log("#Referenced input on Transaction(%s) is Missing", t);
                        return false;
                    }

                    if (input.getUTXO().getValue() != tempOutput.getValue()) {
                        log.atInfo().log("#Referenced input Transaction(%s) value is Invalid", t);
                        return false;
                    }

                    tempUTXOs.remove(input.getTransactionOutputId());
                }

                for (TransactionOutput output : currentTransaction.getOutputs()) {
                    tempUTXOs.put(output.getId(), output);
                }

                if (currentTransaction.getOutputs().get(0).getRecipient() != currentTransaction.getRecipient()) {
                    log.atInfo().log("#Transaction(" + t + ") output reciepient is not who it should be");
                    return false;
                }
                if (currentTransaction.getOutputs().get(1).getRecipient() != currentTransaction.getSender()) {
                    log.atInfo().log("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }
            }
        }
        log.atInfo().log("Blockchain is valid");
        return true;
    }


    private static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockChain.add(newBlock);
    }
}
