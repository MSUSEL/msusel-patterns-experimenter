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
package org.hibernate.test.collectionalias;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Dave Stephan
 */
@Embeddable
public class TableBId implements Serializable
{
		private static final long serialVersionUID = 1L;

		// Fields

		private Integer firstId;

		private String secondId;

		private String thirdId;

		// Constructors

		/** default constructor */
		public TableBId() {
		}

		/** full constructor */
		public TableBId(Integer firstId, String secondId, String thirdId) {
			this.firstId = firstId;
			this.secondId = secondId;
			this.thirdId = thirdId;
		}

		// Property accessors

		@Column(name = "idcolumn", nullable = false)
		public Integer getFirstId() {
			return this.firstId;
		}

		public void setFirstId(Integer firstId) {
			this.firstId = firstId;
		}

		@Column(name = "idcolumn_second", nullable = false, length = 50)
		public String getSecondId() {
			return this.secondId;
		}

		public void setSecondId(String secondId) {
			this.secondId = secondId;
		}

	@Column(name = "thirdcolumn", nullable = false, length = 50)
		public String getThirdId() {
			return this.thirdId;
		}

		public void setThirdId(String thirdId) {
			this.thirdId = thirdId;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((firstId == null) ? 0 : firstId.hashCode());
			result = prime * result + ((secondId == null) ? 0 : secondId.hashCode());
			result = prime * result + ((thirdId == null) ? 0 : thirdId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TableBId other = (TableBId) obj;
			if (firstId == null)
			{
				if (other.firstId != null)
					return false;
			}
			else if (!firstId.equals(other.firstId))
				return false;
			if (secondId == null)
			{
				if (other.secondId != null)
					return false;
			}
			else if (!secondId.equals(other.secondId))
				return false;
			if (thirdId == null)
			{
				if (other.thirdId != null)
					return false;
			}
			else if (!thirdId.equals(other.thirdId))
				return false;
			return true;
		}
}
