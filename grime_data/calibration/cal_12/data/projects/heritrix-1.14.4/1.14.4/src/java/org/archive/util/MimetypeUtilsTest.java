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
package org.archive.util;

import junit.framework.TestCase;

/**
 * @author stack
 * @version $Date: 2004-09-23 02:15:19 +0000 (Thu, 23 Sep 2004) $, $Revision: 2556 $
 */
public class MimetypeUtilsTest extends TestCase {

	public void testStraightTruncate() {
        assertTrue("Straight broken",
            MimetypeUtils.truncate("text/html").equals("text/html"));
	}
    
    public void testWhitespaceTruncate() {
        assertTrue("Null broken",
            MimetypeUtils.truncate(null).equals("no-type"));
        assertTrue("Empty broken",
                MimetypeUtils.truncate("").equals("no-type"));
        assertTrue("Tab broken",
                MimetypeUtils.truncate("    ").equals("no-type"));
        assertTrue("Multispace broken",
                MimetypeUtils.truncate("    ").equals("no-type"));
        assertTrue("NL broken",
                MimetypeUtils.truncate("\n").equals("no-type"));
    }
    
    public void testCommaTruncate() {
        assertTrue("Comma broken",
            MimetypeUtils.truncate("text/html,text/html").equals("text/html"));
        assertTrue("Comma space broken",
            MimetypeUtils.truncate("text/html, text/html").
                equals("text/html"));
        assertTrue("Charset broken",
            MimetypeUtils.truncate("text/html;charset=iso9958-1").
                equals("text/html"));
        assertTrue("Charset space broken",
            MimetypeUtils.truncate("text/html; charset=iso9958-1").
                equals("text/html"));
        assertTrue("dbl text/html space charset broken", MimetypeUtils.
            truncate("text/html, text/html; charset=iso9958-1").
                equals("text/html"));
    }
}
