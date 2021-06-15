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

import org.apache.commons.httpclient.URIException;
import org.archive.net.UURIFactory;

import junit.framework.TestCase;

public class StripSessionCFIDsTest extends TestCase {
    private static final String [] INPUTS = {
        "http://a.b.c/boo?CFID=1169580&CFTOKEN=48630702" +
            "&dtstamp=22%2F08%2F2006%7C06%3A58%3A11",
        "http://a.b.c/boo?CFID=12412453&CFTOKEN=15501799" +
        "   &dt=19_08_2006_22_39_28",
        "http://a.b.c/boo?CFID=14475712" +
        "   &CFTOKEN=2D89F5AF-3048-2957-DA4EE4B6B13661AB" +
            "&r=468710288378&m=forgotten",
        "http://a.b.c/boo?CFID=16603925" +
        "   &CFTOKEN=2AE13EEE-3048-85B0-56CEDAAB0ACA44B8",
        "http://a.b.c/boo?CFID=4308017&CFTOKEN=63914124" +
            "&requestID=200608200458360%2E39414378"
    };
    
    private static final String [] OUTPUTS = {
        "http://a.b.c/boo?dtstamp=22%2F08%2F2006%7C06%3A58%3A11",
        "http://a.b.c/boo?dt=19_08_2006_22_39_28",
        "http://a.b.c/boo?r=468710288378&m=forgotten",
        "http://a.b.c/boo?",
        "http://a.b.c/boo?requestID=200608200458360%2E39414378"
    };

    public void testCanonicalize() throws URIException {
        for (int i = 0; i < INPUTS.length; i++) {
            String result = (new StripSessionCFIDs(INPUTS[i])).
                canonicalize(INPUTS[i], UURIFactory.getInstance(INPUTS[i]));
            assertEquals(result, OUTPUTS[i]);
        }
    }
}