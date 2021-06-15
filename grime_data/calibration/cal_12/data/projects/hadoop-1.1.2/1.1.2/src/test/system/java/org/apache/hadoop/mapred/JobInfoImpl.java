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
package org.apache.hadoop.mapred;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.test.system.JobInfo;

/**
 * Concrete implementation of the JobInfo interface which is exposed to the
 * clients.
 * Look at {@link JobInfo} for further details.
 */
class JobInfoImpl implements JobInfo {

  private List<String> blackListedTracker;
  private String historyUrl;
  private JobID id;
  private boolean setupLaunched;
  private boolean setupFinished;
  private boolean cleanupLaunched;
  private JobStatus status;
  private int runningMaps;
  private int runningReduces;
  private int waitingMaps;
  private int waitingReduces;
  private int finishedMaps;
  private int finishedReduces;
  private int numMaps;
  private int numReduces;
  private long finishTime;
  private long launchTime;
  private int numOfSlotsPerMap;
  private int numOfSlotsPerReduce;

  public JobInfoImpl() {
    id = new JobID();
    status = new JobStatus();
    blackListedTracker = new LinkedList<String>();
    historyUrl = "";
  }
  
  public JobInfoImpl(
      JobID id, boolean setupLaunched, boolean setupFinished,
      boolean cleanupLaunched, int runningMaps, int runningReduces,
      int waitingMaps, int waitingReduces, int finishedMaps,
      int finishedReduces, JobStatus status, String historyUrl,
      List<String> blackListedTracker, boolean isComplete, int numMaps,
      int numReduces) {
    super();
    this.blackListedTracker = blackListedTracker;
    this.historyUrl = historyUrl;
    this.id = id;
    this.setupLaunched = setupLaunched;
    this.setupFinished = setupFinished;
    this.cleanupLaunched = cleanupLaunched;
    this.status = status;
    this.runningMaps = runningMaps;
    this.runningReduces = runningReduces;
    this.waitingMaps = waitingMaps;
    this.waitingReduces = waitingReduces;
    this.finishedMaps = finishedMaps;
    this.finishedReduces = finishedReduces;
    this.numMaps = numMaps;
    this.numReduces = numReduces;
  }

  @Override
  public List<String> getBlackListedTrackers() {
    return blackListedTracker;
  }

  @Override
  public String getHistoryUrl() {
    return historyUrl;
  }

  @Override
  public JobID getID() {
    return id;
  }

  @Override
  public JobStatus getStatus() {
    return status;
  }

  @Override
  public boolean isCleanupLaunched() {
    return cleanupLaunched;
  }

  @Override
  public boolean isSetupLaunched() {
    return setupLaunched;
  }

  @Override
  public boolean isSetupFinished() {
    return setupFinished;
  }

  @Override
  public int runningMaps() {
    return runningMaps;
  }

  @Override
  public int runningReduces() {
    return runningReduces;
  }

  @Override
  public int waitingMaps() {
    return waitingMaps;
  }

  @Override
  public int waitingReduces() {
    return waitingReduces;
  }
 
  @Override
  public int finishedMaps() {
    return finishedMaps;
  }

  @Override
  public int finishedReduces() {
    return finishedReduces;
  }
  
  @Override
  public int numMaps() {
    return numMaps;
  }
  
  @Override
  public int numReduces() {
    return numReduces;
  }

  public void setFinishTime(long finishTime) {
    this.finishTime = finishTime;
  }
  
  public void setLaunchTime(long launchTime) {
    this.launchTime = launchTime;
  }

  @Override
  public long getFinishTime() {
    return finishTime;
  }

  @Override
  public long getLaunchTime() {
    return launchTime;
  }

  public void setNumSlotsPerMap(int numOfSlotsPerMap) {
    this.numOfSlotsPerMap = numOfSlotsPerMap;
  } 

  public void setNumSlotsPerReduce(int numOfSlotsPerReduce) {
    this.numOfSlotsPerReduce = numOfSlotsPerReduce;
  }

  @Override
  public int getNumSlotsPerMap() {
    return numOfSlotsPerMap;
  }

  @Override
  public int getNumSlotsPerReduce() {
    return numOfSlotsPerReduce;
  }
  
  @Override
  public void readFields(DataInput in) throws IOException {
    id.readFields(in);
    setupLaunched = in.readBoolean();
    setupFinished = in.readBoolean();
    cleanupLaunched = in.readBoolean();
    status.readFields(in);
    runningMaps = in.readInt();
    runningReduces = in.readInt();
    waitingMaps = in.readInt();
    waitingReduces = in.readInt();
    historyUrl = in.readUTF();
    int size = in.readInt();
    for (int i = 0; i < size; i++) {
      blackListedTracker.add(in.readUTF());
    }
    finishedMaps = in.readInt();
    finishedReduces = in.readInt();
    numMaps = in.readInt();
    numReduces = in.readInt();
    finishTime = in.readLong();
    launchTime = in.readLong();
    numOfSlotsPerMap = in.readInt();
    numOfSlotsPerReduce = in.readInt();
  }

  @Override
  public void write(DataOutput out) throws IOException {
    id.write(out);
    out.writeBoolean(setupLaunched);
    out.writeBoolean(setupFinished);
    out.writeBoolean(cleanupLaunched);
    status.write(out);
    out.writeInt(runningMaps);
    out.writeInt(runningReduces);
    out.writeInt(waitingMaps);
    out.writeInt(waitingReduces);
    out.writeUTF(historyUrl);
    out.writeInt(blackListedTracker.size());
    for (String str : blackListedTracker) {
      out.writeUTF(str);
    }
    out.writeInt(finishedMaps);
    out.writeInt(finishedReduces);
    out.writeInt(numMaps);
    out.writeInt(numReduces);
    out.writeLong(finishTime);
    out.writeLong(launchTime);
    out.writeInt(numOfSlotsPerMap);
    out.writeInt(numOfSlotsPerReduce);
  }


}
