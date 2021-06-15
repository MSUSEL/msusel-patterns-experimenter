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
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.hadoop.contrib.index.lucene.RAMDirectoryUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.KeepOnlyLastCommitDeletionPolicy;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * An intermediate form for one or more parsed Lucene documents and/or
 * delete terms. It actually uses Lucene file format as the format for
 * the intermediate form by using RAM dir files.
 * 
 * Note: If process(*) is ever called, closeWriter() should be called.
 * Otherwise, no need to call closeWriter().
 */
public class IntermediateForm implements Writable {

  private IndexUpdateConfiguration iconf = null;
  private final Collection<Term> deleteList;
  private RAMDirectory dir;
  private IndexWriter writer;
  private int numDocs;

  /**
   * Constructor
   * @throws IOException
   */
  public IntermediateForm() throws IOException {
    deleteList = new ConcurrentLinkedQueue<Term>();
    dir = new RAMDirectory();
    writer = null;
    numDocs = 0;
  }

  /**
   * Configure using an index update configuration.
   * @param iconf  the index update configuration
   */
  public void configure(IndexUpdateConfiguration iconf) {
    this.iconf = iconf;
  }

  /**
   * Get the ram directory of the intermediate form.
   * @return the ram directory
   */
  public Directory getDirectory() {
    return dir;
  }

  /**
   * Get an iterator for the delete terms in the intermediate form.
   * @return an iterator for the delete terms
   */
  public Iterator<Term> deleteTermIterator() {
    return deleteList.iterator();
  }

  /**
   * This method is used by the index update mapper and process a document
   * operation into the current intermediate form.
   * @param doc  input document operation
   * @param analyzer  the analyzer
   * @throws IOException
   */
  public void process(DocumentAndOp doc, Analyzer analyzer) throws IOException {
    if (doc.getOp() == DocumentAndOp.Op.DELETE
        || doc.getOp() == DocumentAndOp.Op.UPDATE) {
      deleteList.add(doc.getTerm());

    }

    if (doc.getOp() == DocumentAndOp.Op.INSERT
        || doc.getOp() == DocumentAndOp.Op.UPDATE) {

      if (writer == null) {
        // analyzer is null because we specify an analyzer with addDocument
        writer = createWriter();
      }

      writer.addDocument(doc.getDocument(), analyzer);
      numDocs++;
    }

  }

  /**
   * This method is used by the index update combiner and process an
   * intermediate form into the current intermediate form. More specifically,
   * the input intermediate forms are a single-document ram index and/or a
   * single delete term.
   * @param form  the input intermediate form
   * @throws IOException
   */
  public void process(IntermediateForm form) throws IOException {
    if (form.deleteList.size() > 0) {
      deleteList.addAll(form.deleteList);
    }

    if (form.dir.sizeInBytes() > 0) {
      if (writer == null) {
        writer = createWriter();
      }

      writer.addIndexesNoOptimize(new Directory[] { form.dir });
      numDocs++;
    }

  }

  /**
   * Close the Lucene index writer associated with the intermediate form,
   * if created. Do not close the ram directory. In fact, there is no need
   * to close a ram directory.
   * @throws IOException
   */
  public void closeWriter() throws IOException {
    if (writer != null) {
      writer.close();
      writer = null;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer.append(this.getClass().getSimpleName());
    buffer.append("[numDocs=");
    buffer.append(numDocs);
    buffer.append(", numDeletes=");
    buffer.append(deleteList.size());
    if (deleteList.size() > 0) {
      buffer.append("(");
      Iterator<Term> iter = deleteTermIterator();
      while (iter.hasNext()) {
        buffer.append(iter.next());
        buffer.append(" ");
      }
      buffer.append(")");
    }
    buffer.append("]");
    return buffer.toString();
  }

  private IndexWriter createWriter() throws IOException {
    IndexWriter writer =
        new IndexWriter(dir, false, null,
            new KeepOnlyLastCommitDeletionPolicy());
    writer.setUseCompoundFile(false);

    if (iconf != null) {
      int maxFieldLength = iconf.getIndexMaxFieldLength();
      if (maxFieldLength > 0) {
        writer.setMaxFieldLength(maxFieldLength);
      }
    }

    return writer;
  }

  private void resetForm() throws IOException {
    deleteList.clear();
    if (dir.sizeInBytes() > 0) {
      // it's ok if we don't close a ram directory
      dir.close();
      // an alternative is to delete all the files and reuse the ram directory
      dir = new RAMDirectory();
    }
    assert (writer == null);
    numDocs = 0;
  }

  // ///////////////////////////////////
  // Writable
  // ///////////////////////////////////

  /* (non-Javadoc)
   * @see org.apache.hadoop.io.Writable#write(java.io.DataOutput)
   */
  public void write(DataOutput out) throws IOException {
    out.writeInt(deleteList.size());
    for (Term term : deleteList) {
      Text.writeString(out, term.field());
      Text.writeString(out, term.text());
    }

    String[] files = dir.list();
    RAMDirectoryUtil.writeRAMFiles(out, dir, files);
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.io.Writable#readFields(java.io.DataInput)
   */
  public void readFields(DataInput in) throws IOException {
    resetForm();

    int numDeleteTerms = in.readInt();
    for (int i = 0; i < numDeleteTerms; i++) {
      String field = Text.readString(in);
      String text = Text.readString(in);
      deleteList.add(new Term(field, text));
    }

    RAMDirectoryUtil.readRAMFiles(in, dir);
  }

}
