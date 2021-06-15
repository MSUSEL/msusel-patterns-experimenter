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
package org.hibernate.cfg;
import java.util.Map;

import org.hibernate.MappingException;
import org.hibernate.cfg.annotations.TableBinder;
import org.hibernate.mapping.JoinedSubclass;
import org.hibernate.mapping.SimpleValue;

/**
 * @author Emmanuel Bernard
 */
@SuppressWarnings({"serial", "unchecked"})
public class JoinedSubclassFkSecondPass extends FkSecondPass {
	private JoinedSubclass entity;
	private Mappings mappings;

	public JoinedSubclassFkSecondPass(
			JoinedSubclass entity,
			Ejb3JoinColumn[] inheritanceJoinedColumns,
			SimpleValue key,
			Mappings mappings) {
		super( key, inheritanceJoinedColumns );
		this.entity = entity;
		this.mappings = mappings;
	}

	public String getReferencedEntityName() {
		return entity.getSuperclass().getEntityName();
	}

	public boolean isInPrimaryKey() {
		return true;
	}

	public void doSecondPass(Map persistentClasses) throws MappingException {
		TableBinder.bindFk( entity.getSuperclass(), entity, columns, value, false, mappings );
	}
}
