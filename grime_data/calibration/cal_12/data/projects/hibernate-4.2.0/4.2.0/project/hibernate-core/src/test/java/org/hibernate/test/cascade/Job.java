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
// $Id: Job.java 6663 2005-05-03 20:55:31Z steveebersole $
package org.hibernate.test.cascade;


/**
 * Implementation of Job.
 *
 * @author Steve Ebersole
 */
public class Job {
	private Long id;
	private JobBatch batch;
	private String processingInstructions;
	private int status;

	/** GCLIB constructor */
	Job() {}

	protected Job(JobBatch batch) {
		this.batch = batch;
	}

	public Long getId() {
		return id;
	}

	/*package*/ void setId(Long id) {
		this.id = id;
	}

	public JobBatch getBatch() {
		return batch;
	}

	/*package*/ void setBatch(JobBatch batch) {
		this.batch = batch;
	}

	public String getProcessingInstructions() {
		return processingInstructions;
	}

	public void setProcessingInstructions(String processingInstructions) {
		this.processingInstructions = processingInstructions;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
