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
/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package org.apache.hadoop.thriftfs.api;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import com.facebook.thrift.*;

import com.facebook.thrift.protocol.*;
import com.facebook.thrift.transport.*;

public class Pathname implements TBase, java.io.Serializable {
  public String pathname;

  public final Isset __isset = new Isset();
  public static final class Isset implements java.io.Serializable {
    public boolean pathname = false;
  }

  public Pathname() {
  }

  public Pathname(
    String pathname)
  {
    this();
    this.pathname = pathname;
    this.__isset.pathname = true;
  }

  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Pathname)
      return this.equals((Pathname)that);
    return false;
  }

  public boolean equals(Pathname that) {
    if (that == null)
      return false;

    boolean this_present_pathname = true && (this.pathname != null);
    boolean that_present_pathname = true && (that.pathname != null);
    if (this_present_pathname || that_present_pathname) {
      if (!(this_present_pathname && that_present_pathname))
        return false;
      if (!this.pathname.equals(that.pathname))
        return false;
    }

    return true;
  }

  public int hashCode() {
    return 0;
  }

  public void read(TProtocol iprot) throws TException {
    TField field;
    iprot.readStructBegin();
    while (true)
    {
      field = iprot.readFieldBegin();
      if (field.type == TType.STOP) { 
        break;
      }
      switch (field.id)
      {
        case -1:
          if (field.type == TType.STRING) {
            this.pathname = iprot.readString();
            this.__isset.pathname = true;
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        default:
          TProtocolUtil.skip(iprot, field.type);
          break;
      }
      iprot.readFieldEnd();
    }
    iprot.readStructEnd();
  }

  public void write(TProtocol oprot) throws TException {
    TStruct struct = new TStruct("Pathname");
    oprot.writeStructBegin(struct);
    TField field = new TField();
    if (this.pathname != null) {
      field.name = "pathname";
      field.type = TType.STRING;
      field.id = -1;
      oprot.writeFieldBegin(field);
      oprot.writeString(this.pathname);
      oprot.writeFieldEnd();
    }
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("Pathname(");
    sb.append("pathname:");
    sb.append(this.pathname);
    sb.append(")");
    return sb.toString();
  }

}

