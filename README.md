# Encryption DataSource Factory in Tomcat DBCP 2

This factory can avoid storing a clear text password in Tomcat's Resource definition of a DataSource.

:warning: Note that this is a "security by obscurity" solution.

## How to use:

1. `git clone https://github.com/k-tamura/encrypt-db-password.git`
2. Write `encrypt()` & `decrypt()` methods in `org.t246osslab.tomcat.dbcp.dbcp2.EncryptionDataSourceFactory`:

```java
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
```
3. `mvn clean package`
4. `java -jar target/encrypt-db-password-1.0.0-jar-with-dependencies.jar -e [claer text password]`
5. Edit a data source configuration in a file like `server.xml` as follows:
```xml
<Resource auth="Container" type="javax.sql.DataSource"
factory="org.t246osslab.tomcat.dbcp.dbcp2.EncryptionDataSourceFactory" 
password="[Encrypted Password]"
```
6. `cp target/encrypt-db-password-1.0.0.jar $CATALINA_HOME/lib/`
7. Start Tomcat

## See also:

- [Stack Overflow - How to avoid storing passwords in the clear for tomcat's server.xml Resource definition of a DataSource?](https://stackoverflow.com/questions/129160/how-to-avoid-storing-passwords-in-the-clear-for-tomcats-server-xml-resource-def)
- [Tomcat Wiki - Why are plain text passwords in the config files?](https://wiki.apache.org/tomcat/FAQ/Password)
