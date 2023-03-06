package com.roaker.notes;

import lombok.Data;

import java.security.PublicKey;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class TransactionOutput {
    private String id;
    /**
     * also known as the new owner of these coins
     */
    private PublicKey recipient;
    private float value;
    /**
     * the id of the transaction this output was created in
     */
    private String parentTransactionId;

    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = Block.applySha256(Block.getStringFromKey(recipient) + Float.toString(value) + parentTransactionId);
    }

    /**
     * check if coin belongs to you
     */
    public boolean isMine(PublicKey publicKey) {
        return publicKey == recipient;
    }
}
