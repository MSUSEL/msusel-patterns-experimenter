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
package org.hibernate.envers.test.integration.basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
public class BasicTestEntity3 {
    @Id
    @GeneratedValue
    private Integer id;

    private String str1;

    private String str2;

    public BasicTestEntity3() {
    }

    public BasicTestEntity3(String str1, String str2) {
        this.str1 = str1;
        this.str2 = str2;
    }

    public BasicTestEntity3(Integer id, String str1, String str2) {
        this.id = id;
        this.str1 = str1;
        this.str2 = str2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicTestEntity3)) return false;

        BasicTestEntity3 that = (BasicTestEntity3) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (str1 != null ? !str1.equals(that.str1) : that.str1 != null) return false;
        if (str2 != null ? !str2.equals(that.str2) : that.str2 != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (str1 != null ? str1.hashCode() : 0);
        result = 31 * result + (str2 != null ? str2.hashCode() : 0);
        return result;
    }
}