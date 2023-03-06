package com.roaker.notes;

import lombok.Data;
import lombok.extern.flogger.Flogger;
import org.apache.commons.lang3.StringUtils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
@Flogger
public class Transaction {
    /**
     * also the hash of the transaction
     */
    private String transactionId;
    /**
     * senders address/public key
     */
    private PublicKey sender;
    /**
     * Recipients address/public key
     */
    private PublicKey recipient;
    private float value;
    /**
     * prevent anybody else from spending funds in our wallet
     */
    private byte[] signature;

    private List<TransactionInput> inputs = new ArrayList<>();
    private List<TransactionOutput> outputs = new ArrayList<>();
    /**
     * a rough count of how many transactions have been generated
     */
    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, List<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
        sequence++;
        return Block.applySha256(Block.getStringFromKey(sender) +
                Block.getStringFromKey(recipient) +
                Float.toString(value) +
                sequence);
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = Block.getStringFromKey(sender) + Block.getStringFromKey(recipient) + Float.toString(value);
        signature =  Block.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = Block.getStringFromKey(sender) + Block.getStringFromKey(recipient) + Float.toString(value);
        return Block.verifyECDSASig(sender, data, signature);
    }

    /**
     *
     * @return sum of inputs(UTXOs) values
     */
    public float getInputsValue() {
        float total = 0;
        for (TransactionInput i : inputs) {
            if (i.getUTXO() == null) {
                continue;
            }
            total += i.getUTXO().getValue();
        }
        return total;
    }

    /**
     *
     * @return sum of outputs
     */
    public float getOutputsValue() {
        float total= 0;
        for (TransactionOutput o: outputs) {
            total += o.getValue();
        }
        return total;
    }

    public boolean processTransaction() {
        if (!verifySignature()) {
            log.atInfo().log("#Transaction Signature failed to verify");
            return false;
        }

        //gather transaction inputs (Make sure they are unspent)
        for (TransactionInput i : inputs) {
            i.setUTXO(NoobChainII.getUTXOs().get(i.getTransactionOutputId()));
        }

        //check if transaction is valid
        if (getInputsValue() < NoobChainII.getMinimumTransaction())  {
            log.atInfo().log("#Transaction Inputs too small: %s" + getInputsValue());
            return false;
        }

        //generate transaction ouputs
        float leftOver = getInputsValue() - value;
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.recipient, value, transactionId));
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

        //add outputs to Unspent list
        for (TransactionOutput o : outputs) {
            NoobChainII.getUTXOs().put(o.getId(), o);
        }

        //remove transaction inputs from UTXO lists as spent
        for (TransactionInput i : inputs) {
            if (i.getUTXO() == null) {
                continue;
            }
            NoobChainII.getUTXOs().remove(i.getUTXO().getId());
        }
        return true;
    }
}
