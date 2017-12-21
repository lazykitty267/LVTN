package bk.lvtn.Signature;

/**
 * Created by Long on 04/11/2017.
 */

import java.io.File;
import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;

import java.security.KeyPairGenerator;

import java.security.KeyStore;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;

import java.security.PrivateKey;

import java.security.PublicKey;

import java.security.SecureRandom;


import android.content.Context;
import android.support.v7.app.ActionBarActivity;

import android.os.Bundle;

import android.view.Menu;

import android.view.MenuItem;

import javax.crypto.Cipher;


import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import android.util.Base64;


public class DigitalSignature {

    /** Called when the activity is first created. */

    public DigitalSignature(){

    }

//    public void onCreate(Bundle savedInstanceState) {
//
//
//
//        try {
//
//            generateKey();
//
//        } catch (Exception e1) {
//
//            // TODO Auto-generated catch block
//
//            e1.printStackTrace();
//
//        }
//
//
//
//        super.onCreate(savedInstanceState);
//
//
//
//        setContentView(R.layout.main);
//
//        final ImageButton exam = (ImageButton) findViewById(R.id.ibutton);
//
//
//
//        final EditText oritext = (EditText) findViewById(R.id.oritext);
//
//        final Button sent = (Button) findViewById(R.id.sendbutton);
//
//        final EditText ctext = (EditText) findViewById(R.id.changabletext);
//
//        final Button cont = (Button) findViewById(R.id.continuebutton);
//
//        final Button next = (Button) findViewById(R.id.stepbutton);
//
//        final TextView stext = (TextView) findViewById(R.id.steptext);
//
//        final LinearLayout main = (LinearLayout) findViewById(R.id.linearLayout1);
//
//        final LinearLayout sender = (LinearLayout) findViewById(R.id.linearLayout2);
//
//        final LinearLayout hacker = (LinearLayout) findViewById(R.id.linearLayout3);
//
//        final LinearLayout reciever = (LinearLayout) findViewById(R.id.linearLayout4);
//
//
//
//        exam.setOnClickListener(new OnClickListener() {
//
//            public void onClick(View v) {
//
//                main.setVisibility(View.GONE);
//
//                sender.setVisibility(View.VISIBLE);
//
//
//
//            };
//
//        });
//
//
//
//        sent.setOnClickListener(new OnClickListener() {
//
//            public void onClick(View v) {
//
//                sender.setVisibility(View.GONE);
//
//                hacker.setVisibility(View.VISIBLE);
//
//                ctext.setText(oritext.getText().toString());
//
//
//
//            };
//
//        });
//
//
//
//        cont.setOnClickListener(new OnClickListener() {
//
//            public void onClick(View v) {
//
//                hacker.setVisibility(View.GONE);
//
//                reciever.setVisibility(View.VISIBLE);
//
//                stext.setText("This is the text from you though the hacker  \n"
//
//                        + ctext.getText().toString());
//
//                stepcount = 0;
//
//            };
//
//        });
//
//        next.setOnClickListener(new OnClickListener() {
//
//            public void onClick(View v) {
//
//                if (stepcount == 0) {
//
//                    stext.setText("Here is the public key \n" + uk.toString());
//
//                    stepcount++;
//
//
//
//                }
//
//
//
//                else if (stepcount == 1) {
//
//                    try {
//
//                        stext.setText("Jack can use the public key to decrypt the hashcode \n"
//
//                                + decrypt(encrypt(myhash(oritext
//
//                                .getEditableText().toString()
//
//                                .getBytes()))));
//
//                    } catch (NoSuchAlgorithmException e) {
//
//                        // TODO Auto-generated catch block
//
//                        e.printStackTrace();
//
//                    }
//
//                    stepcount++;
//
//                }
//
//
//
//                else if (stepcount == 2) {
//
//                    try {
//
//                        stext.setText("Then Jack can process the recieved text by the hashfunction\n"
//
//                                + myhash(ctext.getEditableText().toString()
//
//                                .getBytes()));
//
//                    } catch (NoSuchAlgorithmException e) {
//
//                        // TODO Auto-generated catch block
//
//                        e.printStackTrace();
//
//                    }
//
//                    stepcount++;
//
//                }
//
//
//
//                else if (stepcount == 3) {
//
//                    try {
//
//                        stext.setText("The last step, compare two hashcodes, if they are the same, it is a complete text from you, if not
//
//                                there must be a hacker to change it.    \n Origin Text's hashcode: \n"
//
//                                + myhash(oritext.getEditableText().toString()
//
//                                .getBytes())
//
//                                + "   \n =?    \n Recieve Text's hashcod:\n "
//
//                                + myhash(ctext.getEditableText().toString()
//
//                                .getBytes()));
//
//                    } catch (NoSuchAlgorithmException e) {
//
//                        // TODO Auto-generated catch block
//
//                        e.printStackTrace();
//
//                    }
//
//                    stepcount = 0;
//
//                }
//
//
//
//            };
//
//        });
//
//
//
//    }



    private final static String RSA = "RSA";

    public PublicKey uk;

    public PrivateKey rk;

    public static int stepcount = 0;

    public void generateKey(Context context,String username) throws Exception {

        KeyPairGenerator gen = KeyPairGenerator.getInstance(RSA);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        gen.initialize(1024, random);

        KeyPair keyPair = gen.generateKeyPair();

        uk = keyPair.getPublic();

        rk = keyPair.getPrivate();

        final File keyFileDirectory = new File(context.getFilesDir(), "rsa/");
        final File publicKeyFile = new File(keyFileDirectory, "sikkr_pub_key");
        final File privateKeyFile = new File(keyFileDirectory, username + "_priv_key");
        if (publicKeyFile.exists()) {
            publicKeyFile.delete();
        }

        if (privateKeyFile.exists()) {
            privateKeyFile.delete();
        }

        if (!publicKeyFile.getParentFile().exists()) {
            publicKeyFile.getParentFile().mkdirs();
        }

        publicKeyFile.createNewFile();
        privateKeyFile.createNewFile();
        saveByteDataToFile(publicKeyFile, keyPair.getPublic().getEncoded());
        saveByteDataToFile(privateKeyFile, keyPair.getPrivate().getEncoded());
    }



    public final String myhash(byte[] by) throws NoSuchAlgorithmException {

        byte[] output1;

        // MD5



        MessageDigest md5 = MessageDigest.getInstance("MD5");



        md5.reset();



        md5.update(by);



        output1 = md5.digest();



        // create hex output



        StringBuffer hexString1 = new StringBuffer();



        for (int i = 0; i < md5.digest().length; i++)



            hexString1.append(Integer.toHexString(0xFF & output1[i]));



        return hexString1.toString();



    }

    public String byteToHex(byte[] b) throws IOException {
        StringBuffer hexString1 = new StringBuffer();



        for (int i = 0; i < b.length; i++)

            hexString1.append(Integer.toHexString(0xFF & b[i]));



        return hexString1.toString();
    }

    public String fileToHex(File file) throws IOException {
        return byteToHex(readByteDataFromFile(file));
    }
    public byte[] rsaSign (byte[] data,PrivateKey priRSA) {

        byte[] cipherData = null;

        try {

            Signature s = Signature.getInstance("SHA1withRSA");
            s.initSign(priRSA);

            s.update(data);
            byte[] signature = s.sign();
            String hexSign = byteToHex(signature);
            return signature;


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cipherData;
    }

    public boolean rsaVerify (byte[] data, byte[] signature,PublicKey pubRSA) {

        boolean success = false;

        try {

            Signature s = Signature.getInstance("SHA1withRSA");
            s.initVerify(pubRSA);

            s.update(data);

            success = s.verify(signature);

//            if(success == true) {
//                Log.i("yeay", "yay");
//            }
//            else {
//                Log.i("nay", "nay");
//            }

            return success;


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        return false;
    }



    public byte[] encrypt(String text, PrivateKey priRSA)

            throws Exception {

        Cipher cipher = Cipher.getInstance(RSA);

        cipher.init(Cipher.ENCRYPT_MODE, priRSA);


//        File f = new File("");
//        FileUtils.readFileToByteArray(f);


        return cipher.doFinal(text.getBytes());

    }



    public final byte[] encrypt(String text, String key) {

        try {

//            PrivateKey rk = getPrivateKey(key);
            //return encrypt(text.getBytes(), rk);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }



    public final byte[] decrypt(File file,PublicKey key) {

        try {
			byte[] b = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);

            //PublicKey uk = getPublicKey(key);
            return decrypt(b,key);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }



    private byte[] decrypt(byte[] src,PublicKey pubRSA) throws Exception {

        Cipher cipher = Cipher.getInstance(RSA);

        cipher.init(Cipher.DECRYPT_MODE, pubRSA);

        return cipher.doFinal(src);

    }


    public PrivateKey getPrivateKey(File privateKeyFile) throws Exception{
//        StringBuilder pkcs8Lines = new StringBuilder();
//        BufferedReader rdr = new BufferedReader(new StringReader(key));
//        String line;
//        while ((line = rdr.readLine()) != null) {
//            pkcs8Lines.append(line);
//        }

        // Remove any whitespace
//
//        String pkcs8Pem = pkcs8Lines.toString();
//        pkcs8Pem = pkcs8Pem.replaceAll("\\s+","");
//
//        // Base64 decode the result
//
//        byte [] pkcs8EncodedBytes = Base64.decode(pkcs8Pem, Base64.DEFAULT);

        // extract the private key

        byte[] privateKey = readByteDataFromFile(privateKeyFile);


        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privKey = kf.generatePrivate(keySpec);
        return privKey;
    }

    public PublicKey getPublicKey(File publicKeyFile) throws Exception{
//        StringBuilder x509Lines = new StringBuilder();
//        BufferedReader rdr = new BufferedReader(new StringReader(key));
//        String line;
//        while ((line = rdr.readLine()) != null) {
//            x509Lines.append(line);
//        }
//
//        // Remove any whitespace
//
//        String pkcs8Pem = x509Lines.toString();
//        pkcs8Pem = pkcs8Pem.replaceAll("\\s+","");
//
//        // Base64 decode the result
//
//        byte [] x509EncodedBytes = Base64.decode(pkcs8Pem, Base64.DEFAULT);

        // extract the private key

        byte[] publicKey = readByteDataFromFile(publicKeyFile);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pubKey = kf.generatePublic(keySpec);
        return pubKey;
    }

    private static void saveByteDataToFile(File file, byte[] data) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
        dos.write(data);
        dos.flush();
        dos.close();
    }

    private static byte[] readByteDataFromFile(File file) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        byte[] read = new byte[dis.available()];
        dis.readFully(read);
        dis.close();
        return read;
    }
//
//    public static String byte2hex(byte[] b) {
//
//        String hs = "";
//
//        String stmp = "";
//
//        for (int n = 0; n < b.length; n++) {
//
//            stmp = Integer.toHexString(b[n] & 0xFF);
//
//            if (stmp.length() == 1)
//
//                hs += ("0" + stmp);
//
//            else
//
//                hs += stmp;
//
//        }
//
//        return hs.toUpperCase();
//
//    }
//
//
//
//    public static byte[] hex2byte(byte[] b) {
//
//        if ((b.length % 2) != 0)
//
//            throw new IllegalArgumentException("hello");
//
//
//
//        byte[] b2 = new byte[b.length / 2];
//
//
//
//        for (int n = 0; n < b.length; n += 2) {
//
//            String item = new String(b, n, 2);
//
//            b2[n / 2] = (byte) Integer.parseInt(item, 16);
//
//        }
//
//        return b2;
//
//    }

}
