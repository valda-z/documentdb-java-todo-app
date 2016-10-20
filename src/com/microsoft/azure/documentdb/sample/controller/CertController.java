package com.microsoft.azure.documentdb.sample.controller;

import com.microsoft.azure.documentdb.sample.comm.GeoIPClient;
import com.microsoft.azure.documentdb.sample.model.CertItem;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by vazvadsk on 2016-10-17.
 */
public class CertController {
    protected static String getThumbPrint(X509Certificate cert)
            throws NoSuchAlgorithmException, CertificateEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] der = cert.getEncoded();
        md.update(der);
        byte[] digest = md.digest();
        return hexify(digest);

    }

    protected static String hexify (byte bytes[]) {

        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        StringBuffer buf = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; ++i) {
            buf.append(hexDigits[(bytes[i] & 0xf0) >> 4]);
            buf.append(hexDigits[bytes[i] & 0x0f]);
        }

        return buf.toString();
    }

    public CertItem parse(String data, String ip){
        CertItem itm = new CertItem();
        String ret = "N/A";
        {
            if(data != null){
                ret = data;
                try {
                    X509Certificate publicKey = (X509Certificate) CertificateFactory
                            .getInstance("X.509")
                            .generateCertificate(new ByteArrayInputStream(Base64.decodeBase64(ret)));

                    // parse certificate to readable form
                    ret = "Subject: " + publicKey.getSubjectDN().getName() +
                            " | Issuer: " + publicKey.getIssuerDN().getName() +
                            " | Thumbprint: " + getThumbPrint(publicKey);

                    // parse certificate properties
                    itm.setIssuer(publicKey.getIssuerDN().getName());
                    itm.setSubject(publicKey.getSubjectDN().getName());
                    itm.setThumbprint(getThumbPrint(publicKey));
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                //
            }
        }
        ret += " # " + new GeoIPClient().query(ip);
        itm.setRet(ret);
        return itm;
    }
}
