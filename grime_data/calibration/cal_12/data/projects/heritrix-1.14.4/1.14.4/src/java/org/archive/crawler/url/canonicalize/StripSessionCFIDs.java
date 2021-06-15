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
 * Strip cold fusion session ids.
 * @author stack
 * @version $Date: 2006-09-25 20:27:35 +0000 (Mon, 25 Sep 2006) $, $Revision: 4655 $
 */
public class StripSessionCFIDs
extends BaseRule {

    private static final long serialVersionUID = 9122689291157731293L;

    private static final String REGEX = "^(.+)" +
        "(?:cfid=[^&]+&cftoken=[^&]+(?:jsession=[^&]+)?)(?:&(.*))?$";
    
    private static final String DESCRIPTION = "Strip ColdFusion session IDs. " +
        "Use this rule to remove sessionids that look like the following: " +
        "CFID=12412453&CFTOKEN=15501799 or " +
        "CFID=3304324&CFTOKEN=57491900&jsessionid=a63098d96360$B0$D9$A " +
        "using the following case-insensitive regex: " + REGEX;
        
    /**
     * Examples:
     * <pre>
     * Examples:
     * boo?CFID=1169580&CFTOKEN=48630702&dtstamp=22%2F08%2F2006%7C06%3A58%3A11
     * boo?CFID=12412453&CFTOKEN=15501799&dt=19_08_2006_22_39_28
     * boo?CFID=14475712&CFTOKEN=2D89F5AF-3048-2957-DA4EE4B6B13661AB&r=468710288378&m=forgotten
     * boo?CFID=16603925&CFTOKEN=2AE13EEE-3048-85B0-56CEDAAB0ACA44B8&r=501652357733&l1=home
     * boo?CFID=3304324&CFTOKEN=57491900&jsessionid=a63098d96360$B0$D9$A 
     * </pre>
     */
    private static final Pattern COLDFUSION_PATTERN =
        Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
    

    public StripSessionCFIDs(String name) {
        super(name, DESCRIPTION);
    }

    public String canonicalize(String url, Object context) {
        return doStripRegexMatch(url, COLDFUSION_PATTERN.matcher(url));
    }
}