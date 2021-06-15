/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.itextpdf.text.pdf;

import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.cert.CRL;
import java.security.cert.Certificate;

import com.itextpdf.text.ExceptionConverter;

/**
 * A signature dictionary representation for the standard filters.
 */
public abstract class PdfSigGenericPKCS extends PdfSignature {
    /**
     * The hash algorithm, for example "SHA1"
     */    
    protected String hashAlgorithm;
    /**
     * The crypto provider
     */    
    protected String provider = null;
    /**
     * The class instance that calculates the PKCS#1 and PKCS#7
     */    
    protected PdfPKCS7 pkcs;
    /**
     * The subject name in the signing certificate (the element "CN")
     */    
    protected String   name;

    private byte externalDigest[];
    private byte externalRSAdata[];
    private String digestEncryptionAlgorithm;

    /**
     * Creates a generic standard filter.
     * @param filter the filter name
     * @param subFilter the sub-filter name
     */    
    public PdfSigGenericPKCS(PdfName filter, PdfName subFilter) {
        super(filter, subFilter);
    }

    /**
     * Sets the crypto information to sign.
     * @param privKey the private key
     * @param certChain the certificate chain
     * @param crlList the certificate revocation list. It can be <CODE>null</CODE>
     */    
    public void setSignInfo(PrivateKey privKey, Certificate[] certChain, CRL[] crlList) {
        try {
            pkcs = new PdfPKCS7(privKey, certChain, crlList, hashAlgorithm, provider, PdfName.ADBE_PKCS7_SHA1.equals(get(PdfName.SUBFILTER)));
            pkcs.setExternalDigest(externalDigest, externalRSAdata, digestEncryptionAlgorithm);
            if (PdfName.ADBE_X509_RSA_SHA1.equals(get(PdfName.SUBFILTER))) {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                for (int k = 0; k < certChain.length; ++k) {
                    bout.write(certChain[k].getEncoded());
                }
                bout.close();
                setCert(bout.toByteArray());
                setContents(pkcs.getEncodedPKCS1());
            }
            else
                setContents(pkcs.getEncodedPKCS7());
            name = PdfPKCS7.getSubjectFields(pkcs.getSigningCertificate()).getField("CN");
            if (name != null)
                put(PdfName.NAME, new PdfString(name, PdfObject.TEXT_UNICODE));
            pkcs = new PdfPKCS7(privKey, certChain, crlList, hashAlgorithm, provider, PdfName.ADBE_PKCS7_SHA1.equals(get(PdfName.SUBFILTER)));
            pkcs.setExternalDigest(externalDigest, externalRSAdata, digestEncryptionAlgorithm);
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    /**
     * Sets the digest/signature to an external calculated value.
     * @param digest the digest. This is the actual signature
     * @param RSAdata the extra data that goes into the data tag in PKCS#7
     * @param digestEncryptionAlgorithm the encryption algorithm. It may must be <CODE>null</CODE> if the <CODE>digest</CODE>
     * is also <CODE>null</CODE>. If the <CODE>digest</CODE> is not <CODE>null</CODE>
     * then it may be "RSA" or "DSA"
     */    
    public void setExternalDigest(byte digest[], byte RSAdata[], String digestEncryptionAlgorithm) {
        externalDigest = digest;
        externalRSAdata = RSAdata;
        this.digestEncryptionAlgorithm = digestEncryptionAlgorithm;
    }

    /**
     * Gets the subject name in the signing certificate (the element "CN")
     * @return the subject name in the signing certificate (the element "CN")
     */    
    public String getName() {
        return name;
    }

    /**
     * Gets the class instance that does the actual signing.
     * @return the class instance that does the actual signing
     */    
    public PdfPKCS7 getSigner() {
        return pkcs;
    }

    /**
     * Gets the signature content. This can be a PKCS#1 or a PKCS#7. It corresponds to
     * the /Contents key.
     * @return the signature content
     */    
    public byte[] getSignerContents() {
        if (PdfName.ADBE_X509_RSA_SHA1.equals(get(PdfName.SUBFILTER)))
            return pkcs.getEncodedPKCS1();
        else
            return pkcs.getEncodedPKCS7();
    }

    /**
     * Creates a standard filter of the type VeriSign.
     */    
    public static class VeriSign extends PdfSigGenericPKCS {
        /**
         * The constructor for the default provider.
         */        
        public VeriSign() {
            super(PdfName.VERISIGN_PPKVS, PdfName.ADBE_PKCS7_DETACHED);
            hashAlgorithm = "MD5";
            put(PdfName.R, new PdfNumber(65537));
        }

        /**
         * The constructor for an explicit provider.
         * @param provider the crypto provider
         */        
        public VeriSign(String provider) {
            this();
            this.provider = provider;
        }
    }

    /**
     * Creates a standard filter of the type self signed.
     */    
    public static class PPKLite extends PdfSigGenericPKCS {
        /**
         * The constructor for the default provider.
         */        
        public PPKLite() {
            super(PdfName.ADOBE_PPKLITE, PdfName.ADBE_X509_RSA_SHA1);
            hashAlgorithm = "SHA1";
            put(PdfName.R, new PdfNumber(65541));
        }

        /**
         * The constructor for an explicit provider.
         * @param provider the crypto provider
         */        
        public PPKLite(String provider) {
            this();
            this.provider = provider;
        }
    }

    /**
     * Creates a standard filter of the type Windows Certificate.
     */    
    public static class PPKMS extends PdfSigGenericPKCS {
        /**
         * The constructor for the default provider.
         */        
        public PPKMS() {
            super(PdfName.ADOBE_PPKMS, PdfName.ADBE_PKCS7_SHA1);
            hashAlgorithm = "SHA1";
        }

        /**
         * The constructor for an explicit provider.
         * @param provider the crypto provider
         */        
        public PPKMS(String provider) {
            this();
            this.provider = provider;
        }
    }
}
