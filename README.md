# Encrypting DB password in Tomcat

This project can avoid storing a clear text password in Tomcat's Resource definition of a DataSource.

:warning: Note that this is a "security by obscurity" solution.

## How to use:

1. `git clone https://github.com/k-tamura/encrypt-db-password.git`
2. Write `encrypt()` & `decrypt()` methods in `mypackage.MyCustomBasicDataSourceFactory`:

```java
    public static String encrypt(String value) {
        /*
         * TODO Implement a processing of returning an encrypted string
         * (StringUtils.reverse(value) is just reversing the given value)
         */
        return StringUtils.reverse(value);
    }

    public static String decrypt(String value) {
        /*
         * TODO Implement a processing of returning an decrypted string
         * (StringUtils.reverse(value) is just reversing the given value)
         */
        return StringUtils.reverse(value);
    }
```
3. `mvn clean package`
4. `java -jar target/encrypt-db-password-1.0.0-jar-with-dependencies.jar -e [claer text password]`
5. Edit a data source configuration in a file like `server.xml` as follows:
```xml
<Resource auth="Container" type="javax.sql.DataSource"
factory="mypackage.MyCustomBasicDataSourceFactory" 
password="[Encrypted Password]"
```
6. `cp target/encrypt-db-password-1.0.0.jar $CATALINA_HOME/lib/`
7. Start Tomcat

## See also:

- [Stack Overflow - How to avoid storing passwords in the clear for tomcat's server.xml Resource definition of a DataSource?](https://stackoverflow.com/questions/129160/how-to-avoid-storing-passwords-in-the-clear-for-tomcats-server-xml-resource-def)
- [Tomcat Wiki - Why are plain text passwords in the config files?](https://wiki.apache.org/tomcat/FAQ/Password)
