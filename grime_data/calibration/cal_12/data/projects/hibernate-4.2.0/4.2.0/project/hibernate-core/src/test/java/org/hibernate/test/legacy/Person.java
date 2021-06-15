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
package org.hibernate.test.legacy;


/**
 * @author hbm2java
 */
public class Person extends org.hibernate.test.legacy.Party {

   java.lang.String id;
   java.lang.String givenName;
   java.lang.String lastName;
   java.lang.String nationalID;


  java.lang.String getId() {
    return id;
  }

  void  setId(java.lang.String newValue) {
    id = newValue;
  }

  java.lang.String getGivenName() {
    return givenName;
  }

  void  setGivenName(java.lang.String newValue) {
    givenName = newValue;
  }

  java.lang.String getLastName() {
    return lastName;
  }

  void  setLastName(java.lang.String newValue) {
    lastName = newValue;
  }

  java.lang.String getNationalID() {
    return nationalID;
  }

  void  setNationalID(java.lang.String newValue) {
    nationalID = newValue;
  }


}
