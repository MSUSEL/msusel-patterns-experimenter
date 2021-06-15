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
package org.archive.crawler.url.canonicalize;

import java.util.regex.Pattern;


/**
 * Strip known session ids.
 * @author stack
 * @version $Date: 2006-09-25 20:27:35 +0000 (Mon, 25 Sep 2006) $, $Revision: 4655 $
 */
public class StripSessionIDs
extends BaseRule {

    private static final long serialVersionUID = -3737115200690525641L;

    private static final String DESCRIPTION = "Strip known session IDs. " +
        "Use this rule to remove all of a set of known session IDs." +
        " For example, this rule will strip JSESSIONID and its value from" +
        " 'http://archive.org/index.html?" +
        "JSESSIONID=DDDSSE233232333355FFSXXXXDSDSDS'.  The resulting" +
        " canonicalization returns 'http://archive.org/index.html'." +
        " This rule strips JSESSIONID, ASPSESSIONID, PHPSESSID, and 'sid'" +
        " session ids.";
    
    /**
     * Example: jsessionid=999A9EF028317A82AC83F0FDFE59385A.
     * Example: PHPSESSID=9682993c8daa2c5497996114facdc805.
     */
    private static final Pattern BASE_PATTERN = Pattern.compile("^(.+)" +
            "(?:(?:(?:jsessionid)|(?:phpsessid))=" +
                 "[0-9a-zA-Z]{32})(?:&(.*))?$",  Pattern.CASE_INSENSITIVE);
    
    /**
     * Example: sid=9682993c8daa2c5497996114facdc805. 
     * 'sid=' can be tricky but all sid= followed by 32 byte string
     * so far seen have been session ids.  Sid is a 32 byte string
     * like the BASE_PATTERN only 'sid' is the tail of 'phpsessid'
     * so have to have it run after the phpsessid elimination.
     */
    private static final Pattern SID_PATTERN =
        Pattern.compile("^(.+)" +
            "(?:sid=[0-9a-zA-Z]{32})(?:&(.*))?$", Pattern.CASE_INSENSITIVE);
    
    /**
     * Example:ASPSESSIONIDAQBSDSRT=EOHBLBDDPFCLHKPGGKLILNAM.
     */
    private static final Pattern ASPSESSION_PATTERN =
        Pattern.compile("^(.+)" +
            "(?:ASPSESSIONID[a-zA-Z]{8}=[a-zA-Z]{24})(?:&(.*))?$",
                Pattern.CASE_INSENSITIVE);
    

    public StripSessionIDs(String name) {
        super(name, DESCRIPTION);
    }

    public String canonicalize(String url, Object context) {
        url = doStripRegexMatch(url, BASE_PATTERN.matcher(url));
        url = doStripRegexMatch(url, SID_PATTERN.matcher(url));
        url = doStripRegexMatch(url, ASPSESSION_PATTERN.matcher(url));
        return url;
    }
}