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
package org.hibernate.envers.test.integration.entityNames.singleAssociatedAudited;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import org.hibernate.MappingException;
import org.hibernate.envers.test.AbstractOneSessionTest;
import org.hibernate.envers.test.Priority;

/**
 * @author Hern&aacute;n Chanfreau
 * 
 */
public class ReadEntityAssociatedAuditedTest extends AbstractOneSessionTest{

	private long id_car1;
	private long id_car2;
	
	private Car currentCar1;
	private Car car1;
	
	private long id_pers1; 
	private long id_pers2;
	
	private Person currentPerson1;
	private Person person1;
	
	
	
	
	protected void initMappings() throws MappingException, URISyntaxException {
		URL url = Thread.currentThread().getContextClassLoader().getResource("mappings/entityNames/singleAssociatedAudited/mappings.hbm.xml");
        config.addFile(new File(url.toURI()));
	}

	
    @Test
    @Priority(10)
    public void initData() {
    	
    	initializeSession();

        Person pers1 = new Person("Hernan", 15);
        Person pers2 = new Person("Leandro", 19);
        
        Car car1 = new Car(1, pers1, null);
        Car car2 = new Car(2, pers2, null);
        
        //REV 1 
        getSession().getTransaction().begin();
        getSession().persist("Personaje",pers1);
        getSession().persist(car1);
        getSession().getTransaction().commit();
        id_car1 = car1.getId();
        id_pers1 = pers1.getId();

        //REV 2
        getSession().getTransaction().begin();
        pers1.setAge(50);
        getSession().persist("Personaje", pers1);
        getSession().persist("Personaje", pers2);
        getSession().persist(car2);
        getSession().getTransaction().commit();
        id_car2 = car2.getId();
        id_pers2 = pers2.getId();
        
    }
    
    private void loadDataOnSessionAndAuditReader() {
        currentCar1 = (Car)getSession().get(Car.class, id_car1);
        currentPerson1 = (Person)getSession().get("Personaje", id_pers1);
		car1 = getAuditReader().find(Car.class, id_car1, 1);
    	person1 = car1.getOwner();
    	
    }
    
	private void checkEntities() {
    	assert(currentPerson1.getAge() != person1.getAge());
    	
    	Person person2 = (Person)getSession().get("Personaje", id_pers2);
    	Car car2 = getAuditReader().find(Car.class, id_car2, 2);
    	Person person2_1 = car2.getOwner();
    	assert(person2.getAge() == person2_1.getAge());
	}
	

	private void checkEntityNames() {
		String currentCar1EN = getSession().getEntityName(currentCar1);
    	
    	String currentPerson1EN = getSession().getEntityName(currentPerson1);
    	
    	String car1EN = getAuditReader().getEntityName(id_car1, 1, car1);
    	assert (currentCar1EN.equals(car1EN));
    	
    	String person1EN = getAuditReader().getEntityName(id_pers1, 1, person1);
    	assert (currentPerson1EN.equals(person1EN));
	}    
	
    @Test
    public void testGetAssociationWithEntityName() {
    	loadDataOnSessionAndAuditReader();
    	checkEntities();
    	checkEntityNames();
    }


    @Test
    public void testGetAssociationWithEntityNameInNewSession() {
    	//force a new session and AR
    	forceNewSession();

    	loadDataOnSessionAndAuditReader();

    	checkEntities();
    	
    	checkEntityNames();
    	 
    }    

}
