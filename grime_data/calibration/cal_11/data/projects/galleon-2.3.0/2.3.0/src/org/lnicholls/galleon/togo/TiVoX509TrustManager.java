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
package org.lnicholls.galleon.togo;

/*
 * Copyright (C) 2005 Leon Nicholls
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * 
 * See the file "COPYING" for more details.
 */

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import org.lnicholls.galleon.util.*;

/**
 * Accepts self-signed certificates.
 */

public class TiVoX509TrustManager implements X509TrustManager {
    private static Logger log = Logger.getLogger(TiVoX509TrustManager.class.getName());
    
    private static String TIVO_ISSUER = "TiVo";

    public TiVoX509TrustManager() throws Exception {
        super();
        TrustManagerFactory trustManagerFactory = null;
        try
        {
            trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        }
        catch (Exception ex)
        {
            try
            {
                trustManagerFactory = TrustManagerFactory.getInstance("IbmX509");
            }
            catch (Exception ex2)
            {
                throw ex2;
            }
        }
        
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length == 0) {
            throw new NoSuchAlgorithmException("SunX509 not supported");
        }
        mFactoryTrustManager = (X509TrustManager) trustManagers[0];
    }

    public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
        if (certificates != null) {
            if (false && log.isDebugEnabled()) {
                for (int i = 0; i < certificates.length; i++) {
                    log.debug("Certificate[" + i + "]=" + certificates[i]);
                }
            }

            if (certificates.length == 1) {
                X509Certificate certificate = certificates[0];
                try {
                    certificate.checkValidity();
                } catch (CertificateException ex) {
                    Tools.logException(TiVoX509TrustManager.class, ex);
                    throw new CertificateException();
                }
                if (certificate.getIssuerDN().getName().indexOf(TIVO_ISSUER)!=-1)
                    throw new CertificateException();
                return;
            }
        }
        mFactoryTrustManager.checkClientTrusted(certificates, authType);
    }

    public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
        if (certificates != null) {
            if (false && log.isDebugEnabled()) {
                for (int i = 0; i < certificates.length; i++) {
                    log.debug("Certificate[" + i + "]=" + certificates[i]);
                }
            }

            if (certificates.length == 1) {
                X509Certificate certificate = certificates[0];
                try {
                    certificate.checkValidity();
                } catch (CertificateException ex) {
                    Tools.logException(TiVoX509TrustManager.class, ex);
                    throw new CertificateException();
                }
                if (certificate.getIssuerDN().getName().indexOf(TIVO_ISSUER)==-1)
                    throw new CertificateException();
                return;
            }
        }
        mFactoryTrustManager.checkServerTrusted(certificates, authType);
    }

    public X509Certificate[] getAcceptedIssuers() {
        return mFactoryTrustManager.getAcceptedIssuers();
    }

    private X509TrustManager mFactoryTrustManager;
}