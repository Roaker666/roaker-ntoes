package com.roaker.notes;

import lombok.Data;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class TransactionInput {
    /**
     * Reference to TransactionOutputs -> transactionId
     */
    private String transactionOutputId;
    /**
     * Contains the Unspent transaction output
     */
    private TransactionOutput UTXO;
    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}
