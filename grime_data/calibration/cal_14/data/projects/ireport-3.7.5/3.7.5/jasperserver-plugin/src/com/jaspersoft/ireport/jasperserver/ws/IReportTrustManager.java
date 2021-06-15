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
package com.jaspersoft.ireport.jasperserver.ws;

import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.net.ssl.X509TrustManager;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @version $Id: IReportTrustManager.java 0 2010-07-19 19:40:50 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class IReportTrustManager implements X509TrustManager {

        public static final String TRUSTED_CERTIFICATE_FINGERPRINTS = "trustedCertificateFingerprints";
        private List<String>temporaryCertificates = new ArrayList<String>();
        private List<String>trustedCertificates = new ArrayList<String>();

        private boolean storingFingerprints = false;

	public IReportTrustManager() {

            reloadTrustedCertificates();

            JasperServerManager.getPreferences().addPreferenceChangeListener(new PreferenceChangeListener() {
                public void preferenceChange(PreferenceChangeEvent evt) {
                        if (!isStoringFingerprints() && evt.getKey().equals(TRUSTED_CERTIFICATE_FINGERPRINTS))
                        {
                            reloadTrustedCertificates();
                        }
                    }
                });

            
	}


        private void reloadTrustedCertificates() {
            trustedCertificates.clear();
            temporaryCertificates.clear();
            String trustedSites = JasperServerManager.getPreferences().get(TRUSTED_CERTIFICATE_FINGERPRINTS, "");
            String[] fingerprints = trustedSites.split(":");
            trustedCertificates.addAll( Arrays.asList(fingerprints));
        }

        private void saveCertificates()
        {
            
            String trustedCertificateFingerprints = "";
            for (String s : trustedCertificates)
            {
                trustedCertificateFingerprints += s + ":";
            }

            setStoringFingerprints(true);
            JasperServerManager.getPreferences().put(TRUSTED_CERTIFICATE_FINGERPRINTS, trustedCertificateFingerprints);
            setStoringFingerprints(false);
        }


	public X509Certificate[] getAcceptedIssuers() {
	   return new X509Certificate[]{};
	}

	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
 
                // Check the first certificate...
                if (chain == null || chain.length == 0)
                {
                    throw new CertificateException("No certificate sent by the server");
                }

                 MessageDigest sha1;
                 try {
                    sha1 = MessageDigest.getInstance("SHA1");
                 } catch (NoSuchAlgorithmException ex) {
                    throw new CertificateException("Unable to instance the SHA1 Algorithm!");
                 }

                 MessageDigest md5;
                 try {
                    md5 = MessageDigest.getInstance("MD5");
                 } catch (NoSuchAlgorithmException ex) {
                    throw new CertificateException("Unable to instance the MD5 Algorithm!");
                 }

                 final StringBuffer certsText = new StringBuffer();

                 List<String> currentCertificates = new ArrayList<String>();

                for (int i = 0; i < chain.length; i++) {
                    X509Certificate cert = chain[i];

                    String digest = toHexString(sha1.digest());

                    if (isTrusted(digest))
                    {
                        return;
                    }

                    if (i==0)
                    {
                        currentCertificates.add(digest);
                    }

                    certsText.append(" " + (i + 1) + " Subject " + cert.getSubjectDN()+"\n");
                    certsText.append("   Issuer  " + cert.getIssuerDN()+"\n");
                    sha1.update(cert.getEncoded());
                    certsText.append("   sha1    " + digest +"\n");
                    md5.update(cert.getEncoded());
                    certsText.append("   md5     " + toHexString(md5.digest())+"\n\n");
                }

                final CertificatesDialog dialog = new CertificatesDialog(Misc.getMainFrame(), true);
                dialog.setCertsText(certsText.toString());

                try {


                        SwingUtilities.invokeAndWait(new Runnable() {

                            public void run() {
                               dialog.setVisible(true);
                            }
                        });


                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    }

                 if (dialog.getDialogResult() == JOptionPane.OK_OPTION)
                 {
                     if (dialog.getAnswer() == 0) // Accept the certificates permanently
                     {
                         this.trustedCertificates.addAll(currentCertificates);
                         saveCertificates();
                         return;
                     }
                     else if (dialog.getAnswer() == 1)
                     {
                         this.temporaryCertificates.addAll(currentCertificates);
                         return;
                     }
                 }

                 throw new CertificateException("Untrusted certificate");
        }


        private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

        private static String toHexString(byte[] bytes) {
            StringBuilder sb = new StringBuilder(bytes.length * 3);
            for (int b : bytes) {
                b &= 0xff;
                sb.append(HEXDIGITS[b >> 4]);
                sb.append(HEXDIGITS[b & 15]);
                sb.append(' ');
            }
            return sb.toString();
        }

    /**
     * @return the storingFingerprints
     */
    public boolean isStoringFingerprints() {
        return storingFingerprints;
    }

    /**
     * @param storingFingerprints the storingFingerprints to set
     */
    public void setStoringFingerprints(boolean storingFingerprints) {
        this.storingFingerprints = storingFingerprints;
    }


    private boolean isTrusted(String fingerprint)
    {
        for (String s: trustedCertificates)
        {
            if (s != null && s.equals(fingerprint)) return true;
        }

        for (String s: temporaryCertificates)
        {
            if (s != null && s.equals(fingerprint)) return true;
        }

        return false;
    }

}
