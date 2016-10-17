package com.microsoft.azure.documentdb.sample.model;

/**
 * Created by vazvadsk on 2016-10-12.
 */
public class CertItem {
    private String ret;

    private String issuer;
    private String subject;
    private String thumbprint;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getThumbprint() {
        return thumbprint;
    }

    public void setThumbprint(String thumbprint) {
        this.thumbprint = thumbprint;
    }
}
