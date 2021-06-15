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
package org.hibernate.envers.test.performance.complex;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
@Audited
public class RootEntity {
    @Id
    private Long id;

    private String data1;

    private String data2;

    private Integer number1;

    private Integer number2;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date1;

    @ManyToOne(cascade = CascadeType.ALL)
    private ChildEntity1 child1;

    @ManyToOne(cascade = CascadeType.ALL)
    private ChildEntity1 child2;

    @ManyToOne(cascade = CascadeType.ALL)
    private ChildEntity1 child3;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public Integer getNumber1() {
        return number1;
    }

    public void setNumber1(Integer number1) {
        this.number1 = number1;
    }

    public Integer getNumber2() {
        return number2;
    }

    public void setNumber2(Integer number2) {
        this.number2 = number2;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public ChildEntity1 getChild1() {
        return child1;
    }

    public void setChild1(ChildEntity1 child1) {
        this.child1 = child1;
    }

    public ChildEntity1 getChild2() {
        return child2;
    }

    public void setChild2(ChildEntity1 child2) {
        this.child2 = child2;
    }

    public ChildEntity1 getChild3() {
        return child3;
    }

    public void setChild3(ChildEntity1 child3) {
        this.child3 = child3;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RootEntity)) return false;

        RootEntity that = (RootEntity) o;

        if (data1 != null ? !data1.equals(that.data1) : that.data1 != null) return false;
        if (data2 != null ? !data2.equals(that.data2) : that.data2 != null) return false;
        if (date1 != null ? !date1.equals(that.date1) : that.date1 != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (number1 != null ? !number1.equals(that.number1) : that.number1 != null) return false;
        if (number2 != null ? !number2.equals(that.number2) : that.number2 != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (data1 != null ? data1.hashCode() : 0);
        result = 31 * result + (data2 != null ? data2.hashCode() : 0);
        result = 31 * result + (number1 != null ? number1.hashCode() : 0);
        result = 31 * result + (number2 != null ? number2.hashCode() : 0);
        result = 31 * result + (date1 != null ? date1.hashCode() : 0);
        return result;
    }
}
