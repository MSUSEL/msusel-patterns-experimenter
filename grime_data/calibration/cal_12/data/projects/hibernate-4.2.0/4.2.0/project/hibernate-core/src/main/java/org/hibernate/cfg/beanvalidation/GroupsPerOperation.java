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
package org.hibernate.cfg.beanvalidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.validation.groups.Default;

import org.hibernate.HibernateException;
import org.hibernate.internal.util.ReflectHelper;

/**
 * @author Emmanuel Bernard
 */
public class GroupsPerOperation {

	private static final String JPA_GROUP_PREFIX = "javax.persistence.validation.group.";
	private static final String HIBERNATE_GROUP_PREFIX = "org.hibernate.validator.group.";
	private static final Class<?>[] DEFAULT_GROUPS = new Class<?>[] { Default.class };
	private static final Class<?>[] EMPTY_GROUPS = new Class<?>[] { };

	private Map<Operation, Class<?>[]> groupsPerOperation = new HashMap<Operation, Class<?>[]>(4);

	public GroupsPerOperation(Properties properties) {
		setGroupsForOperation( Operation.INSERT, properties );
		setGroupsForOperation( Operation.UPDATE, properties );
		setGroupsForOperation( Operation.DELETE, properties );
		setGroupsForOperation( Operation.DDL, properties );
	}

	private void setGroupsForOperation(Operation operation, Properties properties) {
		Object property = properties.get( operation.getGroupPropertyName() );

		Class<?>[] groups;
		if ( property == null ) {
			groups = operation == Operation.DELETE ? EMPTY_GROUPS : DEFAULT_GROUPS;
		}
		else {
			if ( property instanceof String ) {
				String stringProperty = (String) property;
				String[] groupNames = stringProperty.split( "," );
				if ( groupNames.length == 1 && groupNames[0].equals( "" ) ) {
					groups = EMPTY_GROUPS;
				}
				else {
					List<Class<?>> groupsList = new ArrayList<Class<?>>(groupNames.length);
					for (String groupName : groupNames) {
						String cleanedGroupName = groupName.trim();
						if ( cleanedGroupName.length() > 0) {
							try {
								groupsList.add( ReflectHelper.classForName( cleanedGroupName ) );
							}
							catch ( ClassNotFoundException e ) {
								throw new HibernateException( "Unable to load class " + cleanedGroupName, e );
							}
						}
					}
					groups = groupsList.toArray( new Class<?>[groupsList.size()] );
				}
			}
			else if ( property instanceof Class<?>[] ) {
				groups = (Class<?>[]) property;
			}
			else {
				//null is bad and excluded by instanceof => exception is raised
				throw new HibernateException( JPA_GROUP_PREFIX + operation.getGroupPropertyName() + " is of unknown type: String or Class<?>[] only");
			}
		}
		groupsPerOperation.put( operation, groups );
	}

	public Class<?>[] get(Operation operation) {
		return groupsPerOperation.get( operation );
	}

	public static enum Operation {
		INSERT("persist", JPA_GROUP_PREFIX + "pre-persist"),
		UPDATE("update", JPA_GROUP_PREFIX + "pre-update"),
		DELETE("remove", JPA_GROUP_PREFIX + "pre-remove"),
		DDL("ddl", HIBERNATE_GROUP_PREFIX + "ddl");


		private String exposedName;
		private String groupPropertyName;

		Operation(String exposedName, String groupProperty) {
			this.exposedName = exposedName;
			this.groupPropertyName = groupProperty;
		}

		public String getName() {
			return exposedName;
		}

		public String getGroupPropertyName() {
			return groupPropertyName;
		}
	}

}
