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
package com.finalist.jag.skelet;


import java.util.*;


/**
 * Class JagSkeletConfig
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class JagSkeletConfig {

   /** Field author           */
   private String author;

   /** Field version           */
   private String version;

   /** Field company           */
   private String company;

   /** Field templates           */
   private Collection templates = new HashSet();


   /**
    * Method setAuthor
    *
    *
    * @param author
    *
    */
   public void setAuthor(String author) {
      this.author = author;
   }


   /**
    * Method setVersion
    *
    *
    * @param version
    *
    */
   public void setVersion(String version) {
      this.version = version;
   }


   /**
    * Method setCompany
    *
    *
    * @param company
    *
    */
   public void setCompany(String company) {
      this.company = company;
   }


   /**
    * Method addTemplateUrl
    *
    *
    * @param template
    *
    */
   public void addTemplateUrl(String template) {
      this.templates.add(template);
   }


   /**
    * Method addTemplateUrl
    *
    *
    * @param templates
    *
    */
   public void addTemplateUrl(Collection templates) {

      if (templates == null) {
         return;
      }

      Iterator iterator = templates.iterator();

      while (iterator.hasNext()) {
         addTemplateUrl((String) iterator.next());
      }
   }


   /**
    * Method getAuthor
    *
    *
    * @return
    *
    */
   public String getAuthor() {
      return (this.author);
   }


   /**
    * Method getVersion
    *
    *
    * @return
    *
    */
   public String getVersion() {
      return (this.version);
   }


   /**
    * Method getCompany
    *
    *
    * @return
    *
    */
   public String getCompany() {
      return (this.company);
   }


   /**
    * Method getTemplatesUrls
    *
    *
    * @return
    *
    */
   public String[] getTemplatesUrls() {
      return (String[]) templates.toArray(new String[templates.size()]);
   }


   /**
    * Method toString
    *
    *
    * @return
    *
    */
   public String toString() {
      StringBuffer toString = new StringBuffer();
      toString.append("\nauthor : ");
      toString.append(author);
      toString.append("\nversion : ");
      toString.append(version);
      toString.append("\ncompany : ");
      toString.append(company);
      toString.append("\ncity : ");
      toString.append(templates);
      toString.append("\ntemplates : ");

      return new String(toString);
   }
}