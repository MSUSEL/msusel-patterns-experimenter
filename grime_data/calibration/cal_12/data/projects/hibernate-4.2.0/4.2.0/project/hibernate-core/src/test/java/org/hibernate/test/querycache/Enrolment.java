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
//$Id: Enrolment.java 6970 2005-05-31 20:24:41Z oneovthafew $
package org.hibernate.test.querycache;
import java.io.Serializable;

/**
 * @author Gavin King
 */
public class Enrolment implements Serializable {
	private Student student;
	private Course course;
	private long studentNumber;
	private String courseCode;
	private short year;
	private short semester;

	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseId) {
		this.courseCode = courseId;
	}
	public long getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(long studentId) {
		this.studentNumber = studentId;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public short getSemester() {
		return semester;
	}
	public void setSemester(short semester) {
		this.semester = semester;
	}
	public short getYear() {
		return year;
	}
	public void setYear(short year) {
		this.year = year;
	}

	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		Enrolment enrolment = ( Enrolment ) o;

		if ( semester != enrolment.semester ) {
			return false;
		}
		if ( studentNumber != enrolment.studentNumber ) {
			return false;
		}
		if ( year != enrolment.year ) {
			return false;
		}
		if ( courseCode != null ? !courseCode.equals( enrolment.courseCode ) : enrolment.courseCode != null ) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result = ( int ) ( studentNumber ^ ( studentNumber >>> 32 ) );
		result = 31 * result + ( courseCode != null ? courseCode.hashCode() : 0 );
		result = 31 * result + ( int ) year;
		result = 31 * result + ( int ) semester;
		return result;
	}
}