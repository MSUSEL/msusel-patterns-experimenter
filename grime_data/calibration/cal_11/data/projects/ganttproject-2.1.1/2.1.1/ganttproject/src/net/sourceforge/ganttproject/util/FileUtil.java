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
/*
 * Created on 16.10.2005
 */
package net.sourceforge.ganttproject.util;

import java.io.File;
import java.io.IOException;

public abstract class FileUtil {
    public static String getExtension(File file) {
        int lastDot = file.getName().lastIndexOf(FILE_EXTENSION_SEPARATOR);
        return lastDot>=0 ? file.getName().substring(lastDot+1) : file.getName();
    }
    
    public static File replaceExtension(File f, String newExtension) throws IOException {
        String filename = f.getName();
        int i = filename.lastIndexOf(FILE_EXTENSION_SEPARATOR);

        File containingFolder = f.getParentFile();
        File result;
        if (i > 0 && i < filename.length() - 1) {
            String withoutExtension = filename.substring(0, i);
            result = new File(containingFolder, withoutExtension+FILE_EXTENSION_SEPARATOR+newExtension);
        }
        else {
            result = new File(containingFolder, filename+FILE_EXTENSION_SEPARATOR+newExtension);
        }
        return result;
    }
    
    public static File appendSuffixBeforeExtension(File f, String suffix) throws IOException {
        String filename = f.getName();
        int i = filename.lastIndexOf(FILE_EXTENSION_SEPARATOR);

        File containingFolder = f.getParentFile();
        File result;
        if (i > 0 && i < filename.length() - 1) {
            String withoutExtension = filename.substring(0, i);
            String extension = filename.substring(i);
            result = new File(containingFolder, withoutExtension+suffix+extension);
        }
        else {
            result = new File(containingFolder, filename+suffix);
        }
        if (!result.exists()) {
            result.createNewFile();
        }
        return result;
        
    }
    
    public static String getFilenameWithoutExtension(File f) {
        String filename = f.getName();
        int i = filename.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        return i>=0 ?filename.substring(0, i) : filename; 
    }

    private static final char FILE_EXTENSION_SEPARATOR= '.';    
}
