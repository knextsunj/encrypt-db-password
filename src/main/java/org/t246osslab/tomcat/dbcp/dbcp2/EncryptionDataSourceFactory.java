package org.t246osslab.tomcat.dbcp.dbcp2;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory;

public class EncryptionDataSourceFactory extends BasicDataSourceFactory {

    private static final Log log = LogFactory.getLog(EncryptionDataSourceFactory.class);

    public static void main(String[] args) {
        try {
            if (args.length == 1) {
                System.out.println(encrypt(args[0]));
            } else if (args.length == 2 && "-e".equals(args[0])) {
                System.out.println(encrypt(args[1]));
            } else if (args.length == 2 && "-d".equals(args[0])) {
                System.out.println(decrypt(args[1]));
            } else {
                ResourceBundle bundle = ResourceBundle.getBundle("messages");
                String usageDescription = bundle.getString("usage.description");
                System.out.println(usageDescription);
            }
        } catch (Exception e) {
            System.out.println("Failed to encrypt or decrypt.");
            e.printStackTrace();
            
        }
    }

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception {
        try {
            if (obj instanceof Reference) {
                Reference ref = (Reference) obj;
                StringRefAddr passwordRefAddr = (StringRefAddr) ref.get(PROP_PASSWORD);
                if (passwordRefAddr != null) {
                    String encryptedPwd = (String) passwordRefAddr.getContent();
                    String cleartextPwd = decrypt(encryptedPwd);
                    int index = find(PROP_PASSWORD, ref);
                    if (index >= 0) {
                        ref.remove(index);
                        ref.add(index, new StringRefAddr(PROP_PASSWORD, cleartextPwd));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to decrypt password. Please check DataSource definition.");
            throw e;
        }
        return super.getObjectInstance(obj, name, nameCtx, environment);
    }

    private int find(String addrType, Reference ref) throws Exception {
        Enumeration<RefAddr> enu = ref.getAll();
        for (int i = 0; enu.hasMoreElements(); i++) {
            RefAddr addr = (RefAddr) enu.nextElement();
            if (addr.getType().equals(addrType))
                return i;
        }
        return -1;
    }

    public static String encrypt(String source) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // TODO Remove the following code and write a processing of returning an encrypted string
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY.getBytes(), ALGORITHM));
        return new String(Base64.getEncoder().encode(cipher.doFinal(source.getBytes())));
    }

    public static String decrypt(String encryptSource) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // TODO Remove the following code and write a processing of returning an decrypted string
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY.getBytes(), ALGORITHM));
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptSource.getBytes())));
    }

    private static final String KEY = "change_this_key!";
    private static final String ALGORITHM = "AES";
    
    private static final String PROP_PASSWORD = "password"; // Don't change this value
}
