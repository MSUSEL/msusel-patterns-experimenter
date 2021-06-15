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
package org.hibernate.envers.test.entities.components;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 * The {@link #nonAuditedComponent} is ignored in {@link #hashCode()}
 * and {@link #equals(Object)} since it's not audited.
 *
 * @author Kristoffer Lundberg (kristoffer at cambio dot se)
 */
@Embeddable
@Audited
public class Component3 {
	private String str1;

	@AttributeOverrides({
			@AttributeOverride(name = "key", column = @Column(name = "audComp_key")),
			@AttributeOverride(name = "value", column = @Column(name = "audComp_value")),
			@AttributeOverride(name = "description", column = @Column(name = "audComp_description"))
	})
	private Component4 auditedComponent;

	@NotAudited
	@AttributeOverrides({
			@AttributeOverride(name = "key", column = @Column(name = "notAudComp_key")),
			@AttributeOverride(name = "value", column = @Column(name = "notAudComp_value")),
			@AttributeOverride(name = "description", column = @Column(name = "notAudComp_description"))
	})
	private Component4 nonAuditedComponent;

	public Component3() {
	}

	public Component3(String str1, Component4 auditedComponent, Component4 nonAuditedComponent) {
		this.str1 = str1;
		this.auditedComponent = auditedComponent;
		this.nonAuditedComponent = nonAuditedComponent;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public Component4 getAuditedComponent() {
		return auditedComponent;
	}

	public void setAuditedComponent(Component4 auditedComponent) {
		this.auditedComponent = auditedComponent;
	}

	public Component4 getNonAuditedComponent() {
		return nonAuditedComponent;
	}

	public void setNonAuditedComponent(Component4 nonAuditedComponent) {
		this.nonAuditedComponent = nonAuditedComponent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( auditedComponent == null ) ? 0 : auditedComponent.hashCode() );
		result = prime * result + ( ( str1 == null ) ? 0 : str1.hashCode() );
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) return true;
		if ( !( obj instanceof Component3 ) ) return false;

		Component3 other = (Component3) obj;

		if ( auditedComponent != null ? !auditedComponent.equals( other.auditedComponent ) : other.auditedComponent != null ) return false;
		if ( str1 != null ? !str1.equals( other.str1 ) : other.str1 != null ) return false;

		return true;
	}

	@Override
	public String toString() {
		return "Component3[str1 = " + str1 + ", auditedComponent = "
				+ auditedComponent + ", nonAuditedComponent = "
				+ nonAuditedComponent + "]";
	}
}
