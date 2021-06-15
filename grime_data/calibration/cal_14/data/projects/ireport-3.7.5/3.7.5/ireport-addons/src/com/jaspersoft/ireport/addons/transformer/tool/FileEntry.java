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
package com.jaspersoft.ireport.addons.transformer.tool;

import java.io.*;
/**
 *
 * @author  Administrator
 */
public class FileEntry {
    
    public static final int STATUS_NOT_TRANSFORMED = 1;
    public static final int STATUS_TRANSFORMED = 2;
    public static final int STATUS_ERROR_TRANSFORMING = 3;
    public static final int STATUS_TRANSFORMING = 5;
    
    private File file = null;
    private int status = 0;
    private String message = "";
    private String jasper_version = "";
    
    /** Creates a new instance of FileEntry */
    public FileEntry() {
    }
    
    
    /** Getter for property message.
     * @return Value of property message.
     *
     */
    public java.lang.String getMessage() {
        return message;
    }
    
    /** Setter for property message.
     * @param message New value of property message.
     *
     */
    public void setMessage(java.lang.String message) {
        this.message = message;
    }
    
    /** Getter for property status.
     * @return Value of property status.
     *
     */
    public int getStatus() {
        return status;
    }
    
    /** Getter for property status.
     * @return Value of property status.
     *
     */
    public static String decodeStatus( int status ) {
        // Decode the status...
        switch (status)
        {
            case STATUS_NOT_TRANSFORMED: return "Not transformed";
            case STATUS_TRANSFORMED: return "Transformed";
            case STATUS_ERROR_TRANSFORMING: return "Error transforming";
            case STATUS_TRANSFORMING: return "Transforming...";
        }
        return ""+status;
    }
    
    /** Setter for property status.
     * @param status New value of property status.
     *
     */
    public void setStatus(int status) {
        this.status = status;
    }
    
    /** Getter for property file.
     * @return Value of property file.
     *
     */
    public File getFile() {
        return file;
    }
    
    /** Setter for property file.
     * @param file New value of property file.
     *
     */
    public void setFile(File file) {
        this.file = file;
    }
    
    @Override
    public String toString()
    {
        if (file == null) return "";
        try {
        return file.getCanonicalPath();
        }catch (Exception ex) {}
        return "";
    }
    
    /** Getter for property jasper_version.
     * @return Value of property jasper_version.
     *
     */
    public java.lang.String getJasper_version() {
        return jasper_version;
    }
    
    /** Setter for property jasper_version.
     * @param jasper_version New value of property jasper_version.
     *
     */
    public void setJasper_version(java.lang.String jasper_version) {
        this.jasper_version = jasper_version;
    }
    
}
