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
package org.archive.io.warc;

import it.unimi.dsi.fastutil.io.RepositionableStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpParser;
import org.archive.io.ArchiveRecord;
import org.archive.io.ArchiveRecordHeader;


/**
 * A WARC file Record.
 *
 * @author stack
 */
public class WARCRecord extends ArchiveRecord implements WARCConstants {
    private Pattern WHITESPACE = Pattern.compile("\\s");
    
    /**
     * Constructor.
     *
     * @param in Stream cue'd up to be at the start of the record this instance
     * is to represent.
     * @throws IOException
     */
    public WARCRecord(InputStream in, final String identifier,
    	final long offset)
    throws IOException {
        this(in, identifier, offset, true, false);
    }
    
    /**
     * Constructor.
     * @param in Stream cue'd up just past Header Line and Named Fields.
     * @param headers Header Line and ANVL Named fields.
     * @throws IOException
     */
    public WARCRecord(InputStream in, ArchiveRecordHeader headers)
    		throws IOException {
        super(in, headers, 0, true, false);
    }

    /**
     * Constructor.
     *
     * @param in Stream cue'd up to be at the start of the record this instance
     * is to represent or, if <code>headers</code> is not null, just past the
     * Header Line and Named Fields.
     * @param identifier Identifier for this the hosting Reader.
     * @param offset Current offset into <code>in</code> (Used to keep
     * <code>position</code> properly aligned).  Usually 0.
     * @param digest True if we're to calculate digest for this record.  Not
     * digesting saves about ~15% of cpu during parse.
     * @param strict Be strict parsing (Parsing stops if file inproperly
     * formatted).
     * @throws IOException
     */
    public WARCRecord(final InputStream in, final String identifier,
    	final long offset, boolean digest, boolean strict) 
    throws IOException {
        super(in, null, 0, digest, strict);
        setHeader(parseHeaders(in, identifier, offset, strict));
    }
    
    /**
     * Parse WARC Header Line and Named Fields.
     * @param in Stream to read.
     * @param identifier Identifier for the hosting Reader.
     * @param offset Absolute offset into Reader.
     * @param strict Whether to be loose parsing or not.
     * @return An ArchiveRecordHeader.
     * @throws IOException 
     */
    protected ArchiveRecordHeader parseHeaders(final InputStream in,
        final String identifier, final long offset, final boolean strict)
    throws IOException {
    	final Map<Object, Object> m = new HashMap<Object, Object>();
    	m.put(ABSOLUTE_OFFSET_KEY, new Long(offset));
    	m.put(READER_IDENTIFIER_FIELD_KEY, identifier);
        
        long startPosition = -1;
        if (in instanceof RepositionableStream) {
            startPosition = ((RepositionableStream)in).position();
        }
        String firstLine =
            new String(HttpParser.readLine(in, WARC_HEADER_ENCODING));
        if (firstLine == null || firstLine.length() <=0) {
            throw new IOException("Failed to read WARC_MAGIC");
        }
        if (!firstLine.startsWith(WARC_MAGIC)) {
            throw new IOException("Failed to find WARC MAGIC: " + firstLine);
        }
        // Here we start reading off the inputstream but we're reading the
        // stream direct rather than going via WARCRecord#read.  The latter will
        // keep count of bytes read, digest and fail properly if EOR too soon...
        // We don't want digesting while reading Headers.
        // 
        Header [] h = HttpParser.parseHeaders(in, WARC_HEADER_ENCODING);
        for (int i = 0; i < h.length; i++) {
            m.put(h[i].getName(), h[i].getValue());
        }
        int headerLength = -1;
        if (in instanceof RepositionableStream) {
            headerLength =
                (int)(((RepositionableStream)in).position() - startPosition);
        }
        final int contentOffset = headerLength;
        incrementPosition(contentOffset);
   
    	return new ArchiveRecordHeader() {
    		private Map<Object, Object> headers = m;
            private int contentBegin = contentOffset;

			public String getDate() {
				return (String)this.headers.get(HEADER_KEY_DATE);
			}

			public String getDigest() {
                return null;
                // TODO: perhaps return block-digest? 
                // superclass def implies this is calculated ("only after
                // read in totality"), not pulled from header
//				return (String)this.headers.get(HEADER_KEY_CHECKSUM);
			}

			public String getReaderIdentifier() {
				return (String)this.headers.get(READER_IDENTIFIER_FIELD_KEY);
			}

			public Set getHeaderFieldKeys() {
				return this.headers.keySet();
			}

			public Map getHeaderFields() {
				return this.headers;
			}

			public Object getHeaderValue(String key) {
				return this.headers.get(key);
			}

			public long getLength() {
				Object o = this.headers.get(CONTENT_LENGTH);
				if (o == null) {
					return -1;
				}
				long contentLength = (o instanceof Long)?
                    ((Long)o).longValue(): Long.parseLong((String)o);
                return contentLength + contentOffset;
			}

			public String getMimetype() {
				return (String)this.headers.get(CONTENT_TYPE);
			}

			public long getOffset() {
				Object o = this.headers.get(ABSOLUTE_OFFSET_KEY);
				if (o == null) {
					return -1;
				}
				return (o instanceof Long)?
                    ((Long)o).longValue(): Long.parseLong((String)o);
			}

			public String getRecordIdentifier() {
				return (String)this.headers.get(RECORD_IDENTIFIER_FIELD_KEY);
			}

			public String getUrl() {
				return (String)this.headers.get(HEADER_KEY_URI);
			}

			public String getVersion() {
				return (String)this.headers.get(VERSION_FIELD_KEY);
			}
            
            public int getContentBegin() {
                return this.contentBegin;
            }
            
            @Override
            public String toString() {
                return this.headers.toString();
            }
    	};
    }
    
    @Override
    protected String getMimetype4Cdx(ArchiveRecordHeader h) {
        final String m = super.getMimetype4Cdx(h);
        if (m == null) {
            return "-";
        } else {
            // TODO: Mimetypes can have spaces in WARCs.  Emitting for CDX, just
            // squash them for now.  Later, quote them since squashing spaces won't
            // work for params that have quoted-string values.
            Matcher matcher = WHITESPACE.matcher(m);
            return matcher.replaceAll("");
        }
    }
}
