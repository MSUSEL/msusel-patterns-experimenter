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
package org.hibernate.metamodel.relational;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.dialect.Dialect;

/**
 * Convenience base class for {@link org.hibernate.mapping.AuxiliaryDatabaseObject}s.
 * <p/>
 * This implementation performs dialect scoping checks strictly based on
 * dialect name comparisons.  Custom implementations might want to do
 * instanceof-type checks.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractAuxiliaryDatabaseObject implements AuxiliaryDatabaseObject {
	// Use a UUID in identifier prefix because this object is not qualified by a schema/catalog
	// (not sure this matters...)
	private static final String EXPORT_IDENTIFIER_PREFIX = "auxiliary-object-" + UUID.randomUUID();
	private static final AtomicInteger counter = new AtomicInteger( 0 );
	private final String exportIdentifier;
	private final Set<String> dialectScopes;

	protected AbstractAuxiliaryDatabaseObject(Set<String> dialectScopes) {
		this.dialectScopes =  dialectScopes == null ? new HashSet<String>() : dialectScopes;
		this.exportIdentifier =
				new StringBuilder( EXPORT_IDENTIFIER_PREFIX )
						.append( '.' )
						.append( counter.getAndIncrement() )
						.toString();
	}

	public void addDialectScope(String dialectName) {
		dialectScopes.add( dialectName );
	}

	public Iterable<String> getDialectScopes() {
		return dialectScopes;
	}

	public boolean appliesToDialect(Dialect dialect) {
		// empty means no scoping
		return dialectScopes.isEmpty() || dialectScopes.contains( dialect.getClass().getName() );
	}

	@Override
	public String getExportIdentifier() {
		return exportIdentifier;
	}
}
