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
package org.apache.james.transport.mailets;

import org.apache.mailet.GenericMailet;
import org.apache.mailet.Mail;
import org.apache.mailet.MailetException;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.mail.MessagingException;

/**
 * This mailet sets attributes on the Mail.
 * 
 * Sample configuration:
 * &lt;mailet match="All" class="RemoveMailAttribute"&gt;
 *   &lt;name&gt;attribute_name1&lt;/name&gt;
 *   &lt;name&gt;attribute_name2&lt;/name&gt;
 * &lt;/mailet&gt;
 *
 * @version CVS $Revision: 1.1.2.2 $ $Date: 2004/03/15 03:54:19 $
 * @since 2.2.0
 */
public class RemoveMailAttribute extends GenericMailet {
    
    private ArrayList attributesToRemove = new ArrayList();
    
    /**
     * Return a string describing this mailet.
     *
     * @return a string describing this mailet
     */
    public String getMailetInfo() {
        return "Remove Mail Attribute Mailet";
    }

    /**
     * Initialize the mailet
     *
     * @throws MailetException if the processor parameter is missing
     */
    public void init() throws MailetException
    {
        String name = getInitParameter("name");

        if (name != null) {
            StringTokenizer st = new StringTokenizer(name, ",") ;
            while (st.hasMoreTokens()) {
                String attribute_name = st.nextToken().trim() ;
                attributesToRemove.add(attribute_name);
            }
        }
    }

    /**
     * Remove the configured attributes
     *
     * @param mail the mail to process
     *
     * @throws MessagingException in all cases
     */
    public void service(Mail mail) throws MessagingException {
        Iterator iter = attributesToRemove.iterator();
        while (iter.hasNext()) {
            String attribute_name = iter.next().toString();
            mail.removeAttribute (attribute_name);
        }
    }
    

}
