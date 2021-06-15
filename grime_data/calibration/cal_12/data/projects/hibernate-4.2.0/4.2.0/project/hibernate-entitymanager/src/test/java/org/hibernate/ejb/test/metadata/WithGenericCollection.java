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
package org.hibernate.ejb.test.metadata;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
* This class has a List of mapped entity objects that are themselves parameterized.
* This class was added for JIRA issue #HHH-
*
* @author Kahli Burke
*/
@Entity
@Table(name = "WITH_GENERIC_COLLECTION")
public class WithGenericCollection<T> implements java.io.Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    @Basic(optional=false)
    private double d;

    @ManyToOne(optional=false)
    @JoinColumn(name="PARENT_ID", insertable=false, updatable=false)
    private WithGenericCollection<? extends Object> parent = null;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="PARENT_ID")
    private List<WithGenericCollection<? extends Object>> children = new ArrayList<WithGenericCollection<? extends Object>>();

    public WithGenericCollection() {
    }

    //====================================================================
    // getters and setters for State fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getD() {
        return d;
    }

    public List<WithGenericCollection<? extends Object>> getChildren() {
        return children;
    }

    public void setChildren(List<WithGenericCollection<? extends Object>> children) {
        this.children = children;
    }


}
