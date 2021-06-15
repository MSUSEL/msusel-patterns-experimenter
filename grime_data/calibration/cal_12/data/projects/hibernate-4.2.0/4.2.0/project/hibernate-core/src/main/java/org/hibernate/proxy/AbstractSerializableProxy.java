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
package org.hibernate.proxy;
import java.io.Serializable;

/**
 * Convenience base class for SerializableProxy.
 * 
 * @author Gail Badner
 */
public abstract class AbstractSerializableProxy implements Serializable {
	private String entityName;
	private Serializable id;
	private Boolean readOnly;

	/**
	 * For serialization
	 */
	protected AbstractSerializableProxy() {
	}

	protected AbstractSerializableProxy(String entityName, Serializable id, Boolean readOnly) {
		this.entityName = entityName;
		this.id = id;
		this.readOnly = readOnly;
	}

	protected String getEntityName() {
		return entityName;
	}

	protected Serializable getId() {
		return id;
	}

	/**
	 * Set the read-only/modifiable setting from this object in an AbstractLazyInitializer.
	 *
	 * This method should only be called during deserialization, before associating the
	 * AbstractLazyInitializer with a session.
	 *
	 * @param li the read-only/modifiable setting to use when
	 * associated with a session; null indicates that the default should be used.
	 * @throws IllegalStateException if isReadOnlySettingAvailable() == true
	 */
	protected void setReadOnlyBeforeAttachedToSession(AbstractLazyInitializer li) {
		li.setReadOnlyBeforeAttachedToSession( readOnly );
	}
}
