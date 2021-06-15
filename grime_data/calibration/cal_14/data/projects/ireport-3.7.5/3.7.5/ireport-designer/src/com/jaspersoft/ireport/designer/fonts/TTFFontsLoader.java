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
package com.jaspersoft.ireport.designer.fonts;

import com.jaspersoft.ireport.designer.*;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gtoffoli
 */
public class TTFFontsLoader {

    public static java.awt.Font loadFont(String file)
    {
        java.awt.Font f = null;
        try {
            f = java.awt.Font.createFont(Font.TRUETYPE_FONT,  new java.io.FileInputStream(file));
        }
        catch (IllegalArgumentException ett)
        {
            System.out.println(ett.getMessage() +" No TrueType font");
        }
        catch (java.awt.FontFormatException ef)
        {
            System.out.println(ef.getMessage() +" FontFormatException");
        }
         catch (java.io.IOException ioex)
        {
            System.out.println(ioex.getMessage() +" IOException");
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage() +" General Exception");
        }

        return f;
    }

    public static List<IRFont> loadTTFFonts()
    {
        return loadTTFFonts(null);
    }
    
    
    public static List<IRFont> loadTTFFonts(TTFFontsLoaderMonitor monitor)
    {
        if (monitor != null) monitor.fontsLoadingStarted();
        
        if (monitor != null)
        {
             monitor.fontsLoadingStatusUpdated( "Initializing font loader"); //"fontsLoader.loadingFontsInit"
        }
        
        List<IRFont> fonts = new ArrayList<IRFont>();
        
        List<String> fontsPaths = IReportManager.getInstance().getFontpath();
        
        
        if (fontsPaths == null || fontsPaths.size() == 0)
        {
            fontsPaths = IReportManager.getInstance().getClasspath();
        }
//            String path = System.getProperty("java.class.path");
//            if (path != null && path.length()>0)
//            {
//                StringTokenizer st = new StringTokenizer(path,  System.getProperty("path.separator") );
//
//                while (st.hasMoreTokens())
//                {
//                    String path_str = st.nextToken();
//                            
//                    try { addFontNames(fonts, path_str, monitor); } catch (Exception ex) { 
//                 
//                        System.out.println( 
//                                Misc.formatString("Invalid font found in: {0}", new Object[]{path_str})); //"fontListLoader.invalidFont"
//                    }
//                }
//            }
//        }
//        else
//        {
        for (String path_str : fontsPaths)
            {
                addFontNames(fonts, path_str, monitor); 
            }
//        }
        
        if (monitor != null) monitor.fontsLoadingFinished();
        return fonts;
    }
    
    
    public static void addFontNames(List<IRFont> fonts, String path_str, TTFFontsLoaderMonitor monitor)
    {
                java.text.MessageFormat formatter = new java.text.MessageFormat("Loaded font {0}");
        
                java.io.File file = new java.io.File(path_str);
                if (!file.exists()) {

                    return;
                }
                if (file.isDirectory())
                {
                    String[] files = file.list( new java.io.FilenameFilter(){
                        public boolean accept(java.io.File dir, String filename)
                        {
// modified                          	
//                            return filename.toUpperCase().endsWith(".TTF") ;
                            return filename.toUpperCase().endsWith(".TTF") || filename.toUpperCase().endsWith(".TTC");
                        }
                    });

                    //added begin 
                    com.lowagie.text.pdf.FontMapper fontMapper = new com.lowagie.text.pdf.DefaultFontMapper();
                    
                    if (files == null)
                    {
                         System.out.println( 
                                Misc.formatString("Unable to list files in: {0}", new Object[]{""+file}));
                        return;
                    }
                    for (int i=0; i<files.length; ++i)
                    {
                        try {
                            if (files[i].toUpperCase().endsWith(".TTC")) {
                                    try {
                                            String[] names = com.lowagie.text.pdf.BaseFont.enumerateTTCNames(file.getPath() + File.separator + files[i]);
                                            for (int a = 0; a < names.length; a++) {

                                                    java.awt.Font f = fontMapper.pdfToAwt( com.lowagie.text.FontFactory.getFont( file.getPath() + File.separator + files[i]).getBaseFont(), 10  );
                                                    if (f != null) {
                                                            fonts.add(new IRFont(f, files[i]));
                                                    }
                                                    else
                                                    {
                                                        System.out.println( 
                                                            Misc.formatString("Failed to load font {0}", new Object[]{""+file.getPath() + File.separator +files[i]}));
                                                    }


                                                    if (monitor != null)
                                                    {
                                                        monitor.fontsLoadingStatusUpdated( 
                                                                formatter.format( new Object[]{  file.getPath() + File.separator + files[i]  }));
                                                    }
                                            }
                                    } catch(com.lowagie.text.DocumentException de) {
                                            System.out.println(de);
                                    } catch(java.io.IOException ioe) {
                                            System.out.println(ioe);
                                    }
                            } else {
                            //added end


                                java.awt.Font f = loadFont( file.getPath() + File.separator + files[i]);
                                if (f != null)
                                {
                                    fonts.add(new IRFont(f, files[i]));
                                    //System.out.println(""+ f.getFamily() + " " + f.getFontName() +" ("+file.getPath() + file.separator +files[i]+")");
                                }
                                else
                                {
                                    System.out.println( 
                                        Misc.formatString("Failed to load font {0}", new Object[]{""+file.getPath() + File.separator +files[i]}));
                                }


                                if (monitor != null)
                                {
                                    monitor.fontsLoadingStatusUpdated( 
                                            formatter.format( new Object[]{  file.getPath() + File.separator + files[i]  }));
                                }
                            }
                        
                        } catch (Exception ex) { 

                            System.out.println( 
                                    Misc.formatString("Invalid font found: {0}. Font skipped.", new Object[]{files[i]})); //"fontListLoader.invalidFont"
                        }
                        
                        
                    }
                } else if (path_str.toUpperCase().endsWith(".TTF"))
                {
                    // Try to load this file...
                    //System.out.println(""+ path_str);
                }
                else if (path_str.toUpperCase().endsWith(".TTC"))
                {
                    // Try to load this file...
                    //System.out.println("TTC: "+ path_str);
                }
    }
}
