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
package net.sourceforge.ganttproject.document;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import net.sourceforge.ganttproject.GPLogger;
import net.sourceforge.ganttproject.gui.options.model.StringOption;

public class FtpDocument extends AbstractURLDocument implements Document {
    private static final Object EMPTY_STRING = "";
    private final URI myURI;

    FtpDocument(String urlAsString, StringOption ftpUser, StringOption ftpPassword) {
        assert urlAsString!=null;
        try {
            URI url = new URI(urlAsString);
            String userInfo = url.getUserInfo();
            if (userInfo==null || EMPTY_STRING.equals(userInfo)) {
                StringBuffer buf = new StringBuffer();
                if (ftpUser.getValue()!=null) {
                    buf.append(ftpUser.getValue());
                }
                if (ftpPassword.getValue()!=null) {
                    buf.append(':').append(ftpPassword.getValue());
                }
                myURI = new URI("ftp", buf.toString(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getFragment());
            }
            else {
                myURI = url;
            }
            urlAsString = myURI.toString(); 
            myURI.toURL().openConnection().connect();
        } catch (URISyntaxException e) {
        	if (!GPLogger.log(e)) {
        		e.printStackTrace(System.err);
        	}
            throw new RuntimeException("Failed to create FTP document addressed by URL="+urlAsString, e);
        } catch (MalformedURLException e) {
        	if (!GPLogger.log(e)) {
        		e.printStackTrace();
        	}
            throw new RuntimeException("Failed to create FTP document addressed by URL="+urlAsString, e);
        } catch (IOException e) {
        	if (!GPLogger.log(e)) {
        		e.printStackTrace();
        	}
            throw new RuntimeException("Failed to create FTP document addressed by URL="+urlAsString, e);
        }
    }
    
    public String getDescription() {
        return myURI.toString();
    }

    public boolean canRead() {
        return true;
    }

    public boolean canWrite() {
        return true;
    }

    public boolean isValidForMRU() {
        return true;
    }

    public InputStream getInputStream() throws IOException {
        return myURI.toURL().openConnection().getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return myURI.toURL().openConnection().getOutputStream();
    }

    public String getPath() {
        return myURI.toString();
    }

    public void write() throws IOException {
        throw new UnsupportedOperationException();
    }

    public URI getURI() {
        return myURI;
    }

    public boolean isLocal() {
        return false;
    }

    
}
