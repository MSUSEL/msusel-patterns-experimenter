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
package org.hibernate.envers.test.integration.superclass.auditAtMethodSuperclassLevel;
import javax.persistence.MappedSuperclass;

import org.hibernate.envers.Audited;

/**
 * @author Adam Warski (adam at warski dot org)
 * 
 * @author Hern&aacut;n Chanfreau
 */
@MappedSuperclass
public class AuditedMethodMappedSuperclass {

	@Audited
	private String str;
	
	private String otherStr;

	public AuditedMethodMappedSuperclass() {
	}

	public AuditedMethodMappedSuperclass(String str, String otherStr) {
		this.str = str;
		this.otherStr = otherStr;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String getOtherStr() {
		return otherStr;
	}

	public void setOtherStr(String otherStr) {
		this.otherStr = otherStr;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AuditedMethodMappedSuperclass))
			return false;

		AuditedMethodMappedSuperclass that = (AuditedMethodMappedSuperclass) o;

		if (str != null ? !str.equals(that.str) : that.str != null)
			return false;

		return true;
	}

	public int hashCode() {
		return (str != null ? str.hashCode() : 0);
	}
}
