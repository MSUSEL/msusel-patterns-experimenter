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
package org.apache.hadoop.mapreduce.lib.input;

import java.io.IOException;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * An {@link Mapper} that delegates behavior of paths to multiple other
 * mappers.
 * 
 * @see MultipleInputs#addInputPath(Job, Path, Class, Class)
 */
@InterfaceAudience.Private
@InterfaceStability.Unstable
public class DelegatingMapper<K1, V1, K2, V2> extends Mapper<K1, V1, K2, V2> {

  private Mapper<K1, V1, K2, V2> mapper;

  @SuppressWarnings("unchecked")
  protected void setup(Context context)
      throws IOException, InterruptedException {
    // Find the Mapper from the TaggedInputSplit.
    TaggedInputSplit inputSplit = (TaggedInputSplit) context.getInputSplit();
    mapper = (Mapper<K1, V1, K2, V2>) ReflectionUtils.newInstance(inputSplit
       .getMapperClass(), context.getConfiguration());
    
  }

  @SuppressWarnings("unchecked")
  public void run(Context context) 
      throws IOException, InterruptedException {
    setup(context);
    mapper.run(context);
    cleanup(context);
  }
}
