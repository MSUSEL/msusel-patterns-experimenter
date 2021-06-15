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
package org.lnicholls.galleon.winamp;

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

import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SkinLoader {

    public SkinLoader(String filename) {
        mResources = new Hashtable();
        loadResource(filename);
    }

    private void loadResource(String filename) {
        ZipInputStream input = null;
        try {
            input = new ZipInputStream(new FileInputStream(filename));
            ZipEntry resource = input.getNextEntry();
            while (resource != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] data = new byte[1024];
                int success = input.read(data);
                while (success != -1) {
                    baos.write(data, 0, success);
                    success = input.read(data);
                }
                baos.close();

                String name = resource.getName().toLowerCase();
                if (name.endsWith("bmp")) {
                    BMPLoader bmp = new BMPLoader();
                    mResources.put(name, bmp.getBMPImage(new ByteArrayInputStream(baos.toByteArray())));
                } else if (name.endsWith("txt")) {
                    mResources.put(name, new String(baos.toByteArray()));
                } else if (resource.getName().toLowerCase().endsWith("ttf")) {
                    mResources.put(resource.getName(), Font.createFont(Font.TRUETYPE_FONT, new ByteArrayInputStream(
                            baos.toByteArray())));
                }
                resource = input.getNextEntry();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException ex) {
            }
        }
    }

    public Object getResource(String name) {
        return mResources.get(name.toLowerCase());
    }

    private Hashtable mResources;
}