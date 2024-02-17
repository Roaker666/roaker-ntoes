package com.roaker.notes.uc.utils;

import cn.hutool.core.io.IoUtil;
import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.config.TinkConfig;
import com.google.crypto.tink.hybrid.HybridDecryptFactory;
import com.google.crypto.tink.hybrid.HybridEncryptFactory;
import com.google.crypto.tink.proto.KeyTemplate;
import org.springframework.util.Base64Utils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 模拟加密硬件的加密机加密
 *
 * @author lei.rao
 * @since 1.0
 */
public class TinkUtils {
    public static final String DATA_ENCRYPT_FORMAT = "%s_%d_%s";
    public static final String SEPARATOR_ENCRYPT = "_";
    public static final String SP_ENCRYPT_FORMAT = "%s,%d,%s";
    public static final String SP_SEPARATOR_ENCRYPT = ",";

    static {
        try {
            TinkConfig.register();
        } catch (Exception ignore) {
        }
    }

    public static String encryptByAead(Long id, String secretData, String plainText, String contextData) {
        try {
            KeysetHandle keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withString(secretData));
            Aead aead = AeadFactory.getPrimitive(keysetHandle);
            String encrypt = Base64Utils.encodeToString(aead.encrypt(plainText.getBytes(StandardCharsets.UTF_8), contextData.getBytes()));
            return String.format(DATA_ENCRYPT_FORMAT, encrypt, id, Base64Utils.encodeToString(contextData.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptByAead(String secretData, String cipherText, String contextData) {
        try {
            KeysetHandle keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withString(secretData));
            Aead aead = AeadFactory.getPrimitive(keysetHandle);
            return new String(aead.decrypt(Base64Utils.decodeFromString(cipherText), Base64Utils.decodeFromString(contextData)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String encryptByHybrid(Long id, String publicKey, String plainText, String contextData) {
        try {
            KeysetHandle keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withString(publicKey));
            HybridEncrypt hybridEncrypt = HybridEncryptFactory.getPrimitive(keysetHandle);
            String encrypt = Base64Utils.encodeToString(hybridEncrypt.encrypt(plainText.getBytes(StandardCharsets.UTF_8), contextData.getBytes()));
            return String.format(SP_ENCRYPT_FORMAT, encrypt, id, contextData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String decryptByHybrid(String privateKey, String cipherText, String contextData) {
        try {
            KeysetHandle keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withString(privateKey));
            HybridDecrypt hybridDecrypt = HybridDecryptFactory.getPrimitive(keysetHandle);
            return new String(hybridDecrypt.decrypt(Base64Utils.decodeFromString(cipherText), contextData.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getSecretKey(final KeyTemplate keyTemplate) throws Exception {
        KeysetHandle keysetHandle = KeysetHandle.generateNew(keyTemplate);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withOutputStream(out));
            return Base64Utils.encodeToString(IoUtil.toStr(out, StandardCharsets.UTF_8).getBytes());
        }
    }


    public static String[] getKeys(final KeyTemplate keyTemplate) throws Exception {
        String[] result = new String[2];
        KeysetHandle privateKey = KeysetHandle.generateNew(keyTemplate);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayOutputStream out2 = new ByteArrayOutputStream()) {
            CleartextKeysetHandle.write(privateKey, JsonKeysetWriter.withOutputStream(out));
            result[0] = Base64Utils.encodeToString(IoUtil.toStr(out, StandardCharsets.UTF_8).getBytes());
            CleartextKeysetHandle.write(privateKey.getPublicKeysetHandle(), JsonKeysetWriter.withOutputStream(out2));
            result[1] = Base64Utils.encodeToString(IoUtil.toStr(out2, StandardCharsets.UTF_8).getBytes());
        }
        return result;
    }

}
