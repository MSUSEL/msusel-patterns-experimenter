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
package org.hibernate.dialect.pagination;

import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.RowSelection;

/**
 * Limit handler that delegates all operations to the underlying dialect.
 *
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class LegacyLimitHandler extends AbstractLimitHandler {
	private final Dialect dialect;

	public LegacyLimitHandler(Dialect dialect, String sql, RowSelection selection) {
		super( sql, selection );
		this.dialect = dialect;
	}

	public boolean supportsLimit() {
		return dialect.supportsLimit();
	}

	public boolean supportsLimitOffset() {
		return dialect.supportsLimitOffset();
	}

	public boolean supportsVariableLimit() {
		return dialect.supportsVariableLimit();
	}

	public boolean bindLimitParametersInReverseOrder() {
		return dialect.bindLimitParametersInReverseOrder();
	}

	public boolean bindLimitParametersFirst() {
		return dialect.bindLimitParametersFirst();
	}

	public boolean useMaxForLimit() {
		return dialect.useMaxForLimit();
	}

	public boolean forceLimitUsage() {
		return dialect.forceLimitUsage();
	}

	public int convertToFirstRowValue(int zeroBasedFirstResult) {
		return dialect.convertToFirstRowValue( zeroBasedFirstResult );
	}

	public String getProcessedSql() {
		boolean useLimitOffset = supportsLimit() && supportsLimitOffset()
				&& LimitHelper.hasFirstRow( selection ) && LimitHelper.hasMaxRows( selection );
		return dialect.getLimitString(
				sql, useLimitOffset ? LimitHelper.getFirstRow( selection ) : 0, getMaxOrLimit()
		);
	}
}
