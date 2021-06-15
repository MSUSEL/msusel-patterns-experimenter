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
package org.hibernate.test.annotations;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;

/**
 * Plane class
 *
 * @author Emmanuel Bernard
 */
@Entity()
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "planetype", length = 100, discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Plane")
@AttributeOverride(name = "altitude", column = @Column(name = "fld_altitude"))
@NamedQuery(name = "plane.byId", query = "from Plane where id = :id",
		hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true"),
		@QueryHint(name = "org.hibernate.cacheRegion", value = "testedCacheRegion"),
		@QueryHint(name = "org.hibernate.timeout", value = "100"),
		@QueryHint(name = "org.hibernate.fetchSize", value = "1"),
		@QueryHint(name = "org.hibernate.flushMode", value = "Commit"),
		@QueryHint(name = "org.hibernate.cacheMode", value = "NORMAL"),
		@QueryHint(name = "org.hibernate.comment", value = "Plane by id")})
public class Plane extends FlyingObject {

	private Long id;
	private int nbrofSeats;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public int getNbrOfSeats() {
		return nbrofSeats;
	}

	public void setId(Long long1) {
		id = long1;
	}

	public void setNbrOfSeats(int i) {
		nbrofSeats = i;
	}

}
