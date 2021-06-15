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
package org.archive.crawler.selftest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import org.archive.crawler.admin.CrawlJob.MBeanCrawlController;
import org.archive.crawler.datamodel.Checkpoint;
import org.archive.crawler.datamodel.CrawlOrder;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.event.CrawlStatusListener;
import org.archive.crawler.event.CrawlURIDispositionListener;
import org.archive.crawler.framework.Checkpointer;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.framework.exceptions.InitializationException;
import org.archive.crawler.settings.XMLSettingsHandler;
import org.archive.crawler.util.CheckpointUtils;


/**
 * Assumes checkpoint was run during the SelfTest.
 * @author stack
 * @version $Date: 2007-02-21 18:48:17 +0000 (Wed, 21 Feb 2007) $ $Version$
 */
public class CheckpointSelfTest extends SelfTestCase
implements CrawlStatusListener, CrawlURIDispositionListener {
	private final Logger LOG = Logger.getLogger(this.getClass().getName());
	private boolean crawlEnded = false;

	public CheckpointSelfTest() {
		// TODO Auto-generated constructor stub
	}

	public CheckpointSelfTest(String testName) {
		super(testName);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Recover from the checkpoint made during selftest.
	 * @throws InitializationException 
	 * @throws IOException 
	 * @throws InvalidAttributeValueException 
	 * @throws ReflectionException 
	 * @throws MBeanException 
	 * @throws AttributeNotFoundException 
	 * @throws ClassNotFoundException 
	 * @throws InterruptedException 
	 */
	public void stestCheckpointRecover()
	throws InitializationException, IOException,
			InvalidAttributeValueException, AttributeNotFoundException,
			MBeanException, ReflectionException, ClassNotFoundException,
			InterruptedException {
        assertInitialized();
		// Check checkpoint dir is in place.
		File f = getFile(getCrawlJobDir(), "checkpoints");
		// Use the first checkpoint in the dir.
		File cpdir = getFile(f, Checkpointer.formatCheckpointName("", 1));
		// Check valid checkpoint file is in place.
	    getFile(cpdir, Checkpoint.VALIDITY_STAMP_FILENAME);
	    // Get order file from checkpoint dir.
	    File order = getFile(cpdir, "order.xml");
        XMLSettingsHandler handler =
            new XMLSettingsHandler(order);
        handler.initialize();
        // Set recover-path to be this checkpoint dir.
        handler.getOrder().setAttribute(
        	new Attribute(CrawlOrder.ATTR_RECOVER_PATH, cpdir.toString()));
        Checkpoint cp =
        	CrawlController.getCheckpointRecover(handler.getOrder());
        if (cp == null) {
        	throw new NullPointerException("Failed read of checkpoint object");
        }
        CrawlController c = (MBeanCrawlController)CheckpointUtils.
        	readObjectFromFile(MBeanCrawlController.class, cpdir);
        c.initialize(handler);
        c.addCrawlStatusListener(this);
        c.addCrawlURIDispositionListener(this);
        c.requestCrawlStart();
        LOG.info("Recover from selftest crawl started using " +
            order.toString() + ".");
        // Wait here a while till its up and running?
        while(!this.crawlEnded) {
        	LOG.info("Waiting on recovered crawl to finish");
        	Thread.sleep(1000);
        }
	}
	
	private File getFile(final File parent, final String name)
	throws IOException {
		File f = new File(parent, name);
		if (!f.exists()) {
			throw new FileNotFoundException(f.getAbsolutePath());
		}
		if (!f.canRead()) {
			throw new IOException("Can't read " + f.getAbsolutePath());
		}
		return f;
	}

	public void crawlCheckpoint(File checkpointDir) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void crawlEnded(String sExitMessage) {
		this.crawlEnded = true;
	}

	public void crawlEnding(String sExitMessage) {
		// TODO Auto-generated method stub
		
	}

	public void crawlPaused(String statusMessage) {
		// TODO Auto-generated method stub
		
	}

	public void crawlPausing(String statusMessage) {
		// TODO Auto-generated method stub
		
	}

	public void crawlResuming(String statusMessage) {
		// TODO Auto-generated method stub
		
	}

	public void crawlStarted(String message) {
		// TODO Auto-generated method stub
		
	}

	public void crawledURIDisregard(CrawlURI curi) {
		// TODO Auto-generated method stub
		
	}

	public void crawledURIFailure(CrawlURI curi) {
		// TODO Auto-generated method stub
		
	}

	public void crawledURINeedRetry(CrawlURI curi) {
		// TODO Auto-generated method stub
		
	}

	public void crawledURISuccessful(CrawlURI curi) {
		LOG.info(curi.toString());
	}
}