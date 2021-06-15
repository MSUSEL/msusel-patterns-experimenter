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
package org.hibernate.ejb.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.jboss.logging.Logger;

/**
 * @author Emmanuel Bernard
 */
@SuppressWarnings({"unchecked", "serial"})
@Entity
@EntityListeners( LastUpdateListener.class )
public class Cat implements Serializable {
	private static final Logger log = Logger.getLogger( Cat.class );

	private static final List ids = new ArrayList(); 	// used for assertions
	public static int postVersion = 0;	// used for assertions

	private Integer id;
	private String name;
	private Date dateOfBirth;
	private int age;
	private long length;
	private Date lastUpdate;
	private int manualVersion = 0;
	private List<Kitten> kittens;

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int getManualVersion() {
		return manualVersion;
	}

	public void setManualVersion(int manualVersion) {
		this.manualVersion = manualVersion;
	}

	@Transient
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Basic
	@Temporal( TemporalType.TIMESTAMP )
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@PostUpdate
	private void someLateUpdateWorking() {
        log.debug("PostUpdate for: " + this.toString());
		postVersion++;
	}

	@PostLoad
	public void calculateAge() {
		Calendar birth = new GregorianCalendar();
		birth.setTime( dateOfBirth );
		Calendar now = new GregorianCalendar();
		now.setTime( new Date() );
		int adjust = 0;
		if ( now.get( Calendar.DAY_OF_YEAR ) - birth.get( Calendar.DAY_OF_YEAR ) < 0 ) {
			adjust = -1;
		}
		age = now.get( Calendar.YEAR ) - birth.get( Calendar.YEAR ) + adjust;
	}

	@PostPersist
	public synchronized void addIdsToList() {
		ids.add( getId() );
	}

	public static synchronized List getIdList() {
		return Collections.unmodifiableList( ids );
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	@OneToMany(cascade = CascadeType.ALL)
	public List<Kitten> getKittens() {
		return kittens;
	}

	public void setKittens(List<Kitten> kittens) {
		this.kittens = kittens;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation
	 * of this object.
	 */
	@Override
    public String toString()
	{
	    final String TAB = "    ";

	    String retValue = "";

	    retValue = "Cat ( "
	        + super.toString() + TAB
	        + "id = " + this.id + TAB
	        + "name = " + this.name + TAB
	        + "dateOfBirth = " + this.dateOfBirth + TAB
	        + "age = " + this.age + TAB
	        + "length = " + this.length + TAB
	        + "lastUpdate = " + this.lastUpdate + TAB
	        + "manualVersion = " + this.manualVersion + TAB
	        + "postVersion = " + Cat.postVersion + TAB
	        + "kittens = " + this.kittens + TAB
	        + " )";

	    return retValue;
	}
}
