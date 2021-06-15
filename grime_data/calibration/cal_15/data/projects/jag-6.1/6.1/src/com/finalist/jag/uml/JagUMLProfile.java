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
package com.finalist.jag.uml;

import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *
 * Define all UML profile elements for generating a UML model from JAG.
 * A UML Profile uses UML Extensions like stereotypes and tagged values
 * to add extra semantics to an UML model.
 *
 * @author Rudie Ekkelenkamp.
 * @version $Revision: 1.11 $, $Date: 2005/12/24 13:35:48 $
 * @created Octobre 23, 2003
 *
 */
public class JagUMLProfile {
   /* Private class that will keep a hashmap with all constants defined using reflection. */
   private static HashMap map = null;


   /* Class StereoTypes */

   /** Class stereotype: Service for for example a Session EJB */
   public final static String STEREOTYPE_CLASS_SERVICE = "Service";
   /** Class stereotypes: Entity */
   public final static String STEREOTYPE_CLASS_ENTITY = "Entity";
   /** Class stereotypes: ValueObject*/
   public final static String STEREOTYPE_CLASS_VALUE_OBJECT = "ValueObject";
   /** Class stereotypes: DataSource used to configure a datasource to be used with the application */
   public final static String STEREOTYPE_CLASS_DATA_SOURCE = "DataSource";
    /** Class stereotypes: JagConfig used to configure JAG */
    public final static String STEREOTYPE_CLASS_JAG_CONFIG = "JagConfig";

   /* Attribute StereoTypes */

   /** Attribute stereotypes: PrimaryKey */
   public final static String STEREOTYPE_ATTRIBUTE_PRIMARY_KEY = "PrimaryKey";
   /** Attribute stereotypes: ForeignKey */
   public final static String STEREOTYPE_ATTRIBUTE_FOREIGN_KEY = "ForeignKey";
   /** Attribute stereotypes: Required field */
   public final static String STEREOTYPE_ATTRIBUTE_REQUIRED = "Required";
   /** Operation stereotypes: FinderMethod */
   public final static String STEREOTYPE_OPERATION_FINDER_METHOD = "FinderMethod";

   /* Dependency StereoTypes */

   /** Dependency stereotypes: EntityRef */
   public final static String STEREOTYPE_DEPENDENCTY_ENTITY_REF = "EntityRef";
   /** Dependency stereotypes: ServiceRef */
   public final static String STEREOTYPE_DEPENDENCTY_SERVICE_REF = "ServiceRef";
   /** Dependency stereotypes: Exception */
   public final static String STEREOTYPE_DEPENDENCTY_EXCEPTION = "Exception";


    /* Model Tagged Values */

    // Configuration part.

    /** Determines the author of the application. */
    public final static String TAGGED_VALUE_MODEL_AUTHOR = "author";
    /** Determines the version of the application. */
    public final static String TAGGED_VALUE_MODEL_VERSION = "version";
    /** Determines the company of the application. */
    public final static String TAGGED_VALUE_MODEL_COMPANY = "company";
    /** Determines the template to be used. */
    public final static String TAGGED_VALUE_MODEL_TEMPLATE = "template";
    /** Determines the appserver to be used. */
    public final static String TAGGED_VALUE_CONFIG_APPSERVER = "appserver";
    /** Determines the business tier technology to be used. */
    public final static String TAGGED_VALUE_CONFIG_BUSINESS_TIER = "business-tier";
   /** Determines the business tier technology to be used. */
   public final static String TAGGED_VALUE_CONFIG_SERVICE_TIER = "service-tier";
    /** Determines the web tier technology to be used. */
    public final static String TAGGED_VALUE_CONFIG_WEB_TIER = "web-tier";
    /** Determines if relations should be used. */
    public final static String TAGGED_VALUE_CONFIG_USE_RELATIONS = "use-relations";
    /** Determines if a mock should be used. */
    public final static String TAGGED_VALUE_CONFIG_USE_MOCK = "use-mock";
    /** Determines if java5 should be used. */
    public final static String TAGGED_VALUE_CONFIG_USE_JAVA5 = "use-java5";

    // Application settings part.

    /** Determines the name of the application. */
    public final static String TAGGED_VALUE_MODEL_APPLICATION_NAME = "application-name";
    /** Determines the description of the application. */
    public final static String TAGGED_VALUE_MODEL_APPLICATION_VERSION = "application-version";
    /** Determines the description of the application. */
    public final static String TAGGED_VALUE_MODEL_DESCRIPTION = "description";
    /** Determines the root package of the generated sources. */
    public final static String TAGGED_VALUE_MODEL_ROOT_PACKAGE = "root-package";
    /** Determines the description of the application. */
    public final static String TAGGED_VALUE_MODEL_LOGGING = "logging";
    /** Determines the date format. */
    public final static String TAGGED_VALUE_MODEL_DATE_FORMAT = "date-format";
    /** Determines the date format. */
    public final static String TAGGED_VALUE_MODEL_TIMESTAMP_FORMAT = "timestamp-format";

    // Paths part.

    /** Determines the path to the service directory. */
    public final static String TAGGED_VALUE_MODEL_SERVICE_PATH = "service-path";
    /** Determines the path to the ejb directory. */
    public final static String TAGGED_VALUE_MODEL_EJB_PATH = "ejb-path";
    /** Determines the path to the web directory. */
    public final static String TAGGED_VALUE_MODEL_WEB_PATH = "web-path";
    /** Determines the path to the jsp directory. */
    public final static String TAGGED_VALUE_MODEL_JSP_PATH = "jsp-path";
    /** Determines the path to the service directory. */
    public final static String TAGGED_VALUE_MODEL_TEST_PATH = "test-path";
    /** Determines the path to the config directory. */
    public final static String TAGGED_VALUE_MODEL_CONFIG_PATH = "config-path";
   /** Determines the path to the config directory. */
   public final static String TAGGED_VALUE_MODEL_MOCK_PATH = "mock-path";
   /** Determines the path to the config directory. */
   public final static String TAGGED_VALUE_MODEL_SWING_PATH = "swing-path";
   /** Determines the path to the config directory. */
   public final static String TAGGED_VALUE_MODEL_SPRING_PATH = "spring-path";
   /** Determines the path to the config directory. */
   public final static String TAGGED_VALUE_MODEL_HIBERNATE_PATH = "hibernate-path";




   /* Class Tagged Values */

    // Datasource part.

    /** Class tagged value: DataSource JNDI name */
    public final static String TAGGED_VALUE_CLASS_DATA_SOURCE_JNDI_NAME = "datasource-jndi-name";
   /** Class tagged value: mapping, defines the database type: Oracle8, MySQL .. */
   public final static String TAGGED_VALUE_CLASS_DATA_SOURCE_MAPPING = "mapping";
   /** Class tagged value: jdbc-url, defines the connectstring to the database */
   public final static String TAGGED_VALUE_CLASS_DATA_SOURCE_JDBC_URL = "jdbc-url";
   /** Class tagged value: user-name, defines the user name to connect to the data source */
   public final static String TAGGED_VALUE_CLASS_DATA_SOURCE_USER_NAME = "user-name";
   /** Class tagged value: password, defines the password to connect to the data source */
   public final static String TAGGED_VALUE_CLASS_DATA_SOURCE_PASSWORD = "password";

    /** Class tagged value: table-name, used for Entity to map class on a database table. */
    public final static String TAGGED_VALUE_CLASS_TABLE_NAME = "table-name";
   /** Class tagged value: table-name, used for Entity to map class on a database table. */
   public final static String TAGGED_VALUE_CLASS_COMPOSITE_PRIMARY_KEY = "composite-primary-key";
   /** Class tagged value: description. */
   public final static String TAGGED_VALUE_CLASS_DESCRIPTION = "description";
   /** Class tagged value: display-name . */
   public final static String TAGGED_VALUE_CLASS_DISPLAY_NAME = "display-name";
   /** Class tagged value: is-association. */
   public final static String TAGGED_VALUE_CLASS_IS_ASSOCIATION = "is-association";

   /* Attribute Tagged Values */

   /** Attribute tagged value: column-name, used to map a field to a database column */
   public final static String TAGGED_VALUE_ATTRIBUTE_COLUMN_NAME = "column-name";
   /** Attribute tagged value: sql-type, used to map a field type to a SQL type */
   public final static String TAGGED_VALUE_ATTRIBUTE_SQL_TYPE = "sql-type";
   /** Attribute tagged value: jdbc-type, used to map a field type to a JDBC type */
   public final static String TAGGED_VALUE_ATTRIBUTE_JDBC_TYPE = "jdbc-type";
    /** Attribute tagged value: autogenerated primary key. */
    public final static String TAGGED_VALUE_ATTRIBUTE_AUTO_PRIMARY_KEY = "auto-primary-key";

   /** Association tagged value: foreign-field */
   public final static String TAGGED_VALUE_ASSOCIATION_FOREIGN_FIELD = "foreign-field";
   /** Association tagged value: multiplicity */
   public final static String TAGGED_VALUE_ASSOCIATION_MULTIPLICITY = "multiplicity";
   public final static String TAGGED_VALUE_ASSOCIATION_MULTIPLICITY_MANY_TO_ONE = "many to one";
   public final static String TAGGED_VALUE_ASSOCIATION_MULTIPLICITY_ONE_TO_ONE = "one to one";

   /** Association tagged value: bidirectional */
   public final static String TAGGED_VALUE_ASSOCIATION_BIDIRECTIONAL = "bidirectional";


   /** Tagged value for all model elements: documentation */
   public final static String TAGGED_VALUE_DOCUMENTATION = "documentation";

   /**
    * Determines a list of all constants using reflection and put them in a hashmap.
    *
    * @return HashMap with the names of all constants and their String values.
    */
   public synchronized static HashMap getConstants() {
      if (map != null) {
         return map;
      }
      map = new HashMap();
      Field fields[] = null;

      try {
         fields = JagUMLProfile.class.getDeclaredFields();
      }
      catch (SecurityException e) {
         e.printStackTrace();
         return new HashMap();
      }
      if (fields != null) {
         if (fields.length != 0) {
            for (int i = 0; i < fields.length; i++) {
               if (Modifier.isPublic(fields[i].getModifiers())
                  && Modifier.isFinal(fields[i].getModifiers())
                  && Modifier.isStatic(fields[i].getModifiers())
               ) {
                  // It's a constant!
                  try {
                     map.put(fields[i].getName(), fields[i].get(null));
                  }
                  catch (Exception e) {
                     e.printStackTrace();
                     return new HashMap();
                  }
               }
            }
         }
      }
      return map;
   }
}

/*
        $Log: JagUMLProfile.java,v $
        Revision 1.11  2005/12/24 13:35:48  ekkelenkamp
        added new tagged values.

        Revision 1.10  2005/09/23 07:23:58  ekkelenkamp
        export service tier selection

        Revision 1.9  2005/06/09 19:09:54  ekkelenkamp
        java5 support.

        Revision 1.8  2005/02/04 08:20:43  ekkelenkamp
        UML synchronize up-to-date.

        Revision 1.7  2005/01/19 21:44:58  ekkelenkamp
        uml support for many-to-one relations and bidirectionality.

        Revision 1.6  2004/12/05 23:27:44  ekkelenkamp
        Fixes for relation fields update.

        Revision 1.5  2004/11/27 19:30:07  ekkelenkamp
        Improved UML/JAG synchronization.

        Revision 1.4  2004/11/27 07:50:04  ekkelenkamp
        Improved UML/JAG synchronization.

        Revision 1.3  2004/11/26 22:36:13  ekkelenkamp
        export all project settings to a Config class.

        Revision 1.2  2004/03/28 11:55:34  ekkelenkamp
        tagged values on model

        Revision 1.1  2003/11/02 14:01:31  ekkelenkamp
        Initial version of UML support in Jag

*/