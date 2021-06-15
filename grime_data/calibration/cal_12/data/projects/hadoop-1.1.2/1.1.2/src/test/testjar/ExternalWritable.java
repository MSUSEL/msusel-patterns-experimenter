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
package testjar;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * This is an example simple writable class.  This is used as a class external 
 * to the Hadoop IO classes for testing of user Writable classes.
 * 
 */
public class ExternalWritable
  implements WritableComparable {

  private String message = null;
  
  public ExternalWritable() {
    
  }
  
  public ExternalWritable(String message) {
    this.message = message;
  }
  
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void readFields(DataInput in)
    throws IOException {
    
    message = null;
    boolean hasMessage = in.readBoolean();
    if (hasMessage) {
      message = in.readUTF();   
    }
  }

  public void write(DataOutput out)
    throws IOException {
    
    boolean hasMessage = (message != null && message.length() > 0);
    out.writeBoolean(hasMessage);
    if (hasMessage) {
      out.writeUTF(message);
    }
  }
  
  public int compareTo(Object o) {
    
    if (!(o instanceof ExternalWritable)) {
      throw new IllegalArgumentException("Input not an ExternalWritable");
    }
    
    ExternalWritable that = (ExternalWritable)o;
    return this.message.compareTo(that.message);
  }

  public String toString() {
    return this.message;
  }
}
