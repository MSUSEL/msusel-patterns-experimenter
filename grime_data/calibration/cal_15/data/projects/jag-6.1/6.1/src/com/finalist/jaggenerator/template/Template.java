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
package com.finalist.jaggenerator.template;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class represents a JAG 'generation template'.  A generation template is in fact a directory
 * containing a collection of files and subdirectories.  The template directory must have a "template.xml"
 * configuration file, which contains information about the template and what configuration options are to be
 * presented to the user.
 * 
 * @author Michael O'Connor - Finalist IT Group
 */
public class Template {

   private static final String XMLTAG_JAG_TEMPLATE = "jag-template";
   private static final String TEMPLATE_XML = "template.xml";
   private static final String ID_ATTRIBUTE = "id";
   private static final String NAME_ATTRIBUTE = "name";
   private static final String[] STRING_ARRAY = new String[0];
   private static final TemplateConfigParameter[] TCP_ARRAY = new TemplateConfigParameter[0];
   private static final String XMLTAG_PARAM = "param";
   private static final String DESCRIPTION_ATTRIBUTE = "description";
   private static final String TYPE_ATTRIBUTE = "type";
   private static final String XMLTAG_VALUE = "value";
   private static final String ENGINE_ATTRIBUTE = "template-engine";
   /** The default template engine class. */
   public static final String DEFAULT_ENGINE_CLASS = "com.finalist.jag.util.VelocityTemplateEngine";

   private String name;
   private String description;
   private String engine;
   private File templateDir;
   private Document doc;
   private TemplateConfigParameter[] configParams;


   public Template(File templateDir) throws TemplateConfigException {
      this.templateDir = templateDir;
      load(new File(templateDir, TEMPLATE_XML));
   }

   /**
    * Gets the name of this template.
    * @return the name.
    */
   public String getName() {
      return name;
   }

   /**
    * Gets the base directory of this template.
    * @return the base directory.
    */
   public File getTemplateDir() {
      return templateDir;
   }

    /**
     * Sets the base directory of this template.
     */
    public void setTemplateDir(File  templateDir) {
       this.templateDir = templateDir;
    }

   public String getDescription() {
      return description;
   }

   public String getEngine() {
      return engine;
   }

   public String getEngineClass() {
      return DEFAULT_ENGINE_CLASS;
   }

   /**
    * Gets the configuration paramaters defined for this template.
    * @return the config paramseters.
    */
   public TemplateConfigParameter[] getConfigParams() {
      return configParams;
   }

   public String toString() {
      return name;
   }


   /**
    * Reads in the template information from the XML file.
    */
   private void load(File xmlFile) throws TemplateConfigException {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = null;
      try {
         builder = dbf.newDocumentBuilder();
         doc = builder.parse(xmlFile);
         Element root = (Element) doc.getElementsByTagName(XMLTAG_JAG_TEMPLATE).item(0);
         if (root == null) {
            throw new SAXException("");
         }

         name = root.getAttribute(NAME_ATTRIBUTE);
         description = root.getAttribute(DESCRIPTION_ATTRIBUTE);
         engine = root.getAttribute(ENGINE_ATTRIBUTE);

         //read in all the config params defined in this template..
         ArrayList beans = new ArrayList();
         NodeList params = root.getElementsByTagName(XMLTAG_PARAM);
         for (int i = 0; i < params.getLength(); i++) {
            Element parameter = (Element) params.item(i);
            TemplateConfigParameter bean = new TemplateConfigParameter();
            bean.setId(parameter.getAttribute(ID_ATTRIBUTE));
            bean.setName(parameter.getAttribute(NAME_ATTRIBUTE));
            bean.setDescription(parameter.getAttribute(DESCRIPTION_ATTRIBUTE));
            bean.setType(TemplateConfigParameter.getTypeByName(parameter.getAttribute(TYPE_ATTRIBUTE)));
            ArrayList temp = new ArrayList();
            NodeList presetValues = parameter.getElementsByTagName(XMLTAG_VALUE);
            for (int j = 0; j < presetValues.getLength(); j++) {
               Element value = (Element) presetValues.item(j);
               temp.add(value.getFirstChild().getNodeValue());
            }
            bean.setPresetValues((String[]) temp.toArray(STRING_ARRAY));
            beans.add(bean);
         }
         configParams = (TemplateConfigParameter[]) beans.toArray(TCP_ARRAY);

      } catch (ParserConfigurationException e) {
         throw new TemplateConfigException("System error reading 'template.xml' : " + e);

      } catch (SAXException e) {
         throw new TemplateConfigException("The chosen template's 'template.xml' is invalid.");

      } catch (IOException e) {
         throw new TemplateConfigException("JAG can't locate the template's 'template.xml'.");
      }
   }

}
