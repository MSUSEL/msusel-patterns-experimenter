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
package org.archive.io.arc;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.archive.io.ArchiveRecordHeader;


/**
 * An immutable class to hold an ARC record meta data.
 *
 * @author stack
 */
public class ARCRecordMetaData implements ArchiveRecordHeader, ARCConstants {
    /**
     * Map of record header fields.
     *
     * We store all in a hashmap.  This way we can hold version 1 or
     * version 2 record meta data.
     *
     * <p>Keys are lowercase.
     */
    protected Map headerFields = null;
    
    /**
     * Digest for the record.
     * 
     * Only available after the record has been read in totality.
     */
    private String digest = null;
    
    /**
     * Status for this request.
     * 
     * There may be no status.
     */
    private String statusCode = null;
    
    /**
     * The arc this metadata came out.
     * Descriptive String, either path or URL.
     */
    private String arc = null;
    
    private int contentBegin = 0;
    
    /**
     * Shut down the default constructor.
     */
    protected ARCRecordMetaData() {
        super();
    }

    /**
     * Constructor.
     *
     * @param arc The arc file this metadata came out of.
     * @param headerFields Hash of meta fields.
     *
     * @throws IOException
     */
    public ARCRecordMetaData(final String arc, Map headerFields)
        throws IOException {
        // Make sure the minimum required fields are present,
        for (Iterator i = REQUIRED_VERSION_1_HEADER_FIELDS.iterator();
            i.hasNext(); ) {
            testRequiredField(headerFields, (String)i.next());
        }
        this.headerFields = headerFields;
        this.arc = arc;
    }

    /**
     * Test required field is present in hash.
     *
     * @param fields Map of fields.
     * @param requiredField Field to test for.
     *
     * @exception IOException If required field is not present.
     */
    protected void testRequiredField(Map fields, String requiredField)
        throws IOException {
        if (!fields.containsKey(requiredField)) {
            throw new IOException("Required field " + requiredField +
            " not in meta data.");
        }
    }

    /**
     * Get the time when the record was harvested.
     * <p>
     * Returns the date in Heritrix 14 digit time format (UTC). See the
     * {@link org.archive.util.ArchiveUtils} class for converting to Java
     * dates.
     * 
     * @return Header date in Heritrix 14 digit format.
     * @see org.archive.util.ArchiveUtils#parse14DigitDate(String)
     */
    public String getDate() {
        return (String) this.headerFields.get(DATE_FIELD_KEY);
    }

    /**
     * @return Return length of the record.
     */
    public long getLength() {
        return Long.parseLong((String)this.headerFields.
            get(LENGTH_FIELD_KEY));
    }

    /**
     * @return Header url.
     */
    public String getUrl() {
        return (String)this.headerFields.get(URL_FIELD_KEY);
    }

    /**
     * @return IP.
     */
    public String getIp()
    {
        return (String)this.headerFields.get(IP_HEADER_FIELD_KEY);
    }

    /**
     * @return mimetype The mimetype that is in the ARC metaline -- NOT the http
     * content-type content.
     */
    public String getMimetype() {
        return (String)this.headerFields.get(MIMETYPE_FIELD_KEY);
    }

    /**
     * @return Arcfile version.
     */
    public String getVersion() {
        return (String)this.headerFields.get(VERSION_FIELD_KEY);
    }

    /**
     * @return Offset into arcfile at which this record begins.
     */
    public long getOffset() {
        return ((Long)this.headerFields.get(ABSOLUTE_OFFSET_KEY)).longValue();
    }

    /**
     * @param key Key to use looking up field value.
     * @return value for passed key of null if no such entry.
     */
    public Object getHeaderValue(String key) {
        return this.headerFields.get(key);
    }

    /**
     * @return Header field name keys.
     */
    public Set getHeaderFieldKeys()
    {
        return this.headerFields.keySet();
    }

    /**
     * @return Map of header fields.
     */
    public Map getHeaderFields() {
        return this.headerFields;
    }
    
    /**
     * @return Returns identifier for ARC.
     */
    public String getArc() {
        return this.arc;
    }
    
    /**
     * @return Convenience method that does a
     * return new File(this.arc) (Be aware this.arc is not always
     * full path to an ARC file -- may be an URL).  Test
     * returned file for existence.
     */
    public File getArcFile() {
        return new File(this.arc);
    }
    
    /**
     * @return Returns the digest.
     */
    public String getDigest() {
        return this.digest;
    }
    
    /**
     * @param d The digest to set.
     */
    public void setDigest(String d) {
        this.digest = d;
    }
    
    /**
     * @return Returns the statusCode.  May be null.
     */
    public String getStatusCode() {
        return this.statusCode;
    }
    
    /**
     * @param statusCode The statusCode to set.
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
    
    public String toString() {
        return ((this.arc != null)? this.arc: "") +
           ": " +
           ((this.headerFields != null)? this.headerFields.toString():  "");
    }

	public String getReaderIdentifier() {
		return this.getArc();
	}

	public String getRecordIdentifier() {
	    return getDate() + "/" + getUrl();
	}

    public int getContentBegin() {
        return this.contentBegin;
    }
    
    void setContentBegin(final int offset) {
        this.contentBegin = offset;
    }
}