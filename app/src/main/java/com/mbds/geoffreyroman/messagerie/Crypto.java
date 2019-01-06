package com.mbds.geoffreyroman.messagerie;;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Crypto {
    String provider = "AndroidKeyStore";
    String transformation = "RSA/ECB/PKCS1Padding";
    KeyPair kp;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Crypto(String alias) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, UnsupportedEncodingException {
        generateKey(alias);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void generateKey(String alias) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", provider);
        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(alias,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1);
        kpg.initialize(builder.build());
        kp = kpg.genKeyPair();
    }


        @RequiresApi(api = Build.VERSION_CODES.M)
    public PublicKey getPublicKey(String alias) throws CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableEntryException, NoSuchProviderException, InvalidAlgorithmParameterException, KeyStoreException {
        KeyStore keyStore = KeyStore.getInstance(provider);
        keyStore.load(null);
        KeyStore.Entry entry = null;
        PublicKey publicKey = null;
        if(keyStore.containsAlias(alias)) {
            publicKey = keyStore.getCertificate(alias).getPublicKey();
        }
        else{
            kp.getPublic();
        }

        return publicKey;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public PrivateKey getPrivatKey(String alias) throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, UnrecoverableEntryException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyStore keyStore = KeyStore.getInstance(provider);
        keyStore.load(null);
        KeyStore.Entry entry = null;

        try {

            if (keyStore.containsAlias(alias)) {
                entry = keyStore.getEntry(alias, null);

            } else {
                generateKey(alias);
                entry = keyStore.getEntry(alias, null);
            }
        }
         catch (KeyStoreException e) {

        } catch (UnrecoverableEntryException e) {

        }

        PrivateKey privateKey = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
        return privateKey;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public byte[] crypte(String str, String contact) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, CertificateException, InvalidAlgorithmParameterException, KeyStoreException, NoSuchProviderException, UnrecoverableEntryException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(contact));
        byte[] encryptedBytes = cipher.doFinal(str.getBytes());

        return encryptedBytes;
    }


    public String decrypte(byte[] str, String Contact) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        byte[] encryptedBytes = str;
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, kp.getPrivate());
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decrypted = new String(decryptedBytes);

        return decrypted;
    }



}
