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

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.envers.Audited;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@Entity
@Audited
public class ScalePrecisionEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(precision = 3, scale = 0)
    private Double wholeNumber;

    public ScalePrecisionEntity() {
    }

    public ScalePrecisionEntity(Double wholeNumber) {
        this.wholeNumber = wholeNumber;
    }

    public ScalePrecisionEntity(Double wholeNumber, Long id) {
        this.id = id;
        this.wholeNumber = wholeNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScalePrecisionEntity)) return false;

        ScalePrecisionEntity that = (ScalePrecisionEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (wholeNumber != null ? !wholeNumber.equals(that.wholeNumber) : that.wholeNumber != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (wholeNumber != null ? wholeNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ScalePrecisionEntity(id = " + id + ", wholeNumber = " + wholeNumber + ")";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWholeNumber() {
        return wholeNumber;
    }

    public void setWholeNumber(Double wholeNumber) {
        this.wholeNumber = wholeNumber;
    }
}
