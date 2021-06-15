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
package org.apache.hadoop.hdfs.server.protocol;

import java.io.*;

import org.apache.hadoop.io.*;

public abstract class DatanodeCommand implements Writable {
  static class Register extends DatanodeCommand {
    private Register() {super(DatanodeProtocol.DNA_REGISTER);}
    public void readFields(DataInput in) {}
    public void write(DataOutput out) {}
  }

  static class Finalize extends DatanodeCommand {
    private Finalize() {super(DatanodeProtocol.DNA_FINALIZE);}
    public void readFields(DataInput in) {}
    public void write(DataOutput out) {}
  }

  static {                                      // register a ctor
    WritableFactories.setFactory(Register.class,
        new WritableFactory() {
          public Writable newInstance() {return new Register();}
        });
    WritableFactories.setFactory(Finalize.class,
        new WritableFactory() {
          public Writable newInstance() {return new Finalize();}
        });
  }

  public static final DatanodeCommand REGISTER = new Register();
  public static final DatanodeCommand FINALIZE = new Finalize();

  private int action;
  
  public DatanodeCommand() {
    this(DatanodeProtocol.DNA_UNKNOWN);
  }
  
  DatanodeCommand(int action) {
    this.action = action;
  }

  public int getAction() {
    return this.action;
  }
  
  ///////////////////////////////////////////
  // Writable
  ///////////////////////////////////////////
  public void write(DataOutput out) throws IOException {
    out.writeInt(this.action);
  }
  
  public void readFields(DataInput in) throws IOException {
    this.action = in.readInt();
  }
}