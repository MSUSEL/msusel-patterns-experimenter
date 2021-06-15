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
package org.lnicholls.galleon.goback;

/*
 * Copyright (C) 2005 Leon Nicholls
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * 
 * See the file "COPYING" for more details.
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.lnicholls.galleon.goback.TCM;
import org.lnicholls.galleon.util.Tools;
import org.lnicholls.galleon.server.*;

/**
 * Extracted from: TiVo Connect Automatic Machine Discovery Protocol Specification. Revision: 1.5.1, Last updated:
 * 3/5/2003 Copyright � 2003, TiVo Inc. All rights reserved
 *
 * Normally, UDP beacons are sent periodically to the broadcast address of the local network, on port 2190 (registered
 * to TiVo). Upon startup (meaning TiVo DVR boot up or activation of TiVo Connect software running on some other
 * hardware) a TCM broadcasts, for a short period of time (say, 30 seconds) a number of redundant packets in "high
 * frequency mode" (perhaps every 5 seconds). After this initial period, the TCM drops into "low frequency mode"
 * broadcasting at a reduced rate (maybe only once every minute). The initial high-frequency mode allows listeners
 * several chances to quickly detect TCMs that have just arrived, while the eventual low-frequency broadcasts should be
 * infrequent enough to not overly burden the network even in the presence of many TCMs. With broadcast-based discovery,
 * every TCM must be prepared to accept redundant packets, and should always assume they contain the latest information
 * about the originating TCM. Again, keep in mind that "connection-less" UDP beacons are sent blindly onto the network
 * with no guarantee that they'll ever be received. The periodic and redundant nature of the broadcastbased discovery
 * mechanism is necessary for it to work effectively.
 */

public class ListenThread extends Thread {
    private static Logger log = Logger.getLogger(ListenThread.class.getName());
    
    private final static String TIVO_PLATFORM_PREFIX = "tcd"; // platform=tcd/Series2

    public ListenThread(Server server) throws IOException {
        super("ListenThread");
        mServer = server;

        try {
            mLocalHost = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Tools.logException(ListenThread.class, ex);
        }
    }

    public void run() {
        byte[] buf = new byte[300];
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                mServer.getBroadcastThread().getSocket().receive(packet);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                Beacon beacon = new Beacon(buf);

                if (!address.getHostAddress().equals(mLocalHost.getHostAddress())) {
                    TCM tcm = mServer.getTCM(beacon);
                    if (beacon.getPlatform().startsWith(TIVO_PLATFORM_PREFIX))
                    {
	                    if (tcm == null) {
	                        if (log.isDebugEnabled())
	                            log.debug("Got beacon: " + beacon);
	                        mServer.addTCM(new TCM(address, beacon, false));
	                        mServer.getBroadcastThread().enableHighFrequency();
	                    } else {
	                        mServer.removeTCM(tcm);
	                        mServer.addTCM(new TCM(address, beacon, false));
	                    }
                    }
                }
                address = null;
                packet = null;
            } catch (Throwable ex) {
                Tools.logException(ListenThread.class, ex);
            }
        }
    }

    private Server mServer;

    private InetAddress mLocalHost;
}