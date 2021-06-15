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


import org.apache.mailet.GenericMatcher;
import org.apache.mailet.Mail;
import org.apache.mailet.MatcherConfig;
import java.util.Collection;
import javax.mail.MessagingException;
import java.io.Serializable;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * <P>This Matcher determines if the mail contains the attribute specified in the
 * condition and that attribute matches the supplied regular expression,
 * it returns all recipients if that is the case.</P>
 * <P>Sample configuration:</P>
 * <PRE><CODE>
 * &lt;mailet match="HasMailAttributeWithValueRegex=whatever,<regex>" class=&quot;&lt;any-class&gt;&quot;&gt;
 * </CODE></PRE>
 * Note: as it is not possible to put arbitrary objects in the configuration,
 * toString() is called on the attribute value, and that is the value matched against.
 *
 * @version CVS $Revision: 1.1.2.2 $ $Date: 2004/03/15 03:54:21 $
 * @since 2.2.0
 **/
public class HasMailAttributeWithValueRegex extends GenericMatcher 
{
    
    private String attributeName;
    private Perl5Matcher matcher  = new Perl5Matcher();
    private Pattern pattern   = null;

    /**
     * Return a string describing this matcher.
     *
     * @return a string describing this matcher
     */
    public String getMatcherInfo() {
        return "Has Mail Attribute Value Matcher";
    }

    public void init (MatcherConfig conf) throws MessagingException
    {
        String condition = conf.getCondition();
        int idx = condition.indexOf(',');
        if (idx != -1) {
            attributeName = condition.substring(0,idx).trim();
            String pattern_string = condition.substring (idx+1, condition.length()).trim();
            try {
                Perl5Compiler compiler = new Perl5Compiler();
                pattern = compiler.compile(pattern_string);
            } catch(MalformedPatternException mpe) {
                throw new MessagingException("Malformed pattern: " + pattern_string, mpe);
            }
        } else {
            throw new MessagingException ("malformed condition for HasMailAttributeWithValueRegex. must be of the form: attr,regex");
        }
    }

    /**
     * @param mail the mail to check.
     * @return all recipients if the part of the condition prior to the first equalsign
     * is the name of an attribute set on the mail and the part of the condition after
     * interpreted as a regular expression matches the toString value of the
     * corresponding attributes value.
     **/
    public Collection match (Mail mail) throws MessagingException
    {
        Serializable obj = mail.getAttribute (attributeName);
        //to be a little more generic the toString of the value is what is matched against
        if ( obj != null && matcher.matches(obj.toString(), pattern)) {
            return mail.getRecipients();
        } 
        return null;
    }
    
}
