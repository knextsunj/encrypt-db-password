package mypackage;

import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.Name;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
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
        Object objectInstance = super.getObjectInstance(obj, name, nameCtx, environment);
        if (objectInstance != null) {
            BasicDataSource ds = (BasicDataSource) objectInstance;
            if (ds.getPassword() != null && ds.getPassword().length() > 0) {
                ds.setPassword(decrypt(ds.getPassword()));
            }
            return ds;
        } else {
            return null;
        }
    }

    public static String encrypt(String value) {
        /*
         * TODO Write a processing of returning an encrypted string
         * (StringUtils.reverse(value) is just reversing the given string)
         */
        return StringUtils.reverse(value);
    }

    public static String decrypt(String value) {
        /*
         * TODO Write a processing of returning an decrypted string
         * (StringUtils.reverse(value) is just reversing the given string)
         */
        return StringUtils.reverse(value);
    }
    
    // Example
    /*
    public static String encrypt(String source) {
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
    */
}
