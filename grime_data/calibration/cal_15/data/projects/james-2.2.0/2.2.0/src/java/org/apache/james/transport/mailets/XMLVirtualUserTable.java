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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.mail.MessagingException;

import org.apache.mailet.MailAddress;

/**
 * Implements a Virtual User Table to translate virtual users
 * to real users. This implementation has the same functionality
 * as <code>JDBCVirtualUserTable</code>, but is configured in the
 * JAMES configuration and is thus probably most suitable for smaller
 * and less dynamic mapping requirements.
 * 
 * The configuration is specified in the form:
 * 
 * &lt;mailet match="All" class="XMLVirtualUserTable"&gt;
 *   &lt;mapping&gt;virtualuser@xxx=realuser[@yyy][;anotherrealuser[@zzz]]&lt;/mapping&gt;
 *   &lt;mapping&gt;virtualuser2@*=realuser2[@yyy][;anotherrealuser2[@zzz]]&lt;/mapping&gt;
 *   ...
 * &lt;/mailet&gt;
 * 
 * As many &lt;mapping&gt; elements can be added as necessary. As indicated,
 * wildcards are supported, and multiple recipients can be specified with a 
 * semicolon-separated list. The target domain does not need to be specified if 
 * the real user is local to the server.
 * 
 * Matching is done in the following order:
 * 1. user@domain    - explicit mapping for user@domain
 * 2. user@*         - catchall mapping for user anywhere
 * 3. *@domain       - catchall mapping for anyone at domain
 * 4. null           - no valid mapping
 */
public class XMLVirtualUserTable extends AbstractVirtualUserTable
{
  /**
   * Holds the configured mappings
   */
  private Map mappings = new HashMap();

  /**
   * Initialize the mailet
   */
  public void init() throws MessagingException {
      String mapping = getInitParameter("mapping");
      
      if(mapping != null) {
          StringTokenizer tokenizer = new StringTokenizer(mapping, ",");
          while(tokenizer.hasMoreTokens()) {
            String mappingItem = tokenizer.nextToken();
            int index = mappingItem.indexOf('=');
            String virtual = mappingItem.substring(0, index).trim().toLowerCase();
            String real = mappingItem.substring(index + 1).trim().toLowerCase();
            mappings.put(virtual, real);
          }
      }
  }

  /**
   * Map any virtual recipients to real recipients using the configured mapping.
   * 
   * @param recipientsMap the mapping of virtual to real recipients
   */
  protected void mapRecipients(Map recipientsMap) throws MessagingException {
      Collection recipients = recipientsMap.keySet();  
        
      for (Iterator i = recipients.iterator(); i.hasNext(); ) {
          MailAddress source = (MailAddress)i.next();
          String user = source.getUser().toLowerCase();
          String domain = source.getHost().toLowerCase();
    
          String targetString = getTargetString(user, domain);
          
          if (targetString != null) {
              recipientsMap.put(source, targetString);
          }
      }
  }

  /**
   * Returns the real recipient given a virtual username and domain.
   * 
   * @param user the virtual user
   * @param domain the virtual domain
   * @return the real recipient address, or <code>null</code> if no mapping exists
   */
  private String getTargetString(String user, String domain) {
      StringBuffer buf;
      String target;
      
      //Look for exact (user@domain) match
      buf = new StringBuffer().append(user).append("@").append(domain);
      target = (String)mappings.get(buf.toString());
      if (target != null) {
          return target;
      }
      
      //Look for user@* match
      buf = new StringBuffer().append(user).append("@*");
      target = (String)mappings.get(buf.toString());
      if (target != null) {
          return target;
      }
      
      //Look for *@domain match
      buf = new StringBuffer().append("*@").append(domain);
      target = (String)mappings.get(buf.toString());
      if (target != null) {
          return target;
      }
      
      return null;
  }
  
  public String getMailetInfo() {
      return "XML Virtual User Table mailet";
  }
}
