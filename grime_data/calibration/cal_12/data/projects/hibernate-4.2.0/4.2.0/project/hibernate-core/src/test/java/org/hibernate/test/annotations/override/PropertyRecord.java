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
package org.hibernate.test.annotations.override;
import java.util.Map;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Emmanuel Bernard
 */
@Entity
public class PropertyRecord {
	@Id
	@GeneratedValue
	public Long id;

	@AttributeOverrides({
			@AttributeOverride(name = "key.street", column = @Column(name = "STREET_NAME")),
			@AttributeOverride(name = "value.size", column = @Column(name = "SQUARE_FEET")),
			@AttributeOverride(name = "value.tax", column = @Column(name = "ASSESSMENT"))
					})
	@ElementCollection
	public Map<Address, PropertyInfo> parcels;

	@AttributeOverrides({
			@AttributeOverride(name = "key.street", column = @Column(name = "STREET_NAME")),
			@AttributeOverride(name = "value.size", column = @Column(name = "SQUARE_FEET")),
			@AttributeOverride(name = "value.tax", column = @Column(name = "ASSESSMENT"))
					})
	@ElementCollection
	@CollectionTable(name="LegacyParcels")
	public Map<Address, PropertyInfo> legacyParcels;

	@AttributeOverrides({
			@AttributeOverride(name = "size", column = @Column(name = "SQUARE_FEET")),
			@AttributeOverride(name = "tax", column = @Column(name = "ASSESSMENT"))
					})
	@ElementCollection
	public Set<PropertyInfo> unsortedParcels;

	@AttributeOverrides({
			@AttributeOverride(name = "size", column = @Column(name = "SQUARE_FEET")),
			@AttributeOverride(name = "tax", column = @Column(name = "ASSESSMENT"))
					})
	@ElementCollection
	public Set<PropertyInfo> legacyUnsortedParcels;
}