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

import org.apache.mailet.GenericRecipientMatcher;
import org.apache.mailet.MailAddress;

import org.apache.oro.text.regex.*;

import javax.mail.MessagingException;

/**
 * <P>Matches recipients whose address matches a regular expression.</P>
 * <P>Is equivalent to the {@link SenderIsRegex} matcher but matching on the recipient.</P>
 * <P>Configuration string: a regular expression.</P>
 * <PRE><CODE>
 * &lt;mailet match=&quot;RecipientIsRegex=&lt;regular-expression&gt;&quot; class=&quot;&lt;any-class&gt;&quot;&gt;
 * </CODE></PRE>
 * <P>The example below will match any recipient in the format user@log.anything</P>
 * <PRE><CODE>
 * &lt;mailet match=&quot;RecipientIsRegex=(.*)@log\.(.*)&quot; class=&quot;&lt;any-class&gt;&quot;&gt;
 * &lt;/mailet&gt;
 * </CODE></PRE>
 *
 * @version CVS $Revision: 1.1.2.4 $ $Date: 2004/03/15 03:54:21 $
 */

public class RecipientIsRegex extends GenericRecipientMatcher {
    Pattern pattern   = null;

    public void init() throws javax.mail.MessagingException {
        String patternString = getCondition();
        if (patternString == null) {
            throw new MessagingException("Pattern is missing");
        }
        
        patternString = patternString.trim();
        Perl5Compiler compiler = new Perl5Compiler();
        try {
            pattern = compiler.compile(patternString, Perl5Compiler.READ_ONLY_MASK);
        } catch(MalformedPatternException mpe) {
            throw new MessagingException("Malformed pattern: " + patternString, mpe);
        }
    }

    public boolean matchRecipient(MailAddress recipient) {
        String myRecipient = recipient.toString();
        Perl5Matcher matcher  = new Perl5Matcher();
        if (matcher.matches(myRecipient, pattern)){
            return true;
        } else {
            return false;
        }
    }
}
