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
package org.geotools.gce.geotiff;

import java.util.LinkedList;
import java.util.List;

import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * 
 *
 * @source $URL$
 */
public class TestProgress implements ProgressListener {
	public float progress = Float.NaN;
	public InternationalString task;
	public boolean isCanceled;
	public List<String> warnings = new LinkedList<String>();
	public Throwable exception;
	public String description;
	
	public void complete() {
		progress = 1.0f;
	}

	public void dispose() {
		progress = Float.NaN;
	}

	public void exceptionOccurred(Throwable t) {
		exception = t;
	}

	public String getDescription() {
		return description;
	}

	public float getProgress() {
		return progress;
	}

	public InternationalString getTask() {
		return task;
	}

	public boolean isCanceled() {
		return isCanceled;
	}

	public void progress(float percent) {
		progress = percent;
	}

	public void setCanceled(boolean cancel) {
		isCanceled = cancel;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTask(InternationalString task) {
		this.task = task;
	}

	public void started() {
		progress = 0.0f;
	}

	public void warningOccurred(String file, String location, String warning) {
		StringBuffer warn = new StringBuffer();
		
		if( file == null ) {
			warn.append( file );
			warn.append( " " );
		}
		if( location == null ) {
			warn.append("#");
			warn.append( location );
			warn.append(" ");
		}
		warn.append( warning );
		warnings.add( warn.toString() );
	}

}
