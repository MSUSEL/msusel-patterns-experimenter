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
package org.apache.hadoop.hdfs.server.common;

import org.apache.hadoop.hdfs.server.namenode.MetaRecoveryContext;


/************************************
 * Some handy internal HDFS constants
 *
 ************************************/

public interface HdfsConstants {
  /**
   * Type of the node
   */
  static public enum NodeType {
    NAME_NODE,
    DATA_NODE;
  }

  // Startup options
  static public enum StartupOption{
    FORMAT  ("-format"),
    REGULAR ("-regular"),
    UPGRADE ("-upgrade"),
    RECOVER ("-recover"),
    FORCE ("-force"),
    ROLLBACK("-rollback"),
    FINALIZE("-finalize"),
    IMPORT  ("-importCheckpoint"),
    NONINTERACTIVE  ("-nonInteractive");
    
    // Used only with recovery option
    private int force = MetaRecoveryContext.FORCE_NONE;
    
    // used only with format option
    private boolean isConfirmationNeeded = true;
    private boolean isInteractive = true;
    
    private String name = null;
    private StartupOption(String arg) {this.name = arg;}
    public String getName() {return name;}

    public MetaRecoveryContext createRecoveryContext() {
      if (!name.equals(RECOVER.name))
        return null;
      return new MetaRecoveryContext(force);
    }

    public void setForce(int force) {
      this.force = force;
    }
    
    public int getForce() {
      return this.force;
    }
    
    public void setConfirmationNeeded(boolean confirmationNeeded) {
      this.isConfirmationNeeded = confirmationNeeded;
    }

    public boolean getConfirmationNeeded() {
      return isConfirmationNeeded;
    }

    public void setInteractive(boolean interactive) {
      this.isInteractive = interactive;
    }

    public boolean getInteractive() {
      return isInteractive;
    }
  }

  // Timeouts for communicating with DataNode for streaming writes/reads
  public static int READ_TIMEOUT = 60 * 1000;
  public static int READ_TIMEOUT_EXTENSION = 3 * 1000;
  public static int WRITE_TIMEOUT = 8 * 60 * 1000;
  public static int WRITE_TIMEOUT_EXTENSION = 5 * 1000; //for write pipeline


  // The lease holder for recovery initiated by the NameNode
  public static final String NN_RECOVERY_LEASEHOLDER = "NN_Recovery";

}

