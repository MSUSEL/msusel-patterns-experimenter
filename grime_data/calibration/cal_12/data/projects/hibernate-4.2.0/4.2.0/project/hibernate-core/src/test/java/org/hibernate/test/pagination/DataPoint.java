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
package org.hibernate.test.pagination;
import java.math.BigDecimal;

/**
 * @author Gavin King
 */
public class DataPoint {
	private long id;
	private int sequence;
	private BigDecimal x;
	private BigDecimal y;
	private String description;

	/**
	 * @return Returns the id.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Getter for property 'sequence'.
	 *
	 * @return Value for property 'sequence'.
	 */
	public int getSequence() {
		return sequence;
	}

	/**
	 * Setter for property 'sequence'.
	 *
	 * @param sequence Value to set for property 'sequence'.
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the x.
	 */
	public BigDecimal getX() {
		return x;
	}

	/**
	 * @param x The x to set.
	 */
	public void setX(BigDecimal x) {
		this.x = x;
	}

	/**
	 * @return Returns the y.
	 */
	public BigDecimal getY() {
		return y;
	}

	/**
	 * @param y The y to set.
	 */
	public void setY(BigDecimal y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		DataPoint dataPoint = (DataPoint) o;

		if ( sequence != dataPoint.sequence ) {
			return false;
		}
		if ( description != null ? !description.equals( dataPoint.description ) : dataPoint.description != null ) {
			return false;
		}
		if ( x != null ? !x.equals( dataPoint.x ) : dataPoint.x != null ) {
			return false;
		}
		if ( y != null ? !y.equals( dataPoint.y ) : dataPoint.y != null ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = sequence;
		result = 31 * result + ( x != null ? x.hashCode() : 0 );
		result = 31 * result + ( y != null ? y.hashCode() : 0 );
		result = 31 * result + ( description != null ? description.hashCode() : 0 );
		return result;
	}
}
