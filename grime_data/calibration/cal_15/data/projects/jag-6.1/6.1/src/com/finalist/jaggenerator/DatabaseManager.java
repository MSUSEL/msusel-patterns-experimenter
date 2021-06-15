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
package com.finalist.jaggenerator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The DatabaseManager handles JAG's generic database support.
 * This enables JAG to connect to any database, provided the database has a JDBC driver.
 *
 * @author Michael O'Connor - Finalist IT Group
 */
public class DatabaseManager {
   private static DatabaseManager ourInstance;
   private static ArrayList databases = new ArrayList();

   private static final String DATABASE = "database";
   private static final String SUPPORTED_DATABASES = "supported-databases";
   public static final String NAME = "name";
   private static final String DRIVER_CLASS = "driver-class";
   private static final String APPSERVER_TYPEMAPPING = "appserver-typemapping";
   private static final int READ_BUFFER_SIZE = 2048;
   private static final String DOT_CLASS = ".class";
   public static final String APPSERVER_TYPEMAPPINGS = "appserver-typemappings";

   private static String[] typeMappings;
   private static final Database[] DATABASE_ARRAY = new Database[0];
   private static final String FILE = "file";

   /**
    * The DatabaseManager is a singleton - this method obtains the one and only instance.
    * @return
    */
   public synchronized static DatabaseManager getInstance() {
      if (ourInstance == null) {
         ourInstance = new DatabaseManager();
      }
      return ourInstance;
   }

   private DatabaseManager() {
      load();
   }


   /**
    * Adds JDBC driver(s) from the specified File.
    *
    * @param driverFile
    * @return a List of Database objects, one for each driver found in the file.
    */
   public List addDrivers(final File driverFile) {
      boolean driverFound = false;
      List newDatabases = new ArrayList();

      try {
         final JarFile jar = new JarFile(driverFile);
         Enumeration e = jar.entries();

         while (e.hasMoreElements()) {
            JarEntry entry = (JarEntry) e.nextElement();
            if (entry.getName().endsWith(DOT_CLASS)) {
               String className = entry.getName().replace('/', '.').
                     substring(0, entry.getName().indexOf(DOT_CLASS));
               try {
                  Class clazz = new JarClassLoader(jar).loadClass(className);
                  if (Arrays.asList(clazz.getInterfaces()).contains(java.sql.Driver.class)) {
                     boolean alreadyKnown = false;
                     driverFound = true;
                     Iterator i = databases.iterator();
                     while (i.hasNext()) {
                        Database database = (Database) i.next();
                        if (database.getDriverClass().equals(className)) {
                           JOptionPane.showMessageDialog(null,
                                 "The driver '" + className + "' will not be added, as it is already known!",
                                 "Duplicate Driver", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                           alreadyKnown = true;
                           break;
                        }
                     }

                     if (!alreadyKnown) {
                        Database newDb = new Database();
                        newDb.setDriverClass(className);
                        newDb.setFilename(driverFile.getAbsolutePath());
                        newDatabases.add(newDb);
                     }
                  }

               } catch (Throwable cnf) {
                  System.out.println(cnf);
               }
            }
         }

      } catch (IOException e) {
         e.printStackTrace();  //To change body of catch statement use Options | File Templates.
      }

      if (!driverFound) {
         JOptionPane.showMessageDialog(null,
               "JAG could not find any JDBC drivers in file: " + driverFile,
               "No Drivers Found!", javax.swing.JOptionPane.INFORMATION_MESSAGE);
      } else if (newDatabases.size() > 0) {
         JOptionPane.showMessageDialog(null,
               "Found " + newDatabases.size() + " JDBC driver" + (newDatabases.size() == 1 ? "" : "s") +
               " in file: " + driverFile,
               "JDBC Driver" + (newDatabases.size() == 1 ? "" : "s") + " Found!",
               javax.swing.JOptionPane.INFORMATION_MESSAGE);
      }

      return newDatabases;
   }

   /**
    * Gets the list of supported databases.
    *
    * @return an array of all currently supported databases.
    */
   public Database[] getSupportedDatabases() {
      return (Database[]) databases.toArray(DATABASE_ARRAY);
   }

   /**
    * Resets the list of supported databases (and also resets the list in the Datasource panel).
    * @param editedValues
    */
   public void setDatabases(ArrayList editedValues) {
      databases = editedValues;
      JagGenerator.jagGenerator.root.datasource.setSupportedDatabases(getSupportedDatabases());
   }

   /**
    * Gets the list of possible appserver typemappings.
    * @return
    */
   public String[] getTypeMappings() {
      return typeMappings;
   }

   /**
    * Persists the supported database information by appending an XML element to the config Document.
    *
    * @param root The XML Element under which we append the XML.
    */
   public Element appendXML(Element root) {
      Document doc = root.getOwnerDocument();
      Element dbRoot = doc.createElement(SUPPORTED_DATABASES);
      Iterator i = databases.iterator();
      while (i.hasNext()) {
         Database dbInfo = (Database) i.next();
         Element database = doc.createElement(DATABASE);
         Element child = doc.createElement(NAME);
         if (dbInfo.getDbName() != null) {
            child.appendChild(doc.createTextNode(dbInfo.getDbName()));
         }
         database.appendChild(child);
         child = doc.createElement(DRIVER_CLASS);
         if (dbInfo.getDriverClass() != null) {
            child.appendChild(doc.createTextNode(dbInfo.getDriverClass()));
         }
         database.appendChild(child);
         child = doc.createElement(APPSERVER_TYPEMAPPING);
         if (dbInfo.getTypeMapping() != null) {
            child.appendChild(doc.createTextNode(dbInfo.getTypeMapping()));
         }
         database.appendChild(child);
         child = doc.createElement(FILE);
         if (dbInfo.getFilename() != null) {
            child.appendChild(doc.createTextNode(dbInfo.getFilename()));
         }
         database.appendChild(child);
         dbRoot.appendChild(database);
      }
      return dbRoot;
   }


   /**
    * Reads in the supported database info from the config doc.
    */
   private void load() {
      databases.clear();
      Document doc = ConfigManager.getInstance().getDocument();
      NodeList databaseNodes = doc.getElementsByTagName(DATABASE);
      for (int i = 0; i < databaseNodes.getLength(); i++) {
         Database dbInfo = new Database();
         Element database = (Element) databaseNodes.item(i);
         NodeList children = database.getChildNodes();
         for (int j = 0; j < children.getLength(); j++) {
            if (children.item(j) instanceof Element) {
               Element child = (Element) children.item(j);
               if (NAME.equals(child.getNodeName())) {
                  dbInfo.setDbName(child.getFirstChild().getNodeValue());
               } else if (DRIVER_CLASS.equals(child.getNodeName())) {
                  dbInfo.setDriverClass(child.getFirstChild().getNodeValue());
               } else if (APPSERVER_TYPEMAPPING.equals(child.getNodeName())) {
                  dbInfo.setTypeMapping(child.getFirstChild().getNodeValue());
               } else if (FILE.equals(child.getNodeName())) {
                  dbInfo.setFilename(child.getFirstChild().getNodeValue());
               }
            }
         }
         //check validity of driver file
         File driver = new File(dbInfo.getFilename());
         if (!driver.exists()) {
            JagGenerator.logToConsole("Removing missing driver reference: " + dbInfo.getFilename());
            JOptionPane.showMessageDialog(JagGenerator.jagGenerator,
                  "The previously specified JDBC driver for '" + dbInfo.getDbName() + "' databases can not be located.\n" +
                  "(last known location was: " + dbInfo.getFilename() + ")\n" +
                  "The entry for this driver will be deleted.",
                  "Missing JDBC Driver!", javax.swing.JOptionPane.ERROR_MESSAGE);
         } else {

            databases.add(dbInfo);
         }
      }

      Map typeMappingsMap = ConfigManager.getInstance().retrievePropertiesFromXML(APPSERVER_TYPEMAPPINGS);
      String[] temp = (String[]) typeMappingsMap.get(NAME);
      if (temp != null) {
         typeMappings = temp;
      }
   }

   /**
    * This ClassLoader is necessary for grabbing Class objects from a jar file.
    */
   private class JarClassLoader extends ClassLoader {
      private HashMap alreadyLoaded = new HashMap();
      private JarFile jar;

      /**
       * Initialises the JarClassLoader with the jar file.
       * @param jar
       */
      public JarClassLoader(JarFile jar) {
         this.jar = jar;
      }

      public synchronized Class loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
         Class clazz = (Class) alreadyLoaded.get(name);
         if (clazz == null) {
            try {
               //is it a system class?
               return findSystemClass(name);
            } catch (Exception e) {
            }
            try {
               //is it already loaded?
               return Class.forName(name);
            } catch (Exception e) {
            }

            byte[] bytes = loadClassBytes(name);
            try {
               clazz = defineClass(name, bytes, 0, bytes.length);
            } catch (Throwable t) {
               //System.out.println("!!defineClass threw " + t);
            }
               alreadyLoaded.put(name, clazz);

            if (clazz == null) {
               throw new ClassNotFoundException(name);
            }
         }

         if (resolve) {
            resolveClass(clazz);
         }

         return clazz;
      }

      private byte[] loadClassBytes(String name) {
         ByteArrayOutputStream temp = new ByteArrayOutputStream();
         byte[] buffer = new byte[READ_BUFFER_SIZE];
         String entryName = name.replace('.', '/') + DOT_CLASS;
         InputStream in = null;
         try {

//todo : the jars so far have not needed inflating.. is this always the case?
//               InflaterInputStream in = new InflaterInputStream(jar.getInputStream(entry));

            JarEntry entry = jar.getJarEntry(entryName);
            if (entry != null) {
               in = jar.getInputStream(entry);
               int bytesRead = -1;
               do {
                  bytesRead = in.read(buffer, 0, READ_BUFFER_SIZE);
                  if (bytesRead != -1) {
                     temp.write(buffer, 0, bytesRead);
                  }
               } while (bytesRead != -1);
            }

         } catch (IOException e1) {
            e1.printStackTrace();
         }

         return temp.toByteArray();
      }
   }

}

