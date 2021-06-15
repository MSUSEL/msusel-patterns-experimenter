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
package org.hibernate.test.annotations.indexcoll;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.MapKeyJoinColumns;
import javax.persistence.MapKeyTemporal;
import javax.persistence.TemporalType;

/**
 * @author Emmanuel Bernard
 */
@Entity
public class Atmosphere {

	public static enum Level {
		LOW,
		HIGH
	}

	@Id
	@GeneratedValue
	public Integer id;

	@ManyToMany(cascade = CascadeType.ALL)
	@MapKeyColumn(name="gas_name")
	public Map<String, Gas> gases = new HashMap<String, Gas>();

	@MapKeyTemporal(TemporalType.DATE)
	@ElementCollection
	@MapKeyColumn(nullable=false)
	public Map<Date, String> colorPerDate = new HashMap<Date,String>();

	@ElementCollection
	@MapKeyEnumerated(EnumType.STRING)
	@MapKeyColumn(nullable=false)
	public Map<Level, String> colorPerLevel = new HashMap<Level,String>();

	@ManyToMany(cascade = CascadeType.ALL)
	@MapKeyJoinColumn(name="gas_id" )
	@JoinTable(name = "Gas_per_key")
	public Map<GasKey, Gas> gasesPerKey = new HashMap<GasKey, Gas>();

	@ElementCollection
	@Column(name="composition_rate")
	@MapKeyJoinColumns( { @MapKeyJoinColumn(name="gas_id" ) } ) //use @MapKeyJoinColumns explicitly for tests
	@JoinTable(name = "Composition", joinColumns = @JoinColumn(name = "atmosphere_id"))
	public Map<Gas, Double> composition = new HashMap<Gas, Double>();

	//use default JPA 2 column name for map key
	@ManyToMany(cascade = CascadeType.ALL)
	@MapKeyColumn
	@JoinTable(name="Atm_Gas_Def")
	public Map<String, Gas> gasesDef = new HashMap<String, Gas>();

	//use default HAN legacy column name for map key
	@ManyToMany(cascade = CascadeType.ALL)
	@MapKeyColumn
	@JoinTable(name="Atm_Gas_DefLeg")
	public Map<String, Gas> gasesDefLeg = new HashMap<String, Gas>();

	@ManyToMany(cascade = CascadeType.ALL)
	@MapKeyJoinColumn
	@JoinTable(name = "Gas_p_key_def")
	public Map<GasKey, Gas> gasesPerKeyDef = new HashMap<GasKey, Gas>();

}
