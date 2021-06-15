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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.GenericGenerator;

/**
 * An Entity containing a composite key with two generated values.
 *
 * @author <a href="mailto:stale.pedersen@jboss.org">Stale W. Pedersen</a>
 */
@Entity
@IdClass(MultiplePK.class)
@SuppressWarnings("serial")
public class Multiple implements Serializable
{
   @Id
   @GenericGenerator(name = "increment", strategy = "increment")
   @GeneratedValue(generator = "increment")
   private Long id1;
   
   @Id
   @GeneratedValue(generator = "MULTIPLE_SEQ", strategy = GenerationType.SEQUENCE)
   @SequenceGenerator( name = "MULTIPLE_SEQ", sequenceName = "MULTIPLE_SEQ")
   private Long id2;
   
   @Id
   private Long id3;
   private int quantity;
   
   public Multiple()
   {
      
   }
   
   public Multiple(Long id3, int quantity)
   {
      this.id3 = id3;
      this.quantity = quantity;
   }

   public Long getId1()
   {
      return id1;
   }

   public Long getId2()
   {
      return id2;
   }

   public Long getId3()
   {
      return id3;
   }

   public int getQuantity()
   {
      return quantity;
   }

   public void setQuantity(int quantity)
   {
      this.quantity = quantity;
   }
   
   
}
