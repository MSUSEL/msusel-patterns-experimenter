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
import javax.persistence.Embedded;

/**
 * 
 * @author Erik-Berndt Scheper
 * 
 */
public class DefaultValueComponent1 {

	private String str1;

	@Embedded
	private DefaultValueComponent2 comp2 = new DefaultValueComponent2();

	public static final DefaultValueComponent1 of(String str1,
			DefaultValueComponent2 comp2) {
		DefaultValueComponent1 instance = new DefaultValueComponent1();
		instance.setStr1(str1);
		instance.setComp2(comp2);
		return instance;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public DefaultValueComponent2 getComp2() {
		return comp2;
	}

	public void setComp2(DefaultValueComponent2 comp2) {
		this.comp2 = comp2;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DefaultValueComponent1))
			return false;

		DefaultValueComponent1 that = (DefaultValueComponent1) o;

		if (str1 != null ? !str1.equals(that.str1) : that.str1 != null)
			return false;
		if (comp2 != null ? !comp2.equals(that.comp2) : that.comp2 != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result;
		result = (str1 != null ? str1.hashCode() : 0);
		result = 31 * result + (comp2 != null ? comp2.hashCode() : 0);
		return result;
	}

	public String toString() {
		return "Comp1(str1 = " + str1 + ", comp2 = " + comp2 + ")";
	}

}
