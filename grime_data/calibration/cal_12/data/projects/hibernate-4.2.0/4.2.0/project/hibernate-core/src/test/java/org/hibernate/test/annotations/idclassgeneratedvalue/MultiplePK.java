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
package org.hibernate.test.annotations.idclassgeneratedvalue;
import java.io.Serializable;

/**
 * MultiplePK
 *
 * @author <a href="mailto:stale.pedersen@jboss.org">Stale W. Pedersen</a>
 */
public class MultiplePK implements Serializable
{
   private final Long id1;
   private final Long id2;
   private final Long id3;
// AnnotationSourceProcessor (incorrectly) requires this to be transient; see HHH-4819 and HHH-4820
   private final transient int cachedHashCode;

   private MultiplePK()
   {
      id1 = null;
      id2 = null;
      id3 = null;
      cachedHashCode = super.hashCode();
   }
   
   public MultiplePK(Long id1, Long id2, Long id3)
   {
      this.id1 = id1;
      this.id2 = id2;
      this.id3 = id3;
      this.cachedHashCode = calculateHashCode();
   }
   

   private int calculateHashCode() {
       int result = id1.hashCode();
       result = 31 * result + id2.hashCode();
       return result;
   }

   public Long getId1() {
       return id1;
   }

   public Long getId2() {
       return id2;
   }
   
   public Long getId3() {
      return id3;
  }

   @Override
   public boolean equals(Object o) 
   {
       if ( this == o ) {
           return true;
       }
       if ( o == null || getClass() != o.getClass() ) 
       {
           return false;
       }

       MultiplePK multiplePK = (MultiplePK) o;

       return id1.equals( multiplePK.id1 )
               && id2.equals( multiplePK.id2 )
               && id3.equals( multiplePK.id3);
   }

   @Override
   public int hashCode() {
       return cachedHashCode;
   }
}
