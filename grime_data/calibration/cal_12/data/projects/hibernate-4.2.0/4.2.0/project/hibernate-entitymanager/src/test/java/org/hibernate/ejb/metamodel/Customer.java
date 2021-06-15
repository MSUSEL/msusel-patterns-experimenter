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
package org.hibernate.ejb.metamodel;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
@Entity
@Table(name = "CUSTOMER_TABLE")
public class Customer implements java.io.Serializable {
	private String id;
	private String name;
	private Integer age;
	private Address home;
	private Address work;
	private Country country;
	private Spouse spouse;
	private Collection<CreditCard> creditCards = new java.util.ArrayList<CreditCard>();
	private Collection<Order> orders = new java.util.ArrayList<Order>();
	private Collection<Alias> aliases = new java.util.ArrayList<Alias>();
	private Collection<Alias> aliasesNoop = new java.util.ArrayList<Alias>();

	public Customer() {
	}

	public Customer(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Customer(String id, String name, Country country) {
		this.id = id;
		this.name = name;
		this.country = country;
	}

	public Customer(String id, String name, Address home,
					Address work, Country country) {
		this.id = id;
		this.name = name;
		this.home = home;
		this.work = work;
		this.country = country;
	}

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String v) {
		this.id = v;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String v) {
		this.name = v;
	}

	@Column(name = "AGE")
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Embedded
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country v) {
		this.country = v;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK6_FOR_CUSTOMER_TABLE")
	public Address getHome() {
		return home;
	}

	public void setHome(Address v) {
		this.home = v;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK5_FOR_CUSTOMER_TABLE")
	public Address getWork() {
		return work;
	}

	public void setWork(Address v) {
		this.work = v;
	}

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "customer")
	public Spouse getSpouse() {
		return spouse;
	}

	public void setSpouse(Spouse v) {
		this.spouse = v;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
	public Collection<CreditCard> getCreditCards() {
		return creditCards;
	}

	public void setCreditCards(Collection<CreditCard> v) {
		this.creditCards = v;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
	public Collection<Order> getOrders() {
		return orders;
	}

	public void setOrders(Collection<Order> v) {
		this.orders = v;
	}

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "customers")
	public Collection<Alias> getAliases() {
		return aliases;
	}

	public void setAliases(Collection<Alias> v) {
		this.aliases = v;
	}

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "customersNoop")
	public Collection<Alias> getAliasesNoop() {
		return aliasesNoop;
	}

	public void setAliasesNoop(Collection<Alias> v) {
		this.aliasesNoop = v;
	}
}