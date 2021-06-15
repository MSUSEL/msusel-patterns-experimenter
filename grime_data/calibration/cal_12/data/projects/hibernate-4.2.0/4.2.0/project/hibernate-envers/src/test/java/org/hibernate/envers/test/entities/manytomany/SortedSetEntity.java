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
package org.hibernate.envers.test.entities.manytomany;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.envers.Audited;
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.envers.test.entities.StrTestEntityComparator;

/**
 * Entity with custom-ordered SortedSet and SortedMap
 *
 * @author Michal Skowronek (mskowr at o2 pl)
 */
@Entity
public class SortedSetEntity {
    @Id
    private Integer id;

    @Audited
    private String data;

    @Audited
    @ManyToMany
    @Sort(type = SortType.COMPARATOR, comparator = StrTestEntityComparator.class)
    private SortedSet<StrTestEntity> sortedSet = new TreeSet<StrTestEntity>(StrTestEntityComparator.INSTANCE);
    @Audited
	@ElementCollection
	@MapKeyJoinColumn
    @Sort(type = SortType.COMPARATOR, comparator = StrTestEntityComparator.class)
    private SortedMap<StrTestEntity, String> sortedMap = new TreeMap<StrTestEntity, String>(StrTestEntityComparator.INSTANCE);

    public SortedSetEntity() {
    }

    public SortedSetEntity(Integer id, String data) {
        this.id = id;
        this.data = data;
    }

    public SortedSetEntity(String data) {
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public SortedSet<StrTestEntity> getSortedSet() {
        return sortedSet;
    }

    public void setSortedSet(SortedSet<StrTestEntity> sortedSet) {
        this.sortedSet = sortedSet;
    }

	public SortedMap<StrTestEntity, String> getSortedMap() {
		return sortedMap;
	}

	public void setSortedMap(SortedMap<StrTestEntity, String> sortedMap) {
		this.sortedMap = sortedMap;
	}

	public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SortedSetEntity)) return false;

        SortedSetEntity that = (SortedSetEntity) o;

		return !(data != null ? !data.equals(that.data) : that.data != null) && !(id != null ? !id.equals(that.id) : that.id != null);
	}

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "SetOwnedEntity(id = " + id + ", data = " + data + ")";
    }
}
