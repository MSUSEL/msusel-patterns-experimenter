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
package org.archive.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to run an external process.
 * @author stack
 * @version $Date: 2006-05-02 02:54:20 +0000 (Tue, 02 May 2006) $ $Revision: 4223 $
 */
public class ProcessUtils {
    private static final Logger LOGGER =
        Logger.getLogger(ProcessUtils.class.getName());
    
    protected ProcessUtils() {
        super();
    }
    
    /**
     * Thread to gobble up an output stream.
     * See http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html
     */
    protected class StreamGobbler extends Thread {
        private final InputStream is;
        private final StringBuffer sink = new StringBuffer();

        StreamGobbler(InputStream is, String name) {
            this.is = is;
            setName(name);
        }

        public void run() {
            try {
                BufferedReader br =
                    new BufferedReader(new InputStreamReader(this.is));
                for (String line = null; (line = br.readLine()) != null;) {
                    this.sink.append(line);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        
        public String getSink() {
            return this.sink.toString();
        }
    }
    
    /**
     * Data structure to hold result of a process exec.
     * @author stack
     * @version $Date: 2006-05-02 02:54:20 +0000 (Tue, 02 May 2006) $ $Revision: 4223 $
     */
    public class ProcessResult {
        private final String [] args;
        private final int result;
        private final String stdout;
        private final String stderr;
            
        protected ProcessResult(String [] args, int result, String stdout,
                    String stderr) {
            this.args = args;
            this.result = result;
            this.stderr = stderr;
            this.stdout = stdout;
        }
            
        public int getResult() {
            return this.result;
        }
            
        public String getStdout() {
            return this.stdout;
        }
            
        public String getStderr() {
            return this.stderr;
        }
                
        public String toString() {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < this.args.length; i++) {
                sb.append(this.args[i]);
                sb.append(", ");
            }
            return sb.toString() + " exit code: " + this.result +
                ((this.stderr != null && this.stderr.length() > 0)?
                    "\nSTDERR: " + this.stderr: "") +
                ((this.stdout != null && this.stdout.length() > 0)?
                    "\nSTDOUT: " + this.stdout: "");
        }
    }
        
    /**
     * Runs process.
     * @param args List of process args.
     * @return A ProcessResult data structure.
     * @throws IOException If interrupted, we throw an IOException. If non-zero
     * exit code, we throw an IOException (This may need to change).
     */
    public static ProcessUtils.ProcessResult exec(String [] args)
    throws IOException {
        Process p = Runtime.getRuntime().exec(args);
        ProcessUtils pu = new ProcessUtils();
        // Gobble up any output.
        StreamGobbler err = pu.new StreamGobbler(p.getErrorStream(), "stderr");
        err.setDaemon(true);
        err.start();
        StreamGobbler out = pu.new StreamGobbler(p.getInputStream(), "stdout");
        out.setDaemon(true);
        out.start();
        int exitVal;
        try {
            exitVal = p.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Wait on process " + args + " interrupted: "
                + e.getMessage());
        }
        ProcessUtils.ProcessResult result =
            pu.new ProcessResult(args, exitVal, out.getSink(), err.getSink());
        if (exitVal != 0) {
            throw new IOException(result.toString());
        } else if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(result.toString());
        }
        return result;
    }
}
