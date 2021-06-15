<%--

    The MIT License (MIT)

    MSUSEL Arc Framework
    Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
    Software Engineering Laboratory and Idaho State University, Informatics and
    Computer Science, Empirical Software Engineering Laboratory

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

--%>
<%@ page
  contentType="text/html; charset=UTF-8"
  import="org.apache.hadoop.mapred.*"
  import="org.apache.hadoop.fs.*"
  import="org.apache.hadoop.mapred.JobHistory.*"
%>
<%!	private static final long serialVersionUID = 1L;
%>
<%
    PathFilter jobLogFileFilter = new PathFilter() {
      public boolean accept(Path path) {
        return !(path.getName().endsWith(".xml"));
      }
    };

    FileSystem fs = (FileSystem) application.getAttribute("fileSys");
    String jobId = request.getParameter("jobid");
    JobHistory.JobInfo job = (JobHistory.JobInfo)
                               request.getSession().getAttribute("job");
    // if session attribute of JobInfo exists and is of different job's,
    // then remove the attribute
    // if the job has not yet finished, remove the attribute sothat it 
    // gets refreshed.
    boolean isJobComplete = false;
    if (null != job) {
      String jobStatus = job.get(Keys.JOB_STATUS);
      isJobComplete = Values.SUCCESS.name() == jobStatus
                      || Values.FAILED.name() == jobStatus
                      || Values.KILLED.name() == jobStatus;
    }
    if (null != job && 
       (!jobId.equals(job.get(Keys.JOBID)) 
         || !isJobComplete)) {
      // remove jobInfo from session, keep only one job in session at a time
      request.getSession().removeAttribute("job"); 
      job = null ; 
    }
	
    if (null == job) {
      String jobLogFile = request.getParameter("logFile");
      job = new JobHistory.JobInfo(jobId); 
      DefaultJobHistoryParser.parseJobTasks(jobLogFile, job, fs) ; 
      request.getSession().setAttribute("job", job);
      request.getSession().setAttribute("fs", fs);
    }
%>
