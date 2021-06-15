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
package org.lnicholls.galleon.server;

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

import java.util.*;
import java.io.Serializable;

public class TiVo implements Serializable {

    public TiVo() {

    }

    public TiVo(String name, String address) {
        mName = name;
        mAddress = address;
    }

    public String getName() {
        return mName;
    }

    public void setName(String value) {
        mName = value;
    }
    
    public String getServer() {
        return mServer;
    }

    public void setServer(String value) {
        mServer = value;
    }    
    
    public int getPort() {
        return mPort;
    }

    public void setPort(int value) {
        mPort = value;
    }    
    
    public String getPlatform() {
        return mPlatform;
    }

    public void setPlatform(String value) {
        mPlatform = value;
    }    
    
    public String getServiceNumber() {
        return mServiceNumber;
    }

    public void setServiceNumber(String value) {
        mServiceNumber = value;
    }    
    
    public String getSoftwareVersion() {
        return mSoftwareVersion;
    }

    public void setSoftwareVersion(String value) {
        mSoftwareVersion = value;
    }    
    
    public String getPath() {
        return mPath;
    }

    public void setPath(String value) {
        mPath = value;
    }    

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String value) {
        mAddress = value;
    }
    
    public Date getLastChangedDate() {
        return mLastChangedDate;
    }

    public void setLastChangedDate(Date date) {
        mLastChangedDate = date;
    }    
    
    public int getNumShows() {
        return mNumShows;
    }

    public void setNumShows(int value) {
        mNumShows = value;
    }    
    
    public int getCapacity() {
        return mCapacity;
    }

    public void setCapacity(int value) {
        mCapacity = value;
    }    
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        synchronized (buffer) {
            buffer.append("Name=" + mName + '\n');
            buffer.append("Server=" + mServer + '\n');
            buffer.append("Address=" + mAddress + '\n');
            buffer.append("Port=" + mPort + '\n');
            buffer.append("Platform=" + mPlatform + '\n');
            buffer.append("SoftwareVersion=" + mSoftwareVersion + '\n');
            buffer.append("Path=" + mPath + '\n');
            buffer.append("LastChangedDate=" + mLastChangedDate + '\n');
            buffer.append("NumShows=" + mNumShows + '\n');
            buffer.append("Capacity=" + mCapacity + '\n');
        }
        return buffer.toString();
    }    

    private String mName = "";
    
    private String mServer = "";

    private String mAddress = "";
    
    private int mPort = 80;
    
    private String mPlatform = "";
    
    private String mServiceNumber = "";
    
    private String mSoftwareVersion = "";
    
    private String mPath = "";
    
    private Date mLastChangedDate = new Date(0);
    
    private int mNumShows = 0;
    
    private int mCapacity = 40;  //GB
}