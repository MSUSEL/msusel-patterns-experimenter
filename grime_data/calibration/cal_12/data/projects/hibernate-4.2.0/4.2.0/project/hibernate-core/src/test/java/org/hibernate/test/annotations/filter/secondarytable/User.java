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
package org.hibernate.test.annotations.filter.secondarytable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SqlFragmentAlias;
import org.hibernate.annotations.Type;

@Entity
@Table(name="T_USER")
@SecondaryTable(name="SECURITY_USER")
@FilterDef(name="ageFilter", parameters=@ParamDef(name="age", type="integer"))
@Filter(name="ageFilter", condition="{u}.AGE < :age AND {s}.LOCKED_OUT <> 1", 
				aliases={@SqlFragmentAlias(alias="u", table="T_USER"), @SqlFragmentAlias(alias="s", table="SECURITY_USER")})
public class User {
	
	@Id
	@GeneratedValue
	@Column(name="USER_ID")
	private int id;
	
	@Column(name="EMAIL_ADDRESS")
	private String emailAddress;
	
	@Column(name="AGE")
	private int age;
	
	@Column(name="SECURITY_USERNAME", table="SECURITY_USER")
	private String username;
	
	@Column(name="SECURITY_PASSWORD", table="SECURITY_USER")
	private String password;
	
	@Column(name="LOCKED_OUT", table="SECURITY_USER")
	@Type( type = "numeric_boolean")
	private boolean lockedOut;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean isLockedOut() {
		return lockedOut;
	}

	public void setLockedOut(boolean lockedOut) {
		this.lockedOut = lockedOut;
	}
	
}
