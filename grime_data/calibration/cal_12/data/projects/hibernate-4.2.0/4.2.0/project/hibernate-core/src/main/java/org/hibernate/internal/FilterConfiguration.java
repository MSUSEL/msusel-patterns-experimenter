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
package org.hibernate.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.util.collections.CollectionHelper;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.persister.entity.Joinable;

/**
 *
 * @author Rob Worsnop
 */
public class FilterConfiguration {
	private final String name;
	private final String condition;
	private final boolean autoAliasInjection;
	private final Map<String, String> aliasTableMap;
	private final Map<String, String> aliasEntityMap;
	private final PersistentClass persistentClass;
	
	public FilterConfiguration(String name, String condition, boolean autoAliasInjection, Map<String, String> aliasTableMap, Map<String, String> aliasEntityMap, PersistentClass persistentClass) {
		this.name = name;
		this.condition = condition;
		this.autoAliasInjection = autoAliasInjection;
		this.aliasTableMap = aliasTableMap;
		this.aliasEntityMap = aliasEntityMap;
		this.persistentClass = persistentClass;
	}

	public String getName() {
		return name;
	}

	public String getCondition() {
		return condition;
	}

	public boolean useAutoAliasInjection() {
		return autoAliasInjection;
	}

	public Map<String, String> getAliasTableMap(SessionFactoryImplementor factory) {
		Map<String,String> mergedAliasTableMap = mergeAliasMaps(factory);
		if (!mergedAliasTableMap.isEmpty()){
			return mergedAliasTableMap;
		} else if (persistentClass != null){
			String table = persistentClass.getTable().getQualifiedName(factory.getDialect(), 
					factory.getSettings().getDefaultCatalogName(),
					factory.getSettings().getDefaultSchemaName());
			return Collections.singletonMap(null, table);
		} else{
			return Collections.emptyMap();
		}
	}
	
	private Map<String,String> mergeAliasMaps(SessionFactoryImplementor factory){
		Map<String,String> ret = new HashMap<String, String>();
		if (aliasTableMap != null){
			ret.putAll(aliasTableMap);
		}
		if (aliasEntityMap != null){
			for (Map.Entry<String, String> entry : aliasEntityMap.entrySet()){
				ret.put(entry.getKey(), 
						Joinable.class.cast(factory.getEntityPersister(entry.getValue())).getTableName());
			}
		}
		return ret;
	}
}