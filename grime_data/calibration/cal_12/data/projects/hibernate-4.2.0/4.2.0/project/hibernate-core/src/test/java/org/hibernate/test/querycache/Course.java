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
//$Id: Course.java 5686 2005-02-12 07:27:32Z steveebersole $
package org.hibernate.test.querycache;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Gavin King
 */
public class Course implements Serializable {
	private String courseCode;
	private String description;
	private Set courseMeetings = new HashSet();

	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set getCourseMeetings() {
		return courseMeetings;
	}
	public void setCourseMeetings(Set courseMeetings) {
		this.courseMeetings = courseMeetings;
	}

	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || ! ( o instanceof Course ) ) {
			return false;
		}

		Course course = ( Course ) o;

		if ( courseCode != null ? !courseCode.equals( course.getCourseCode() ) : course.getCourseCode() != null ) {
			return false;
		}
		if ( description != null ? !description.equals( course.getDescription() ) : course.getDescription() != null ) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result = courseCode != null ? courseCode.hashCode() : 0;
		result = 31 * result + ( description != null ? description.hashCode() : 0 );
		return result;
	}
}