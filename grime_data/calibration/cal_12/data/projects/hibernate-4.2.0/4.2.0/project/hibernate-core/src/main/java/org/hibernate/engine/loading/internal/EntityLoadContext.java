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
package org.hibernate.engine.loading.internal;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;

import org.hibernate.internal.CoreMessageLogger;

/**
 * Tracks information about loading of entities specific to a given result set.  These can be hierarchical.
 *
 * @author Steve Ebersole
 */
public class EntityLoadContext {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, EntityLoadContext.class.getName() );

	private final LoadContexts loadContexts;
	private final ResultSet resultSet;
	private final List hydratingEntities = new ArrayList( 20 ); // todo : need map? the prob is a proper key, right?

	public EntityLoadContext(LoadContexts loadContexts, ResultSet resultSet) {
		this.loadContexts = loadContexts;
		this.resultSet = resultSet;
	}

	void cleanup() {
		if ( !hydratingEntities.isEmpty() ) {
			LOG.hydratingEntitiesCount( hydratingEntities.size() );
		}
		hydratingEntities.clear();
	}


	@Override
	public String toString() {
		return super.toString() + "<rs=" + resultSet + ">";
	}
}
