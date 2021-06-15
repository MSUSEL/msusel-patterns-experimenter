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
//$Id$
package org.hibernate.test.annotations.onetoone;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;


/**
 * @author Emmanuel Bernard
 */
@Entity
public class Computer {

	private ComputerPk id;
	private String cpu;
	private SerialNumber serial;

	@OneToOne(cascade = {CascadeType.PERSIST})
	@JoinColumns({
	@JoinColumn(name = "serialbrand", referencedColumnName = "brand"),
	@JoinColumn(name = "serialmodel", referencedColumnName = "model")
			})
	public SerialNumber getSerial() {
		return serial;
	}

	public void setSerial(SerialNumber serial) {
		this.serial = serial;
	}

	public boolean equals(Object o) {
		if ( this == o ) return true;
		if ( !( o instanceof Computer ) ) return false;

		final Computer computer = (Computer) o;

		if ( !id.equals( computer.id ) ) return false;

		return true;
	}

	public int hashCode() {
		return id.hashCode();
	}

	@EmbeddedId
	@AttributeOverrides({
	@AttributeOverride(name = "brand", column = @Column(name = "computer_brand")),
	@AttributeOverride(name = "model", column = @Column(name = "computer_model"))
			})
	public ComputerPk getId() {
		return id;
	}

	public void setId(ComputerPk id) {
		this.id = id;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
}
