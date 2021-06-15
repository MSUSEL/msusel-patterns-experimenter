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
package org.hibernate.test.typeparameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.jdbc.Work;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

/**
 * Test for parameterizable types.
 * 
 * @author Michael Gloegl
 */
public class TypeParameterTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] {
				"typeparameters/Typedef.hbm.xml",
				"typeparameters/Widget.hbm.xml"
		};
	}

	@Test
	@SuppressWarnings( {"UnnecessaryUnboxing"})
	public void testSave() throws Exception {
		deleteData();

		Session s = openSession();
		s.beginTransaction();
		Widget obj = new Widget();
		obj.setValueThree(5);
		final Integer id = (Integer) s.save(obj);
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();

		doWork(id, s);

		s.getTransaction().commit();
		s.close();

		deleteData();
	}
	
	private void doWork(final Integer id, final Session s) {
		s.doWork(
				new Work() {
					@Override
					public void execute(Connection connection) throws SQLException {
						PreparedStatement statement = ((SessionImplementor)s).getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().prepareStatement( "SELECT * FROM STRANGE_TYPED_OBJECT WHERE ID=?" );
						statement.setInt(1, id.intValue());
						ResultSet resultSet = ((SessionImplementor)s).getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().extract( statement );

						assertTrue("A row should have been returned", resultSet.next());
						assertTrue("Default value should have been mapped to null", resultSet.getObject("VALUE_ONE") == null);
						assertTrue("Default value should have been mapped to null", resultSet.getObject("VALUE_TWO") == null);
						assertEquals("Non-Default value should not be changed", resultSet.getInt("VALUE_THREE"), 5);
						assertTrue("Default value should have been mapped to null", resultSet.getObject("VALUE_FOUR") == null);
					}
				}
		);
	}

	@Test
	public void testLoading() throws Exception {
		initData();

		Session s = openSession();
		Transaction t = s.beginTransaction();

		Widget obj = (Widget) s.createQuery("from Widget o where o.string = :string").setString("string", "all-normal").uniqueResult();
		assertEquals("Non-Default value incorrectly loaded", obj.getValueOne(), 7);
		assertEquals("Non-Default value incorrectly loaded", obj.getValueTwo(), 8);
		assertEquals("Non-Default value incorrectly loaded", obj.getValueThree(), 9);
		assertEquals("Non-Default value incorrectly loaded", obj.getValueFour(), 10);

		obj = (Widget) s.createQuery("from Widget o where o.string = :string").setString("string", "all-default").uniqueResult();
		assertEquals("Default value incorrectly loaded", obj.getValueOne(), 1);
		assertEquals("Default value incorrectly loaded", obj.getValueTwo(), 2);
		assertEquals("Default value incorrectly loaded", obj.getValueThree(), -1);
		assertEquals("Default value incorrectly loaded", obj.getValueFour(), -5);

		
		t.commit();
		s.close();
		deleteData();
	}

	private void initData() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		Widget obj = new Widget();
		obj.setValueOne(7);
		obj.setValueTwo(8);
		obj.setValueThree(9);
		obj.setValueFour(10);
		obj.setString("all-normal");
		s.save(obj);

		obj = new Widget();
		obj.setValueOne(1);
		obj.setValueTwo(2);
		obj.setValueThree(-1);
		obj.setValueFour(-5);
		obj.setString("all-default");
		s.save(obj);

		t.commit();
		s.close();
	}

	private void deleteData() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		s.createQuery( "delete from Widget" ).executeUpdate();
		t.commit();
		s.close();
	}
}