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
package com.jaspersoft.ireport.examples;
import com.jaspersoft.ireport.examples.beans.PersonBean;
import java.util.Vector;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;

/**
 *
 * @author  Administrator
 */
public class SampleJRDataSourceFactory {
    
    // This is the method to call to get the datasource.
    // The method must be static.....    
    public  JRDataSource createDatasource()
    {
        javax.swing.table.DefaultTableModel tm = new javax.swing.table.DefaultTableModel(4,2);
        
        PersonBean person = new PersonBean();
        person.setFirstName("Giulio");
        person.setLastName("Toffoli");
        person.setEmail("gt@businesslogic.it");
        tm.setValueAt(person, 0, 0);
        tm.setValueAt("Test value row 1 col 1", 0, 1);
        
        person = new PersonBean();
        person.setFirstName("Teodor");
        person.setLastName("Danciu");
        person.setEmail("teodor@hotmail.com");
        tm.setValueAt(person, 1, 0);
        tm.setValueAt("Test value row 2 col 1", 1, 1);
        
        person = new PersonBean();
        person.setFirstName("Mario");
        person.setLastName("Rossi");
        person.setEmail("mario@rossi.org");
        tm.setValueAt(person, 2, 0);
        tm.setValueAt("Test value row 3 col 1", 2, 1);
        
        person = new PersonBean();
        person.setFirstName("Jennifer");
        person.setLastName("Lopez");
        person.setEmail("lopez@jennifer.com");
        tm.setValueAt(person, 3, 0);
        tm.setValueAt("Test value row 4 col 1", 3, 1);
        
        return new JRTableModelDataSource(tm);
    }    
    
     public  JRDataSource createBeanCollectionDatasource()
    {
        return new JRBeanCollectionDataSource(createBeanCollection());
    }    
     
    @SuppressWarnings("unchecked")
     public static  Vector  createBeanCollection()
     {
            java.util.Vector coll = new java.util.Vector();
        
        PersonBean person = new PersonBean();
        person.setFirstName("Giulio");
        person.setLastName("Toffoli");
        person.setEmail("gt@businesslogic.it");
        coll.add(person);
        
        person = new PersonBean();
        person.setFirstName("Teodor");
        person.setLastName("Danciu");
        person.setEmail("teodor@hotmail.com");
        coll.add(person);
        
        person = new PersonBean();
        person.setFirstName("Mario");
        person.setLastName("Rossi");
        person.setEmail("mario@rossi.org");
        coll.add(person);
        
        person = new PersonBean();
        person.setFirstName("Jennifer");
        person.setLastName("Lopez");
        person.setEmail("lopez@jennifer.com");
        coll.add(person);
    
        return coll;
     }
}
