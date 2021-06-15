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
package org.hibernate.envers.test.integration.modifiedflags.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.envers.test.entities.components.Component1;
import org.hibernate.envers.test.entities.components.Component2;

/**
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
@Entity
@Table(name = "PartialModFlags")
@Audited(withModifiedFlag = false)
public class PartialModifiedFlagsEntity {
    @Id
    private Integer id;

    @Audited(withModifiedFlag = true)
    private String data;

	@Audited(withModifiedFlag = true)
	@Embedded
    private Component1 comp1;

	@Audited(withModifiedFlag = false)
	@Embedded
    private Component2 comp2;

    @Audited(withModifiedFlag = true)
    @OneToOne(mappedBy="reference")
    private WithModifiedFlagReferencingEntity referencing;

	@Audited(withModifiedFlag = false)
    @OneToOne(mappedBy="secondReference")
    private WithModifiedFlagReferencingEntity referencing2;

    @Audited(withModifiedFlag = true)
    @ElementCollection
    @JoinTable(name = "PartialModFlags_StrSet")
    @AuditJoinTable(name = "PartialModFlags_StrSet_AUD")
    private Set<String> stringSet = new HashSet<String>();

	@Audited(withModifiedFlag = true)
	@ManyToMany
	@CollectionTable(name = "ENTITIESSET")
    private Set<StrTestEntity> entitiesSet = new HashSet<StrTestEntity>();

	@Audited(withModifiedFlag = true)
	@ElementCollection
	@MapKeyColumn(nullable=false)
    @JoinTable(name = "PartialModFlags_StrMap")
    @AuditJoinTable(name = "PartialModFlags_StrMap_AUD")
    private Map<String, String> stringMap = new HashMap<String, String>();

	@Audited(withModifiedFlag = true)
    @ManyToMany
    @CollectionTable(name = "ENTITIESMAP")
    @MapKeyColumn(nullable = false)
    private Map<String, StrTestEntity> entitiesMap =
			new HashMap<String, StrTestEntity>();

	public PartialModifiedFlagsEntity() {
	}

	public PartialModifiedFlagsEntity(Integer id) {
		this.id = id;
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

    public WithModifiedFlagReferencingEntity getReferencing() {
        return referencing;
    }

    public void setReferencing(WithModifiedFlagReferencingEntity referencing) {
        this.referencing = referencing;
    }

	public WithModifiedFlagReferencingEntity getReferencing2() {
        return referencing2;
    }

    public void setReferencing2(WithModifiedFlagReferencingEntity referencing) {
        this.referencing2 = referencing;
    }

	public Component1 getComp1() {
		return comp1;
	}

	public void setComp1(Component1 comp1) {
		this.comp1 = comp1;
	}

	public Component2 getComp2() {
		return comp2;
	}

	public void setComp2(Component2 comp2) {
		this.comp2 = comp2;
	}

	public Set<String> getStringSet() {
		return stringSet;
	}

	public void setStringSet(Set<String> stringSet) {
		this.stringSet = stringSet;
	}

	public Set<StrTestEntity> getEntitiesSet() {
		return entitiesSet;
	}

	public void setEntitiesSet(Set<StrTestEntity> entitiesSet) {
		this.entitiesSet = entitiesSet;
	}

	public Map<String, String> getStringMap() {
		return stringMap;
	}

	public void setStringMap(Map<String, String> stringMap) {
		this.stringMap = stringMap;
	}

	public Map<String, StrTestEntity> getEntitiesMap() {
		return entitiesMap;
	}

	public void setEntitiesMap(Map<String, StrTestEntity> entitiesMap) {
		this.entitiesMap = entitiesMap;
	}

	public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PartialModifiedFlagsEntity)) return false;

        PartialModifiedFlagsEntity that = (PartialModifiedFlagsEntity) o;

        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }
}
