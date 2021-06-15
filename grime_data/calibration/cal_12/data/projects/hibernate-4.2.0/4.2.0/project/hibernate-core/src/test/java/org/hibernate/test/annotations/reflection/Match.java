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
package org.hibernate.test.annotations.reflection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * @author Emmanuel Bernard
 */
@Entity
@Table(name = "matchtable", schema = "matchschema")
@SecondaryTable(name = "extendedMatch")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQueries({
@NamedQuery(name = "matchbyid", query = "select m from Match m where m.id = :id"),
@NamedQuery(name = "getAllMatches2", query = "select m from Match m")
		})
@NamedNativeQueries({
@NamedNativeQuery(name = "matchbyid", query = "select m from Match m where m.id = :id", resultSetMapping = "matchrs"),
@NamedNativeQuery(name = "getAllMatches2", query = "select m from Match m", resultSetMapping = "matchrs")
		})
public class Match extends Competition {
	public String competitor1Point;
	@Version
	public Integer version;
	public SocialSecurityNumber playerASSN;
}
