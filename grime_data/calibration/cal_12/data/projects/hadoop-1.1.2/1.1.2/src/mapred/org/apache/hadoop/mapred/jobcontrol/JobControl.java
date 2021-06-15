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
package org.apache.hadoop.mapred.jobcontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

/** This class encapsulates a set of MapReduce jobs and its dependency. It tracks 
 *  the states of the jobs by placing them into different tables according to their 
 *  states. 
 *  
 *  This class provides APIs for the client app to add a job to the group and to get 
 *  the jobs in the group in different states. When a 
 *  job is added, an ID unique to the group is assigned to the job. 
 *  
 *  This class has a thread that submits jobs when they become ready, monitors the
 *  states of the running jobs, and updates the states of jobs based on the state changes 
 *  of their depending jobs states. The class provides APIs for suspending/resuming
 *  the thread,and for stopping the thread.
 *  
 */
public class JobControl implements Runnable{

  // The thread can be in one of the following state
  private static final int RUNNING = 0;
  private static final int SUSPENDED = 1;
  private static final int STOPPED = 2;
  private static final int STOPPING = 3;
  private static final int READY = 4;
	
  private int runnerState;			// the thread state
	
  private Map<String, Job> waitingJobs;
  private Map<String, Job> readyJobs;
  private Map<String, Job> runningJobs;
  private Map<String, Job> successfulJobs;
  private Map<String, Job> failedJobs;
	
  private long nextJobID;
  private String groupName;
	
  /** 
   * Construct a job control for a group of jobs.
   * @param groupName a name identifying this group
   */
  public JobControl(String groupName) {
    this.waitingJobs = new Hashtable<String, Job>();
    this.readyJobs = new Hashtable<String, Job>();
    this.runningJobs = new Hashtable<String, Job>();
    this.successfulJobs = new Hashtable<String, Job>();
    this.failedJobs = new Hashtable<String, Job>();
    this.nextJobID = -1;
    this.groupName = groupName;
    this.runnerState = JobControl.READY;
  }
	
  private static ArrayList<Job> toArrayList(Map<String, Job> jobs) {
    ArrayList<Job> retv = new ArrayList<Job>();
    synchronized (jobs) {
      for (Job job : jobs.values()) {
        retv.add(job);
      }
    }
    return retv;
  }
	
  /**
   * @return the jobs in the waiting state
   */
  public ArrayList<Job> getWaitingJobs() {
    return JobControl.toArrayList(this.waitingJobs);
  }
	
  /**
   * @return the jobs in the running state
   */
  public ArrayList<Job> getRunningJobs() {
    return JobControl.toArrayList(this.runningJobs);
  }
	
  /**
   * @return the jobs in the ready state
   */
  public ArrayList<Job> getReadyJobs() {
    return JobControl.toArrayList(this.readyJobs);
  }
	
  /**
   * @return the jobs in the success state
   */
  public ArrayList<Job> getSuccessfulJobs() {
    return JobControl.toArrayList(this.successfulJobs);
  }
	
  public ArrayList<Job> getFailedJobs() {
    return JobControl.toArrayList(this.failedJobs);
  }
	
  private String getNextJobID() {
    nextJobID += 1;
    return this.groupName + this.nextJobID;
  }
	
  private static void addToQueue(Job aJob, Map<String, Job> queue) {
    synchronized(queue) {
      queue.put(aJob.getJobID(), aJob);
    }		
  }
	
  private void addToQueue(Job aJob) {
    Map<String, Job> queue = getQueue(aJob.getState());
    addToQueue(aJob, queue);	
  }
	
  private Map<String, Job> getQueue(int state) {
    Map<String, Job> retv = null;
    if (state == Job.WAITING) {
      retv = this.waitingJobs;
    } else if (state == Job.READY) {
      retv = this.readyJobs;
    } else if (state == Job.RUNNING) {
      retv = this.runningJobs;
    } else if (state == Job.SUCCESS) {
      retv = this.successfulJobs;
    } else if (state == Job.FAILED || state == Job.DEPENDENT_FAILED) {
      retv = this.failedJobs;
    } 
    return retv;
  }

  /**
   * Add a new job.
   * @param aJob the new job
   */
  synchronized public String addJob(Job aJob) {
    String id = this.getNextJobID();
    aJob.setJobID(id);
    aJob.setState(Job.WAITING);
    this.addToQueue(aJob);
    return id;	
  }
	
  /**
   * Add a collection of jobs
   * 
   * @param jobs
   */
  public void addJobs(Collection<Job> jobs) {
    for (Job job : jobs) {
      addJob(job);
    }
  }
	
  /**
   * @return the thread state
   */
  public int getState() {
    return this.runnerState;
  }
	
  /**
   * set the thread state to STOPPING so that the 
   * thread will stop when it wakes up.
   */
  public void stop() {
    this.runnerState = JobControl.STOPPING;
  }
	
  /**
   * suspend the running thread
   */
  public void suspend () {
    if (this.runnerState == JobControl.RUNNING) {
      this.runnerState = JobControl.SUSPENDED;
    }
  }
	
  /**
   * resume the suspended thread
   */
  public void resume () {
    if (this.runnerState == JobControl.SUSPENDED) {
      this.runnerState = JobControl.RUNNING;
    }
  }
	
  synchronized private void checkRunningJobs() {
		
    Map<String, Job> oldJobs = null;
    oldJobs = this.runningJobs;
    this.runningJobs = new Hashtable<String, Job>();
		
    for (Job nextJob : oldJobs.values()) {
      int state = nextJob.checkState();
      /*
        if (state != Job.RUNNING) {
        System.out.println("The state of the running job " +
        nextJob.getJobName() + " has changed to: " + nextJob.getState());
        }
      */
      this.addToQueue(nextJob);
    }
  }
	
  synchronized private void checkWaitingJobs() {
    Map<String, Job> oldJobs = null;
    oldJobs = this.waitingJobs;
    this.waitingJobs = new Hashtable<String, Job>();
		
    for (Job nextJob : oldJobs.values()) {
      int state = nextJob.checkState();
      /*
        if (state != Job.WAITING) {
        System.out.println("The state of the waiting job " +
        nextJob.getJobName() + " has changed to: " + nextJob.getState());
        }
      */
      this.addToQueue(nextJob);
    }
  }
	
  synchronized private void startReadyJobs() {
    Map<String, Job> oldJobs = null;
    oldJobs = this.readyJobs;
    this.readyJobs = new Hashtable<String, Job>();
		
    for (Job nextJob : oldJobs.values()) {
      //System.out.println("Job to submit to Hadoop: " + nextJob.getJobName());
      nextJob.submit();
      //System.out.println("Hadoop ID: " + nextJob.getMapredJobID());
      this.addToQueue(nextJob);
    }	
  }
	
  synchronized public boolean allFinished() {
    return this.waitingJobs.size() == 0 &&
      this.readyJobs.size() == 0 &&
      this.runningJobs.size() == 0;
  }
	
  /**
   *  The main loop for the thread.
   *  The loop does the following:
   *  	Check the states of the running jobs
   *  	Update the states of waiting jobs
   *  	Submit the jobs in ready state
   */
  public void run() {
    this.runnerState = JobControl.RUNNING;
    while (true) {
      while (this.runnerState == JobControl.SUSPENDED) {
        try {
          Thread.sleep(5000);
        }
        catch (Exception e) {
					
        }
      }
      checkRunningJobs();	
      checkWaitingJobs();		
      startReadyJobs();		
      if (this.runnerState != JobControl.RUNNING && 
          this.runnerState != JobControl.SUSPENDED) {
        break;
      }
      try {
        Thread.sleep(5000);
      }
      catch (Exception e) {
				
      }
      if (this.runnerState != JobControl.RUNNING && 
          this.runnerState != JobControl.SUSPENDED) {
        break;
      }
    }
    this.runnerState = JobControl.STOPPED;
  }

}
