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
package org.hibernate.loader;
import java.util.Map;

import org.hibernate.persister.entity.Loadable;

/**
 * EntityAliases that chooses the column names over the alias names.  This strategy is used
 * when the result-set mapping did not give specific aliases to use in extracting from the
 * result set.  We use the column names from the underlying persister.
 * 
 * @author max
 * @author Steve Ebersole
 */
public class ColumnEntityAliases extends DefaultEntityAliases {

	public ColumnEntityAliases(
			Map returnProperties,
			Loadable persister, 
			String suffix) {
		super( returnProperties, persister, suffix );
	}
	
	protected String[] getIdentifierAliases(Loadable persister, String suffix) {
		return persister.getIdentifierColumnNames();
	}
	
	protected String getDiscriminatorAlias(Loadable persister, String suffix) {
		return persister.getDiscriminatorColumnName();
	}
	
	protected String[] getPropertyAliases(Loadable persister, int j) {
		return persister.getPropertyColumnNames(j);
	}
}
