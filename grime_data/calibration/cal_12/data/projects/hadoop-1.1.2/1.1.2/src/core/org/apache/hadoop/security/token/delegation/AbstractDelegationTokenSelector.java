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
package org.apache.hadoop.security.token.delegation;

import java.util.Collection;

//import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.security.token.TokenIdentifier;
import org.apache.hadoop.security.token.TokenSelector;
//import static org.apache.hadoop.classification.InterfaceAudience.LimitedPrivate.Project.HDFS;
//import static org.apache.hadoop.classification.InterfaceAudience.LimitedPrivate.Project.MAPREDUCE;

/**
 * Look through tokens to find the first delegation token that matches the
 * service and return it.
 */
//@InterfaceAudience.LimitedPrivate({HDFS, MAPREDUCE})
public 
class AbstractDelegationTokenSelector<TokenIdent 
extends AbstractDelegationTokenIdentifier> 
    implements TokenSelector<TokenIdent> {
  private Text kindName;
  
  protected AbstractDelegationTokenSelector(Text kindName) {
    this.kindName = kindName;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Token<TokenIdent> selectToken(Text service,
      Collection<Token<? extends TokenIdentifier>> tokens) {
    if (service == null) {
      return null;
    }
    for (Token<? extends TokenIdentifier> token : tokens) {
      if (kindName.equals(token.getKind())
          && service.equals(token.getService())) {
        return (Token<TokenIdent>) token;
      }
    }
    return null;
  }
}
