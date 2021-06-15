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
package org.apache.hadoop.contrib.index.mapred;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;

/**
 * This class represents an indexing operation. The operation can be an insert,
 * a delete or an update. If the operation is an insert or an update, a (new)
 * document must be specified. If the operation is a delete or an update, a
 * delete term must be specified.
 */
public class DocumentAndOp implements Writable {

  /**
   * This class represents the type of an operation - an insert, a delete or
   * an update.
   */
  public static final class Op {
    public static final Op INSERT = new Op("INSERT");
    public static final Op DELETE = new Op("DELETE");
    public static final Op UPDATE = new Op("UPDATE");

    private String name;

    private Op(String name) {
      this.name = name;
    }

    public String toString() {
      return name;
    }
  }

  private Op op;
  private Document doc;
  private Term term;

  /**
   * Constructor for no operation.
   */
  public DocumentAndOp() {
  }

  /**
   * Constructor for an insert operation.
   * @param op
   * @param doc
   */
  public DocumentAndOp(Op op, Document doc) {
    assert (op == Op.INSERT);
    this.op = op;
    this.doc = doc;
    this.term = null;
  }

  /**
   * Constructor for a delete operation.
   * @param op
   * @param term
   */
  public DocumentAndOp(Op op, Term term) {
    assert (op == Op.DELETE);
    this.op = op;
    this.doc = null;
    this.term = term;
  }

  /**
   * Constructor for an insert, a delete or an update operation.
   * @param op
   * @param doc
   * @param term
   */
  public DocumentAndOp(Op op, Document doc, Term term) {
    if (op == Op.INSERT) {
      assert (doc != null);
      assert (term == null);
    } else if (op == Op.DELETE) {
      assert (doc == null);
      assert (term != null);
    } else {
      assert (op == Op.UPDATE);
      assert (doc != null);
      assert (term != null);
    }
    this.op = op;
    this.doc = doc;
    this.term = term;
  }

  /**
   * Set the instance to be an insert operation.
   * @param doc
   */
  public void setInsert(Document doc) {
    this.op = Op.INSERT;
    this.doc = doc;
    this.term = null;
  }

  /**
   * Set the instance to be a delete operation.
   * @param term
   */
  public void setDelete(Term term) {
    this.op = Op.DELETE;
    this.doc = null;
    this.term = term;
  }

  /**
   * Set the instance to be an update operation.
   * @param doc
   * @param term
   */
  public void setUpdate(Document doc, Term term) {
    this.op = Op.UPDATE;
    this.doc = doc;
    this.term = term;
  }

  /**
   * Get the type of operation.
   * @return the type of the operation.
   */
  public Op getOp() {
    return op;
  }

  /**
   * Get the document.
   * @return the document
   */
  public Document getDocument() {
    return doc;
  }

  /**
   * Get the term.
   * @return the term
   */
  public Term getTerm() {
    return term;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer.append(this.getClass().getName());
    buffer.append("[op=");
    buffer.append(op);
    buffer.append(", doc=");
    if (doc != null) {
      buffer.append(doc);
    } else {
      buffer.append("null");
    }
    buffer.append(", term=");
    if (term != null) {
      buffer.append(term);
    } else {
      buffer.append("null");
    }
    buffer.append("]");
    return buffer.toString();
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.io.Writable#write(java.io.DataOutput)
   */
  public void write(DataOutput out) throws IOException {
    throw new IOException(this.getClass().getName()
        + ".write should never be called");
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.io.Writable#readFields(java.io.DataInput)
   */
  public void readFields(DataInput in) throws IOException {
    throw new IOException(this.getClass().getName()
        + ".readFields should never be called");
  }
}
