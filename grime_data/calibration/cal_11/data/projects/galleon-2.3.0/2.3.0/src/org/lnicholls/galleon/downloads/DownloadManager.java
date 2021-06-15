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
package org.lnicholls.galleon.downloads;

/*
 * Created on 21 de abril de 2003, 05:43 PM
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.lnicholls.galleon.server.DownloadConfiguration;
import org.lnicholls.galleon.server.Server;
import org.lnicholls.galleon.util.Tools;

public class DownloadManager implements Serializable {

	private static Logger log = Logger.getLogger(DownloadManager.class.getName());

	private class SL implements StatusListener, Serializable {
		public void statusChanged(StatusEvent se) {
			Downloader dt = (Downloader) se.getSource();

			if (da.length > 0)
			{
				if (se.getNewStatus() == se.STOPPED || se.getNewStatus() == se.COMPLETED) {
					Downloader[] dab = new Downloader[da.length - 1];
					int counter = 0;
					for (int i = 0; i < da.length; i++) {
						if (da[i] != dt)
							dab[counter++] = da[i];
					}
					da = dab;
					--running;
				}
				else
				if (se.getNewStatus() == se.ERROR) {
					Downloader[] dab = new Downloader[da.length - 1];
					int counter = 0;
					for (int i = 0; i < da.length; i++) {
						if (da[i] != dt)
							dab[counter++] = da[i];
					}
					da = dab;
					--running;
				}
	
				if (se.getNewStatus() == se.COMPLETED) {
					// pauseMenuItem.setEnabled( false );
				}
			}
		}
	}

	/**
	 * Thread status listener. It updates thread progress and status in screen.
	 */
	private class TSL implements StatusListener, Serializable {
		public void statusChanged(StatusEvent se) {
			DownloadThread dt = (DownloadThread) se.getSource();

			if (se.getNewStatus() == se.ERROR) {
				log.info("Restarting... " + dt.getName());
				try {
					dt.getParentDownload().resumeThread(dt.getID());
				} catch (Exception ex) {
					Tools.logException(DownloadManager.class, ex);
				}
			}
		}
	}

	private class DWL implements DataWritingListener, Serializable {
		public void dataWritten(DataWritingEvent dwe) {

			Downloader d = (Downloader) dwe.getSource();
			long b = d.getBytesCompleted();
			long s = d.getSize();

			// log.debug((int) (b*100/s));
		}
	}

	private class TDWL implements DataWritingListener, Serializable {
		public void dataWritten(DataWritingEvent dwe) {

			DownloadThread dt = (DownloadThread) dwe.getSource();

			long tb = dt.getBytesCompleted();
			long ts = dt.getSize();
		}
	}

	public DownloadManager() {
	}

	private void startMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		int idx = 0;

		try {
			if (da[idx].getStatus() == ThreadStatus.STOPPED.getID()) {
				da[idx].start();

				++running;

				if (running == 1) {
					// t.scheduleAtFixedRate( new DownloadMonitor(), 0, 1000);
				}
			}

			if (da[idx].getStatus() == ThreadStatus.PAUSED.getID()) {
				da[idx].resumeDownload();
			}
		} catch (Exception ex) {
			Tools.logException(DownloadManager.class, ex);
		}
	}

	public void start(int idx) {

		try {
			if (da[idx].getStatus() == ThreadStatus.STOPPED.getID()) {
				da[idx].start();

				++running;

				if (running == 1) {
					// t.scheduleAtFixedRate( new DownloadMonitor(), 0, 1000);
				}
			}

			if (da[idx].getStatus() == ThreadStatus.PAUSED.getID()) {
				da[idx].resumeDownload();
			}
		} catch (Exception ex) {
			Tools.logException(DownloadManager.class, ex);
		}
	}

	public void addDownload(Download d, StatusListener statusListener) {

		Downloader[] dab = new Downloader[da.length + 1];

		System.arraycopy(da, 0, dab, 0, da.length);

		Downloader downloader = new Downloader(d);
		downloader.addStatusListener(statusListener);
		downloader.addStatusListener(dsl);
		downloader.addDataWritingListener(dwl);
		downloader.setID(da.length);
		dab[dab.length - 1] = downloader;

		DownloadConfiguration downloadConfiguration = Server.getServer().getServerConfiguration()
				.getDownloadConfiguration();
		int threads = 1;
		if (downloadConfiguration.getBandwidth() == 10)
			threads = 5;
		else if (downloadConfiguration.getBandwidth() == 5)
			threads = 3;
		int priority = Thread.MIN_PRIORITY;
		if (downloadConfiguration.getBandwidth() == 10)
			priority = Thread.MAX_PRIORITY;
		else if (downloadConfiguration.getBandwidth() == 5)
			priority = Thread.NORM_PRIORITY;
		
		da = dab;
		da[dab.length - 1].setThreadNumber(threads);
		da[dab.length - 1].setPriority(priority);
		da[dab.length - 1].start();

		loadDownloads();
	}

	private void loadDownloads() {

		for (int y = 0; y < da.length; ++y) {
			da[y].setDownloadPriority(y);
		}
	}

	private void loadDownloadThreads(DownloadThread[] dt) {

		for (int y = 0; y < dt.length; ++y) {
			dt[y].addStatusListener(tsl);
			dt[y].addDataWritingListener(tdwl);
		}
	}

	private Downloader findDownload(int id) {
		for (int y = 0; y < da.length; ++y)
			if (da[y].getID() == id)
				return da[y];

		return null;
	}

	public List getDownloads() {
		Downloader[] dab = new Downloader[da.length];
		System.arraycopy(da, 0, dab, 0, da.length);
		List downloads = new ArrayList();
		for (int i = 0; i < dab.length; i++)
			downloads.add(dab[i].getDownload());
		return downloads;
	}

	public void pauseDownload(Download download) {
		for (int i = 0; i < da.length; i++) {
			if (da[i].getDownload().getURL().equals(download.getURL()))
				da[i].pauseDownload();
		}
	}

	public void resumeDownload(Download download) {
		for (int i = 0; i < da.length; i++) {
			if (da[i].getDownload().getURL().equals(download.getURL()))
				try {
					da[i].resumeDownload();
				} catch (Exception ex) {
					Tools.logException(DownloadManager.class, ex);
				}
		}
	}
	
	public void stopDownload(Download download) {
		for (int i = 0; i < da.length; i++) {
			if (da[i].getDownload().getURL().equals(download.getURL()))
				da[i].stopDownload();
		}
	}	

	SL dsl = new SL();

	TSL tsl = new TSL();

	DWL dwl = new DWL();

	TDWL tdwl = new TDWL();

	Downloader[] da = new Downloader[0];

	int running = 0;

	int lsi = -1, tsi = -1;
}