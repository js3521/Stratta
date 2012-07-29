package org.stratta.security;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public final class Password implements Serializable {

    private static final long serialVersionUID = 1L;
    private transient char[] _password;
    private final String _ciphertext;

    public Password(char[] password, boolean store) {
        if (password == null || password.length == 0) {
            _password = null;
            _ciphertext = null;
        } else {
            _password = password.clone();
            _ciphertext = (store) ? encrypt() : null;

            Arrays.fill(password, '\0');
        }

    }

    public Password() {
        this(null, false);
    }

    public boolean isBlank() {
        return (_password == null);
    }

    public char[] getPassword() {
        return (_password != null) ? _password.clone() : null;
    }
    
    @Override
    public String toString() {
        return "Password";
    }

    private String encrypt() {
        String key = getMacAddress();

        if (key != null) {
            return buildEncryptor(key).encrypt(new String(_password));
        } else {
            return null;
        }
    }

    private char[] decrypt() {
        String key = getMacAddress();

        if (key != null) {
            return buildEncryptor(key).decrypt(_ciphertext).toCharArray();
        } else {
            return null;
        }
    }

    private StandardPBEStringEncryptor buildEncryptor(String key) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);

        return encryptor;
    }

    private String getMacAddress() {
        byte[] mac;

        try {
            NetworkInterface network = NetworkInterface
                    .getByInetAddress(InetAddress.getLocalHost());

            mac = network.getHardwareAddress();
        } catch (IOException e) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < mac.length; i++) {
            builder.append(String.format("%02X", mac[i]));
        }

        return builder.toString();
    }

    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();

        if (_ciphertext != null) {
            _password = decrypt().clone();
        }
    }
}
