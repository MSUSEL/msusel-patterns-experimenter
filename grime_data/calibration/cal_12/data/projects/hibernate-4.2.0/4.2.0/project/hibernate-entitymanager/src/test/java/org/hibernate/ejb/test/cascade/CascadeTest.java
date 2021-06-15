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
package org.hibernate.ejb.test.cascade;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Test;

import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;

/**
 * @author Max Rydahl Andersen
 */
public class CascadeTest extends BaseEntityManagerFunctionalTestCase {
	@Test
	public void testCascade() throws Exception {
		
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		
		Teacher teacher = new Teacher();

		Student student = new Student();

		teacher.setFavoriteStudent(student);
		student.setFavoriteTeacher(teacher);

		teacher.getStudents().add(student);
		student.setPrimaryTeacher(teacher);

		em.persist( teacher );
		em.getTransaction().commit();

		em = getOrCreateEntityManager();
		em.getTransaction().begin();
		
		Teacher foundTeacher = (Teacher) em.createQuery( "select t from Teacher as t" ).getSingleResult();
		
		System.out.println(foundTeacher);
		System.out.println(foundTeacher.getFavoriteStudent());
		
		for (Student fstudent : foundTeacher.getStudents()) {
			System.out.println(fstudent);			
			System.out.println(fstudent.getFavoriteTeacher());
			System.out.println(fstudent.getPrimaryTeacher());
		}
		
		em.getTransaction().commit(); // here *alot* of flushes occur on an object graph that has *Zero* changes.
		em.close();
	}

	@Test
	public void testNoCascadeAndMerge() throws Exception {
		Song e1 = new Song();
		Author e2 = new Author();

		e1.setAuthor(e2);

		EntityManager em = getOrCreateEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(e2);
		em.persist(e1);
		tx.commit();
		em.close();

		em = getOrCreateEntityManager();

		e1 = em.find(Song.class, e1.getId());


		tx = em.getTransaction();
		tx.begin();
		em.merge(e1);
		//em.refresh(e1);
		tx.commit();
		em.close();
	}

	@Override
	public Class[] getAnnotatedClasses() {
		return new Class[]{
				Teacher.class,
				Student.class,
				Song.class,
				Author.class
		};
	}


}
