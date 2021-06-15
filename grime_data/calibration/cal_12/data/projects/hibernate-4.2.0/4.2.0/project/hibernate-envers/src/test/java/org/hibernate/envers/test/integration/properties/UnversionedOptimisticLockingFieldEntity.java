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
package org.hibernate.envers.test.integration.properties;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.envers.Audited;

/**
 * @author Nicolas Doroskevich
 */
@Audited
@Table(name = "UnverOptimLockField")
@Entity
public class UnversionedOptimisticLockingFieldEntity {
	
	 @Id
	 @GeneratedValue
	 private Integer id;
	 
	 private String str;
	 
	 @Version
	 private int optLocking;
	 
	 public UnversionedOptimisticLockingFieldEntity() {
	 }
	 
	 public UnversionedOptimisticLockingFieldEntity(String str) {
		 this.str = str;
	 }
	 
	 public UnversionedOptimisticLockingFieldEntity(Integer id, String str) {
		 this.id = id;
		 this.str = str;
	 }
	 
	 public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
	
	public int getOptLocking() {
		return optLocking;
	}

	public void setOptLocking(int optLocking) {
		this.optLocking = optLocking;
	}
	
	public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnversionedOptimisticLockingFieldEntity)) return false;

        UnversionedOptimisticLockingFieldEntity that = (UnversionedOptimisticLockingFieldEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (str != null ? !str.equals(that.str) : that.str != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (str != null ? str.hashCode() : 0);
        return result;
    }
	 
}
