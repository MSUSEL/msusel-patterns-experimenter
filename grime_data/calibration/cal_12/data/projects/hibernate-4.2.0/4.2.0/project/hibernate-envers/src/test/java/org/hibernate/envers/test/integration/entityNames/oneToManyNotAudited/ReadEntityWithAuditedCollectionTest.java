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
package org.hibernate.envers.test.integration.entityNames.oneToManyNotAudited;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.hibernate.MappingException;
import org.hibernate.envers.test.AbstractOneSessionTest;
import org.hibernate.envers.test.Priority;

/**
 * @author Hern&aacute;n Chanfreau
 * 
 */
public class ReadEntityWithAuditedCollectionTest extends AbstractOneSessionTest{

	private long id_car1;
	private long id_car2;
	
	private Car currentCar1;
	private Person currentPerson1;
	
	private long id_pers1;
	
	private Car car1_1;
	
	protected void initMappings() throws MappingException, URISyntaxException {
		URL url = Thread.currentThread().getContextClassLoader().getResource("mappings/entityNames/oneToManyNotAudited/mappings.hbm.xml");
        config.addFile(new File(url.toURI()));
	}
	
	
    @Test
    @Priority(10)
    public void initData() {
    	
    	initializeSession();

        Person pers1 = new Person("Hernan", 28);
        Person pers2 = new Person("Leandro", 29);
        Person pers4 = new Person("Camomo", 15);

        List<Person > owners = new ArrayList<Person>();
        owners.add(pers1);
        owners.add(pers2);
        Car car1 = new Car(5, owners);

        //REV 1 
        getSession().getTransaction().begin();
        getSession().persist(car1);
        getSession().getTransaction().commit();
        id_pers1 = pers1.getId();
        id_car1 = car1.getId();

        owners = new ArrayList<Person>();
        owners.add(pers2);
        owners.add(pers4);
        Car car2 = new Car(27, owners);
        //REV 2
        getSession().getTransaction().begin();
        Person person1 = (Person)getSession().get("Personaje", id_pers1);
        person1.setName("Hernan David");
        person1.setAge(40);
        getSession().persist(car1);
        getSession().persist(car2);
        getSession().getTransaction().commit();
        id_car2 = car2.getId();

    }
    
    private void loadDataOnSessionAndAuditReader() {
    	
    	currentCar1 = (Car)getSession().get(Car.class, id_car1);
    	currentPerson1 = (Person)getSession().get("Personaje", id_pers1);
    	
    	car1_1 = getAuditReader().find(Car.class, id_car1, 2);
    	Car car2 = getAuditReader().find(Car.class, id_car2, 2);

    	for (Person owner : car1_1.getOwners()) {
    		owner.getName();
    		owner.getAge();
		}
    	for (Person owner : car2.getOwners()) {
    		owner.getName();
    		owner.getAge();
		}
    }
    
    private void checkEntityNames() {
    	
    	String currCar1EN = getSession().getEntityName(currentCar1);
    	String currPerson1EN = getSession().getEntityName(currentPerson1);

    	String car1_1EN = getAuditReader().getEntityName(id_car1, 2, car1_1);
    	assert(currCar1EN.equals(car1_1EN));
    	
    	String person1_1EN = getSession().getEntityName(currentPerson1);
    	assert(currPerson1EN.equals(person1_1EN));
    }

    @Test
    public void testObtainEntityNameCollectionWithEntityNameAndNotAuditedMode() {
    	loadDataOnSessionAndAuditReader();
    	
    	checkEntityNames();

    	
    }    

    @Test
    public void testObtainEntityNameCollectionWithEntityNameAndNotAuditedModeInNewSession() {
    	// force new session and AR
    	forceNewSession();
    	
    	loadDataOnSessionAndAuditReader();
    	
    	checkEntityNames();

    	
    }
    	
    	
    	
    

}

