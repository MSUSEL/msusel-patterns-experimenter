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
package com.itextpdf.text.pdf.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

/**
 * Tool that parses the content of a PDF document.
 * @since	2.1.4
 */
public class PdfContentReaderTool {

	/**
	 * Shows the detail of a dictionary.
	 * This is similar to the PdfLister functionality.
	 * @param dic	the dictionary of which you want the detail
	 * @return	a String representation of the dictionary
	 */
    static public String getDictionaryDetail(PdfDictionary dic){
        return getDictionaryDetail(dic, 0);
    }

    /**
     * Shows the detail of a dictionary.
     * @param dic	the dictionary of which you want the detail
     * @param depth	the depth of the current dictionary (for nested dictionaries)
     * @return	a String representation of the dictionary
     */
    static public String getDictionaryDetail(PdfDictionary dic, int depth){
        StringBuffer builder = new StringBuffer();
        builder.append('(');
        List<PdfName> subDictionaries = new ArrayList<PdfName>();
        for (PdfName key: dic.getKeys()) {
            PdfObject val = dic.getDirectObject(key);
            if (val.isDictionary())
                subDictionaries.add(key);
            builder.append(key);
            builder.append('=');
            builder.append(val);
            builder.append(", ");
        }
        builder.setLength(builder.length()-2);
        builder.append(')');
        for (PdfName pdfSubDictionaryName: subDictionaries) {
            builder.append('\n');
            for(int i = 0; i < depth+1; i++){
                builder.append('\t');
            }
            builder.append("Subdictionary ");
            builder.append(pdfSubDictionaryName);
            builder.append(" = ");
            builder.append(getDictionaryDetail(dic.getAsDict(pdfSubDictionaryName), depth+1));
        }
        return builder.toString();
    }

    /**
     * Displays a summary of the entries in the XObject dictionary for the stream
     * @param resourceDic the resource dictionary for the stream
     * @return a string with the summary of the entries
     * @throws IOException
     * @since 5.0.2
     */
    static public String getXObjectDetail(PdfDictionary resourceDic) throws IOException {
        StringBuilder sb = new StringBuilder();
        
        PdfDictionary xobjects = resourceDic.getAsDict(PdfName.XOBJECT);
        if (xobjects == null)
        	return "No XObjects";
        for (PdfName entryName : xobjects.getKeys()) {
            PdfStream xobjectStream = xobjects.getAsStream(entryName);
            
            sb.append("------ " + entryName + " - subtype = " + xobjectStream.get(PdfName.SUBTYPE) + " = " + xobjectStream.getAsNumber(PdfName.LENGTH) + " bytes ------\n");
            
            if (!xobjectStream.get(PdfName.SUBTYPE).equals(PdfName.IMAGE)){
            
                byte[] contentBytes = ContentByteUtils.getContentBytesFromContentObject(xobjectStream);
                
                InputStream is = new ByteArrayInputStream(contentBytes);
                int ch;
                while ((ch = is.read()) != -1){
                    sb.append((char)ch);
                }
    
                sb.append("------ " + entryName + " - subtype = " + xobjectStream.get(PdfName.SUBTYPE) + "End of Content" + "------\n");
            }
        }
       
        return sb.toString();
    }
    
    /**
     * Writes information about a specific page from PdfReader to the specified output stream.
     * @since 2.1.5
     * @param reader    the PdfReader to read the page content from
     * @param pageNum   the page number to read
     * @param out       the output stream to send the content to
     * @throws IOException
     */
    static public void listContentStreamForPage(PdfReader reader, int pageNum, PrintWriter out) throws IOException {
        out.println("==============Page " + pageNum + "====================");
        out.println("- - - - - Dictionary - - - - - -");
        PdfDictionary pageDictionary = reader.getPageN(pageNum);
        out.println(getDictionaryDetail(pageDictionary));

        out.println("- - - - - XObject Summary - - - - - -");
        out.println(getXObjectDetail(pageDictionary.getAsDict(PdfName.RESOURCES)));
        
        out.println("- - - - - Content Stream - - - - - -");
        RandomAccessFileOrArray f = reader.getSafeFile();

        byte[] contentBytes = reader.getPageContent(pageNum, f);
        f.close();

        out.flush();

        InputStream is = new ByteArrayInputStream(contentBytes);
        int ch;
        while ((ch = is.read()) != -1){
            out.print((char)ch);
        }

        out.flush();
        
        out.println("- - - - - Text Extraction - - - - - -");
        String extractedText = PdfTextExtractor.getTextFromPage(reader, pageNum, new LocationTextExtractionStrategy());
        if (extractedText.length() != 0)
            out.println(extractedText);
        else
            out.println("No text found on page " + pageNum);

        out.println();

    }

    /**
     * Writes information about each page in a PDF file to the specified output stream.
     * @since 2.1.5
     * @param pdfFile	a File instance referring to a PDF file
     * @param out       the output stream to send the content to
     * @throws IOException
     */
    static public void listContentStream(File pdfFile, PrintWriter out) throws IOException {
        PdfReader reader = new PdfReader(pdfFile.getCanonicalPath());

        int maxPageNum = reader.getNumberOfPages();

        for (int pageNum = 1; pageNum <= maxPageNum; pageNum++){
            listContentStreamForPage(reader, pageNum, out);
        }

    }

    /**
     * Writes information about the specified page in a PDF file to the specified output stream.
     * @since 2.1.5
     * @param pdfFile   a File instance referring to a PDF file
     * @param pageNum   the page number to read
     * @param out       the output stream to send the content to
     * @throws IOException
     */
    static public void listContentStream(File pdfFile, int pageNum, PrintWriter out) throws IOException {
        PdfReader reader = new PdfReader(pdfFile.getCanonicalPath());

        listContentStreamForPage(reader, pageNum, out);
    }

    /**
     * Writes information about each page in a PDF file to the specified file, or System.out.
     * @param args
     */
    public static void main(String[] args) {
        try{
            if (args.length < 1 || args.length > 3){
                System.out.println("Usage:  PdfContentReaderTool <pdf file> [<output file>|stdout] [<page num>]");
                return;
            }

            PrintWriter writer = new PrintWriter(System.out);
            if (args.length >= 2){
                if (args[1].compareToIgnoreCase("stdout") != 0){
                    System.out.println("Writing PDF content to " + args[1]);
                    writer = new PrintWriter(new FileOutputStream(new File(args[1])));
                }
            }

            int pageNum = -1;
            if (args.length >= 3){
                pageNum = Integer.parseInt(args[2]);
            }

            if (pageNum == -1){
                listContentStream(new File(args[0]), writer);
            } else {
                listContentStream(new File(args[0]), pageNum, writer);
            }
            writer.flush();

            if (args.length >= 2){
                writer.close();
                System.out.println("Finished writing content to " + args[1]);
            }
        } catch (Exception e){
            e.printStackTrace(System.err);
        }
    }

}
