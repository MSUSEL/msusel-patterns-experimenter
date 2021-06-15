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
package org.apache.hadoop.hdfsproxy;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;

class DummyLdapContext extends InitialLdapContext {
  class ResultEnum<T> implements NamingEnumeration<T> {
    private ArrayList<T> rl;

    public ResultEnum() {
      rl = new ArrayList<T>();
    }

    public ResultEnum(ArrayList<T> al) {
      rl = al;
    }

    public boolean hasMoreElements() {
      return !rl.isEmpty();
    }

    public T nextElement() {
      T t = rl.get(0);
      rl.remove(0);
      return t;
    }

    public boolean hasMore() throws NamingException {
      return !rl.isEmpty();
    }

    public T next() throws NamingException {
      T t = rl.get(0);
      rl.remove(0);
      return t;
    }

    public void close() throws NamingException {
    }
  }

  public DummyLdapContext() throws NamingException {
  }

  public DummyLdapContext(Hashtable<?, ?> environment, Control[] connCtls)
      throws NamingException {
  }

  public NamingEnumeration<SearchResult> search(String name,
      Attributes matchingAttributes, String[] attributesToReturn)
      throws NamingException {
    System.out.println("Searching Dummy LDAP Server Results:");
    if (!"ou=proxyroles,dc=mycompany,dc=com".equalsIgnoreCase(name)) {
      System.out.println("baseName mismatch");
      return new ResultEnum<SearchResult>();
    }
    if (!"cn=127.0.0.1".equals((String) matchingAttributes.get("uniqueMember")
        .get())) {
      System.out.println("Ip address mismatch");
      return new ResultEnum<SearchResult>();
    }
    BasicAttributes attrs = new BasicAttributes();
    BasicAttribute uidAttr = new BasicAttribute("uid", "testuser");
    attrs.put(uidAttr);
    BasicAttribute groupAttr = new BasicAttribute("userClass", "testgroup");
    attrs.put(groupAttr);
    BasicAttribute locAttr = new BasicAttribute("documentLocation", "/testdir");
    attrs.put(locAttr);
    SearchResult sr = new SearchResult(null, null, attrs);
    ArrayList<SearchResult> al = new ArrayList<SearchResult>();
    al.add(sr);
    NamingEnumeration<SearchResult> ne = new ResultEnum<SearchResult>(al);
    return ne;
  }

  @SuppressWarnings("unchecked")
  public static void main(String[] args) throws Exception {
    DummyLdapContext dlc = new DummyLdapContext();
    String baseName = "ou=proxyroles,dc=mycompany,dc=com";
    Attributes matchAttrs = new BasicAttributes(true);
    String[] attrIDs = { "uid", "documentLocation" };
    NamingEnumeration<SearchResult> results = dlc.search(baseName, matchAttrs,
        attrIDs);
    if (results.hasMore()) {
      SearchResult sr = results.next();
      Attributes attrs = sr.getAttributes();
      for (NamingEnumeration ne = attrs.getAll(); ne.hasMore();) {
        Attribute attr = (Attribute) ne.next();
        if ("uid".equalsIgnoreCase(attr.getID())) {
          System.out.println("User ID = " + attr.get());
        } else if ("documentLocation".equalsIgnoreCase(attr.getID())) {
          System.out.println("Document Location = ");
          for (NamingEnumeration e = attr.getAll(); e.hasMore();) {
            System.out.println(e.next());
          }
        }
      }
    }
  }
}
