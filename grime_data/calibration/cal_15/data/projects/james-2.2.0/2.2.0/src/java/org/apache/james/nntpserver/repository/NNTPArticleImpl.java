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
package org.apache.james.nntpserver.repository;

import org.apache.james.core.MailHeaders;
import org.apache.james.nntpserver.NNTPException;
import org.apache.avalon.excalibur.io.IOUtil;

import javax.mail.internet.InternetHeaders;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

/** 
 * Please see NNTPArticle for comments
 */
class NNTPArticleImpl implements NNTPArticle {

    /**
     * The file that stores the article data
     */
    private final File articleFile;

    /**
     * The newsgroup containing this article.
     */
    private final NNTPGroup group;

    /**
     * The sole constructor for this class.
     *
     * @param group the news group containing this article
     * @param f the file that stores the article data
     */
    NNTPArticleImpl(NNTPGroup group, File f) {
        articleFile = f;
        this.group = group;
    }

    /**
     * @see org.apache.james.nntpserver.repository.NNTPArticle#getGroup()
     */
    public NNTPGroup getGroup() {
        return group;
    }

    /**
     * @see org.apache.james.nntpserver.repository.NNTPArticle#getArticleNumber()
     */
    public int getArticleNumber() {
        return Integer.parseInt(articleFile.getName());
    }

    /**
     * @see org.apache.james.nntpserver.repository.NNTPArticle#getUniqueID()
     */
    public String getUniqueID() {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(articleFile);
            InternetHeaders headers = new InternetHeaders(fin);
            String[] idheader = headers.getHeader("Message-Id");
            fin.close();
            return ( idheader.length > 0 ) ? idheader[0] : null;
        } catch(Exception ex) {
            throw new NNTPException(ex);
        } finally {
            IOUtil.shutdownStream(fin);
        }
    }

    /**
     * @see org.apache.james.nntpserver.repository.NNTPArticle#writeArticle(OutputStream)
     */
    public void writeArticle(OutputStream out) {
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(articleFile);
            byte[] readBuffer = new byte[1024];
            int read = 0;
            while ((read = fileStream.read(readBuffer)) > 0) {
                out.write(readBuffer, 0, read);
            }
        } catch(IOException ex) { 
            throw new NNTPException(ex);
        } finally {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException ioe) {
                    // Ignored
                }
            }
        }
    }

    /**
     * @see org.apache.james.nntpserver.repository.NNTPArticle#writeHead(OutputStream)
     */
    public void writeHead(OutputStream out) {
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(articleFile);
            MailHeaders headers = new MailHeaders(fileStream);
            byte[] headerBuffer = headers.toByteArray();
            int headerBufferLength = headerBuffer.length;
            // Write the headers excluding the final CRLF pair
            if (headerBufferLength > 2) {
                out.write(headerBuffer, 0, (headerBufferLength - 2));
            }
        } catch(Exception ex) { 
            throw new NNTPException(ex);
        } finally {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException ioe) {
                    // Ignored
                }
            }
        }
    }

    /**
     * @see org.apache.james.nntpserver.repository.NNTPArticle#writeBody(OutputStream)
     */
    public void writeBody(OutputStream out) {
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(articleFile);
            MailHeaders headers = new MailHeaders(fileStream);
            byte[] readBuffer = new byte[1024];
            int read = 0;
            while ((read = fileStream.read(readBuffer)) > 0) {
                out.write(readBuffer, 0, read);
            }
        } catch(Exception ex) {
            throw new NNTPException(ex);
        } finally {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException ioe) {
                    // Ignored
                }
            }
        }
    }

    /**
     * @see org.apache.james.nntpserver.repository.NNTPArticle#writeOverview(OutputStream)
     */
    public void writeOverview(OutputStream out) {
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(articleFile);
            InternetHeaders hdr = new InternetHeaders(fileStream);
            String subject = hdr.getHeader("Subject",null);
            String author = hdr.getHeader("From",null);
            String date = hdr.getHeader("Date",null);
            String msgId = hdr.getHeader("Message-Id",null);
            String references = hdr.getHeader("References",null);
            long byteCount = articleFile.length();

            // get line count, if not set, count the lines
            String lineCount = hdr.getHeader("Lines",null);
            if (lineCount == null) {
                BufferedReader rdr = new BufferedReader(new FileReader(fileStream.getFD()));
                int lines = 0;
                while (rdr.readLine() != null) {
                    lines++;
                }

                lineCount = Integer.toString(lines);
                rdr.close();
            }

            StringBuffer line=new StringBuffer(256)
                .append(getArticleNumber())    .append("\t")
                .append(cleanHeader(subject))    .append("\t")
                .append(cleanHeader(author))     .append("\t")
                .append(cleanHeader(date))       .append("\t")
                .append(cleanHeader(msgId))      .append("\t")
                .append(cleanHeader(references)) .append("\t")
                .append(byteCount)               .append("\t")
                .append(lineCount).append("\r\n");
            String lineString = line.toString();
            out.write(lineString.getBytes("ASCII"));
        } catch(Exception ex) {
            throw new NNTPException(ex);
        } finally {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException ioe) {
                    // Ignored
                }
            }
        }
    }

    /**
     * @see org.apache.james.nntpserver.repository.NNTPArticle#getHeader(String)
     */
    public String getHeader(String header) {
        try {
            FileInputStream fin = new FileInputStream(articleFile);
            InternetHeaders hdr = new InternetHeaders(fin);
            fin.close();
            return hdr.getHeader(header,null);
        } catch(Exception ex) {
            throw new NNTPException(ex);
        }
    }

    /**
     * Strips out newlines and tabs, converting them to spaces.
     * rfc2980: 2.8 XOVER requires newline and tab to be converted to spaces
     *
     * @param the input String
     *
     * @return the cleaned string
     */
    private String cleanHeader(String field) {
        if ( field == null )
            field = "";
        StringBuffer sb = new StringBuffer(field);
        for( int i=0 ; i<sb.length() ; i++ ) {
            char c = sb.charAt(i);
            if( (c=='\n') || (c=='\t') || (c=='\r')) {
                sb.setCharAt(i, ' ');
            }
        }
        return sb.toString();
    }
}
