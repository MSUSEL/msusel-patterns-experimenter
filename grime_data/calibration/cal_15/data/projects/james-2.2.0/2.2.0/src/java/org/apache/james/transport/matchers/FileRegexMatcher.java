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
package org.apache.james.transport.matchers;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.james.util.RFC2822Headers;
import javax.mail.MessagingException;

/**
 * Initializes RegexMatcher with regular expressions from a file.
 *
 */
public class FileRegexMatcher extends GenericRegexMatcher {
    public void init() throws MessagingException {
        try {
            java.io.RandomAccessFile patternSource = new java.io.RandomAccessFile(getCondition(), "r");
            int lines = 0;
            while(patternSource.readLine() != null) lines++;
            patterns = new Object[lines][2];
            patternSource.seek(0);
            for (int i = 0; i < lines; i++) {
                String line = patternSource.readLine();
                patterns[i][0] = line.substring(0, line.indexOf(':'));
                patterns[i][1] = line.substring(line.indexOf(':')+1);
            }
            compile(patterns);
        }
        catch (java.io.FileNotFoundException fnfe) {
            throw new MessagingException("Could not locate patterns.", fnfe);
        }
        catch (java.io.IOException ioe) {
            throw new MessagingException("Could not read patterns.", ioe);
        }
        catch(MalformedPatternException mp) {
            throw new MessagingException("Could not initialize regex patterns", mp);
        }
    }
}
