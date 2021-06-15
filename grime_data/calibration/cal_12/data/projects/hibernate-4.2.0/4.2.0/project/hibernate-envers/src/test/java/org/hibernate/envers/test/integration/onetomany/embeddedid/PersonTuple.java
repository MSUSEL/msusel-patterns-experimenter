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
package org.hibernate.envers.test.integration.onetomany.embeddedid;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@Entity
@Audited
public class PersonTuple implements Serializable {
    @Embeddable
    public static class PersonTupleId implements Serializable {
        @Column(nullable = false)
        private long personAId;

        @Column(nullable = false)
        private long personBId;

        @Column(nullable = false)
        private String constantId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PersonTupleId)) return false;

            PersonTupleId that = (PersonTupleId) o;

            if (personAId != that.personAId) return false;
            if (personBId != that.personBId) return false;
            if (constantId != null ? !constantId.equals(that.constantId) : that.constantId != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (int) (personAId ^ (personAId >>> 32));
            result = 31 * result + (int) (personBId ^ (personBId >>> 32));
            result = 31 * result + (constantId != null ? constantId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "PersonTupleId(personAId = " + personAId + ", personBId = " + personBId + ", constantId = " + constantId + ")";
        }

        public long getPersonAId() {
            return personAId;
        }

        public void setPersonAId(long personAId) {
            this.personAId = personAId;
        }

        public long getPersonBId() {
            return personBId;
        }

        public void setPersonBId(long personBId) {
            this.personBId = personBId;
        }

        public String getConstantId() {
            return constantId;
        }

        public void setConstantId(String constantId) {
            this.constantId = constantId;
        }
    }

    @EmbeddedId
    private PersonTupleId personTupleId = new PersonTupleId();

    @MapsId("personAId")
    @ManyToOne(optional = false)
    @JoinColumn(insertable = false, updatable = false, nullable=false)
    private Person personA;

    @MapsId("personBId")
    @ManyToOne(optional = false)
    @JoinColumn(insertable = false, updatable = false, nullable=false)
    private Person personB;

    @MapsId("constantId")
    @ManyToOne(optional = false)
    @JoinColumn(insertable = false, updatable = false, nullable=false)
    private Constant constant;

    @Column(nullable = false)
    private boolean helloWorld = false;

    public PersonTuple() {
    }

    public PersonTuple(boolean helloWorld, Person personA, Person personB, Constant constant) {
        this.helloWorld = helloWorld;
        this.personA = personA;
        this.personB = personB;
        this.constant = constant;

        this.personTupleId.personAId = personA.getId();
        this.personTupleId.personBId = personB.getId();
        this.personTupleId.constantId = constant.getId();

        personA.getPersonATuples().add(this);
        personB.getPersonBTuples().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonTuple)) return false;

        PersonTuple that = (PersonTuple) o;

        return personTupleId.equals(that.personTupleId);
    }

    @Override
    public int hashCode() {
        return personTupleId.hashCode();
    }

    @Override
    public String toString() {
        return "PersonTuple(id = " + personTupleId + ", helloWorld = " + helloWorld + ")";
    }

    public PersonTupleId getPersonTupleId() {
        return personTupleId;
    }

    public void setPersonTupleId(PersonTupleId personTupleId) {
        this.personTupleId = personTupleId;
    }

    public Person getPersonA() {
        return personA;
    }

    public void setPersonA(Person personA) {
        this.personA = personA;
    }

    public Person getPersonB() {
        return personB;
    }

    public void setPersonB(Person personB) {
        this.personB = personB;
    }

    public Constant getConstant() {
        return constant;
    }

    public void setConstant(Constant constant) {
        this.constant = constant;
    }

    public boolean isHelloWorld() {
        return helloWorld;
    }

    public void setHelloWorld(boolean helloWorld) {
        this.helloWorld = helloWorld;
    }
}