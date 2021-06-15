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
/**
 *     The below 2 methods are from pdfbox.
 *
 *     private DERObject createDERForRecipient(byte[] in, X509Certificate cert) ;
 *     private KeyTransRecipientInfo computeRecipientInfo(X509Certificate x509certificate, byte[] abyte0);
 *
 *     2006-11-22 Aiken Sam.
 */

/**
 * Copyright (c) 2003-2006, www.pdfbox.org
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of pdfbox; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://www.pdfbox.org
 *
 */

package com.itextpdf.text.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.EncryptedContentInfo;
import org.bouncycastle.asn1.cms.EnvelopedData;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.cms.KeyTransRecipientInfo;
import org.bouncycastle.asn1.cms.RecipientIdentifier;
import org.bouncycastle.asn1.cms.RecipientInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;

/**
 * @author Aiken Sam (aikensam@ieee.org)
 */
public class PdfPublicKeySecurityHandler {

    static final int SEED_LENGTH = 20;

    private ArrayList<PdfPublicKeyRecipient> recipients = null;

    private byte[] seed = new byte[SEED_LENGTH];

    public PdfPublicKeySecurityHandler() {
        KeyGenerator key;
        try {
            key = KeyGenerator.getInstance("AES");
            key.init(192, new SecureRandom());
            SecretKey sk = key.generateKey();
            System.arraycopy(sk.getEncoded(), 0, seed, 0, SEED_LENGTH); // create the 20 bytes seed
        } catch (NoSuchAlgorithmException e) {
            seed = SecureRandom.getSeed(SEED_LENGTH);
        }

        recipients = new ArrayList<PdfPublicKeyRecipient>();
    }

    public void addRecipient(PdfPublicKeyRecipient recipient) {
        recipients.add(recipient);
    }

    protected byte[] getSeed() {
        return seed.clone();
    }
    /*
    public PdfPublicKeyRecipient[] getRecipients() {
        recipients.toArray();
        return (PdfPublicKeyRecipient[])recipients.toArray();
    }*/

    public int getRecipientsSize() {
        return recipients.size();
    }

    public byte[] getEncodedRecipient(int index) throws IOException, GeneralSecurityException {
        //Certificate certificate = recipient.getX509();
        PdfPublicKeyRecipient recipient = recipients.get(index);
        byte[] cms = recipient.getCms();

        if (cms != null) return cms;

        Certificate certificate  = recipient.getCertificate();
        int permission =  recipient.getPermission();//PdfWriter.AllowCopy | PdfWriter.AllowPrinting | PdfWriter.AllowScreenReaders | PdfWriter.AllowAssembly;
        int revision = 3;

        permission |= revision==3 ? 0xfffff0c0 : 0xffffffc0;
        permission &= 0xfffffffc;
        permission += 1;

        byte[] pkcs7input = new byte[24];

        byte one = (byte)permission;
        byte two = (byte)(permission >> 8);
        byte three = (byte)(permission >> 16);
        byte four = (byte)(permission >> 24);

        System.arraycopy(seed, 0, pkcs7input, 0, 20); // put this seed in the pkcs7 input

        pkcs7input[20] = four;
        pkcs7input[21] = three;
        pkcs7input[22] = two;
        pkcs7input[23] = one;

        DERObject obj = createDERForRecipient(pkcs7input, (X509Certificate)certificate);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        DEROutputStream k = new DEROutputStream(baos);

        k.writeObject(obj);

        cms = baos.toByteArray();

        recipient.setCms(cms);

        return cms;
    }

    public PdfArray getEncodedRecipients() throws IOException,
                                         GeneralSecurityException {
        PdfArray EncodedRecipients = new PdfArray();
        byte[] cms = null;
        for (int i=0; i<recipients.size(); i++)
        try {
            cms = getEncodedRecipient(i);
            EncodedRecipients.add(new PdfLiteral(PdfContentByte.escapeString(cms)));
        } catch (GeneralSecurityException e) {
            EncodedRecipients = null;
        } catch (IOException e) {
            EncodedRecipients = null;
        }

        return EncodedRecipients;
    }

    private DERObject createDERForRecipient(byte[] in, X509Certificate cert)
        throws IOException,
               GeneralSecurityException
    {

        String s = "1.2.840.113549.3.2";

        AlgorithmParameterGenerator algorithmparametergenerator = AlgorithmParameterGenerator.getInstance(s);
        AlgorithmParameters algorithmparameters = algorithmparametergenerator.generateParameters();
        ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(algorithmparameters.getEncoded("ASN.1"));
        ASN1InputStream asn1inputstream = new ASN1InputStream(bytearrayinputstream);
        DERObject derobject = asn1inputstream.readObject();
        KeyGenerator keygenerator = KeyGenerator.getInstance(s);
        keygenerator.init(128);
        SecretKey secretkey = keygenerator.generateKey();
        Cipher cipher = Cipher.getInstance(s);
        cipher.init(1, secretkey, algorithmparameters);
        byte[] abyte1 = cipher.doFinal(in);
        DEROctetString deroctetstring = new DEROctetString(abyte1);
        KeyTransRecipientInfo keytransrecipientinfo = computeRecipientInfo(cert, secretkey.getEncoded());
        DERSet derset = new DERSet(new RecipientInfo(keytransrecipientinfo));
        AlgorithmIdentifier algorithmidentifier = new AlgorithmIdentifier(new DERObjectIdentifier(s), derobject);
        EncryptedContentInfo encryptedcontentinfo =
            new EncryptedContentInfo(PKCSObjectIdentifiers.data, algorithmidentifier, deroctetstring);
        EnvelopedData env = new EnvelopedData(null, derset, encryptedcontentinfo, null);
        ContentInfo contentinfo =
            new ContentInfo(PKCSObjectIdentifiers.envelopedData, env);
        return contentinfo.getDERObject();
    }

    private KeyTransRecipientInfo computeRecipientInfo(X509Certificate x509certificate, byte[] abyte0)
        throws GeneralSecurityException, IOException
    {
        ASN1InputStream asn1inputstream =
            new ASN1InputStream(new ByteArrayInputStream(x509certificate.getTBSCertificate()));
        TBSCertificateStructure tbscertificatestructure =
            TBSCertificateStructure.getInstance(asn1inputstream.readObject());
        AlgorithmIdentifier algorithmidentifier = tbscertificatestructure.getSubjectPublicKeyInfo().getAlgorithmId();
        IssuerAndSerialNumber issuerandserialnumber =
            new IssuerAndSerialNumber(
                tbscertificatestructure.getIssuer(),
                tbscertificatestructure.getSerialNumber().getValue());
        Cipher cipher = Cipher.getInstance(algorithmidentifier.getObjectId().getId());
        cipher.init(1, x509certificate);
        DEROctetString deroctetstring = new DEROctetString(cipher.doFinal(abyte0));
        RecipientIdentifier recipId = new RecipientIdentifier(issuerandserialnumber);
        return new KeyTransRecipientInfo( recipId, algorithmidentifier, deroctetstring);
    }
}
