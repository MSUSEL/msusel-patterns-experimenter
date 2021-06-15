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
package org.hibernate.test.immutable.entitywithmutablecollection;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Contract implements Serializable {
	
	private long id;
	private long version;
	private String customerName;
	private String type;
	private List variations;
	private Contract parent;
	private Set subcontracts;
	private Set plans = new HashSet();
	private Set parties;
	private Set infos;

	public Contract() {
		super();
	}

	public Contract(Plan plan, String customerName, String type) {
		plans = new HashSet();
		if ( plan != null ) {
			plans.add( plan );
			plan.getContracts().add( this );
		}
		this.customerName = customerName;
		this.type = type;
		variations = new ArrayList();
		subcontracts = new HashSet();
		parties = new HashSet();
		infos = new HashSet();
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	public Set getPlans() {
		return plans;
	}

	public void setPlans(Set plans) {
		this.plans = plans;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List getVariations() {
		return variations;
	}

	public void setVariations(List variations) {
		this.variations = variations;
	}

	public Contract getParent() {
		return parent;
	}

	public void setParent(Contract parent) {
		this.parent = parent;
	}

	public Set getSubcontracts() {
		return subcontracts;
	}

	public void setSubcontracts(Set subcontracts) {
		this.subcontracts = subcontracts;
	}

	public void addSubcontract(Contract subcontract) {
		subcontracts.add( subcontract );
		subcontract.setParent( this );
	}

	public Set getParties() {
		return parties;
	}

	public void setParties(Set parties) {
		this.parties = parties;
	}

	public void addParty(Party party) {
		parties.add( party );
		party.setContract( this );
	}

	public void removeParty(Party party) {
		parties.remove( party );
		party.setContract( null );
	}

	public Set getInfos() {
		return infos;
	}

	public void setInfos(Set infos) {
		this.infos = infos;
	}
}
