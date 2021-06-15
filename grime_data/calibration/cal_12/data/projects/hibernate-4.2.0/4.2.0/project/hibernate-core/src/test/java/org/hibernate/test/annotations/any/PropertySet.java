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
package org.hibernate.test.annotations.any;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.MetaValue;

@Entity
@Table( name = "property_set" )
public class PropertySet {
	private Integer id;
	private String name;
	private Property someProperty;

	private List<Property> generalProperties = new ArrayList<Property>();

	public PropertySet() {
		super();
	}

	public PropertySet(String name) {
		this.name = name;
	}

	@ManyToAny(
			metaColumn = @Column( name = "property_type" ) )
	@AnyMetaDef( idType = "integer", metaType = "string",
			metaValues = {
			@MetaValue( value = "S", targetEntity = StringProperty.class ),
			@MetaValue( value = "I", targetEntity = IntegerProperty.class ) } )
	@Cascade( { org.hibernate.annotations.CascadeType.ALL } )
	@JoinTable( name = "obj_properties", joinColumns = @JoinColumn( name = "obj_id" ),
			inverseJoinColumns = @JoinColumn( name = "property_id" ) )
	public List<Property> getGeneralProperties() {
		return generalProperties;
	}

	public void setGeneralProperties(List<Property> generalProperties) {
		this.generalProperties = generalProperties;
	}

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

	@Any( metaColumn = @Column( name = "property_type" ) )
	@Cascade( value = { CascadeType.ALL } )
	@AnyMetaDef( idType = "integer", metaType = "string", metaValues = {
	@MetaValue( value = "S", targetEntity = StringProperty.class ),
	@MetaValue( value = "I", targetEntity = IntegerProperty.class )
			} )
	@JoinColumn( name = "property_id" )
	public Property getSomeProperty() {
		return someProperty;
	}

	public void setSomeProperty(Property someProperty) {
		this.someProperty = someProperty;
	}

	public void addGeneratedProperty(Property property) {
		this.generalProperties.add( property );
	}
}
