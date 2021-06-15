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

import org.apache.james.util.RFC2822Headers;
import org.apache.mailet.GenericMatcher;
import org.apache.mailet.Mail;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Collection;

/**
 * This is a generic matcher that uses regular expressions.  If any of
 * the regular expressions match, the matcher is considered to have
 * matched.  This is an abstract class that must be subclassed to feed
 * patterns.  Patterns are provided by calling the compile method.  A
 * subclass will generally call compile() once during init(), but it
 * could subclass match(), and call it as necessary during message
 * processing (e.g., if a file of expressions changed). 
 *
 * @
 */

abstract public class GenericRegexMatcher extends GenericMatcher {
    protected Object[][] patterns;

    public void compile(Object[][] patterns) throws MalformedPatternException {
        // compile a bunch of regular expressions
        this.patterns = patterns;
        for (int i = 0; i < patterns.length; i++) {
            String pattern = (String)patterns[i][1];
            patterns[i][1] = new Perl5Compiler().compile(pattern, Perl5Compiler.READ_ONLY_MASK | Perl5Compiler.SINGLELINE_MASK);
        }
    }

    abstract public void init() throws MessagingException;

    public Collection match(Mail mail) throws MessagingException {
        MimeMessage message = mail.getMessage();
        Perl5Matcher matcher = new Perl5Matcher();

        //Loop through all the patterns
        if (patterns != null) for (int i = 0; i < patterns.length; i++) {
            //Get the header name
            String headerName = (String)patterns[i][0];
            //Get the patterns for that header
            Pattern pattern = (Pattern)patterns[i][1];
            //Get the array of header values that match that
            String headers[] = message.getHeader(headerName);
            //Loop through the header values
            if (headers != null) for (int j = 0; j < headers.length; j++) {
                if (matcher.matches(headers[j], pattern)) {
                    // log("Match: " + headerName + "[" + j + "]: " + headers[j]);
                    return mail.getRecipients();
                }
                //log("       " + headerName + "[" + j + "]: " + headers[j]);
            }
        }
        return null;
    }
}
