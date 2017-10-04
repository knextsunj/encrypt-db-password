package mypackage;

import java.util.Base64;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory;

public class MyCustomBasicDataSourceFactory extends BasicDataSourceFactory {

    public static void main(String[] args) {
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
    }

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception {
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

    public static String encrypt(String source) {
        // TODO Remove the following code and write a processing of returning an encrypted string
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY.getBytes(), ALGORITHM));
            return new String(Base64.getEncoder().encode(cipher.doFinal(source.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to encrypt.";
        }
    }

    public static String decrypt(String encryptSource) {
        // TODO Remove the following code and write a processing of returning an decrypted string
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY.getBytes(), ALGORITHM));
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptSource.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to decrypt.";
        }
    }

    private static final String KEY = "change_this_key!";
    private static final String ALGORITHM = "AES";
    
    private static final String PROP_PASSWORD = "password"; // Don't change this value
}
