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
package org.hibernate.envers.test.integration.onetoone.bidirectional.primarykeyjoincolumn;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Proxy;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@Entity
@Proxy(lazy = false)
public class NotAuditedNoProxyPerson implements Serializable {
    @Id
    @Column(name = "PERSON_ID")
    @GeneratedValue(generator = "NotAuditedNoProxyKeyGenerator")
    @GenericGenerator(name = "NotAuditedNoProxyKeyGenerator", strategy = "foreign",
                      parameters = {@Parameter(name = "property", value = "account")})
    private Long personId;

    private String name;

    @OneToOne(optional = false)
    @PrimaryKeyJoinColumn(name = "PERSON_ID", referencedColumnName = "ACCOUNT_ID")
    private AccountNotAuditedOwners account;

    public NotAuditedNoProxyPerson() {
    }

    public NotAuditedNoProxyPerson(String name) {
        this.name = name;
    }

    public NotAuditedNoProxyPerson(Long personId, String name) {
        this.personId = personId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotAuditedNoProxyPerson)) return false;

        NotAuditedNoProxyPerson person = (NotAuditedNoProxyPerson) o;

        if (personId != null ? !personId.equals(person.personId) : person.personId != null) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = personId != null ? personId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NotAuditedNoProxyPerson(personId = " + personId + ", name = " + name + ")";
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountNotAuditedOwners getAccount() {
        return account;
    }

    public void setAccount(AccountNotAuditedOwners account) {
        this.account = account;
    }
}
