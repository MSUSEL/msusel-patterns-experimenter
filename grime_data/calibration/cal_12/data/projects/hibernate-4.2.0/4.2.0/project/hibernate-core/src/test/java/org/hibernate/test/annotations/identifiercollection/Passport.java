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
package org.hibernate.test.annotations.identifiercollection;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Type;

/**
 * @author Emmanuel Bernard
 */
@Entity
@TableGenerator(name="ids_generator", table="IDS")
public class Passport {
	@Id @GeneratedValue @Column(name="passport_id") private Long id;
	private String name;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="PASSPORT_STAMP")
	@CollectionId(columns = @Column(name="COLLECTION_ID"), type=@Type(type="long"), generator = "generator")
	@TableGenerator(name="generator", table="IDSTAMP")
	private Collection<Stamp> stamps = new ArrayList();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="PASSPORT_VISASTAMP")
	@CollectionId(columns = @Column(name="COLLECTION_ID"), type=@Type(type="long"), generator = "ids_generator")
	//TODO test identity generator
	private Collection<Stamp> visaStamp = new ArrayList();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Stamp> getStamps() {
		return stamps;
	}

	public void setStamps(Collection<Stamp> stamps) {
		this.stamps = stamps;
	}

	public Collection<Stamp> getVisaStamp() {
		return visaStamp;
	}

	public void setVisaStamp(Collection<Stamp> visaStamp) {
		this.visaStamp = visaStamp;
	}
}
