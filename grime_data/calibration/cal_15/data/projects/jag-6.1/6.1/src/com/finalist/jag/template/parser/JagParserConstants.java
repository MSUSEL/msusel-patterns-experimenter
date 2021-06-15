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
package com.finalist.jag.template.parser;

public interface JagParserConstants {
   static int IDENT = 0;
   static int STRING = 1;
   static int INTEGER = 2;
   static int FLOAT = 3;
   static int TEXT = 4;

   static int HEADERDEF_BEGIN = 5;
   static int HEADERDEF_END = 6;
   static int TAGDEF_BEGIN = 7;
   static int TAGDEF_END = 8;
   static int TAGDEF_CLOSE = 9;
   static int ASSIGN = 10;
   static int SEMICOLON = 11;
   static int COLON = 12;

   static int LANGUAGE = 13;
   static int TAGSTART = 14;
   static int TAGEND = 15;
   static int TAGLIB = 16;
   static int URI = 17;
   static int PREFIX = 18;
   static int DEFINE = 19;
   static int PARAMDEF = 20;

   static int TAGNAME = 21;
   static int TAGACTION = 22;
   static int SLIST = 23;

   static int COMMENT_BEGIN = 24;
   static int COMMENT_END = 25;

   static final char EOF_CHAR = (char) -1;

   static String[] tokennames =
      {
         "IDENT", //IDENT 			= 0
         "STRING", //STRING 			= 1
         "INTEGER", //INTEGER 			= 2
         "FLOAT", //FLOAT 			= 3
         "TEXT", //TEXT 				= 4

         "<#@", //HEADERDEF_BEGIN	= 5
         "#>", //HEADERDEF_END		= 6
         "<", //TAGDEF_BEGIN		= 7
         ">", //TAGDEF_END		= 8
         "/", //TAGDEF_CLOSE		= 9
         "=", //ASSIGN			= 10
         ";", //SEMICOLON			= 11
         ":", //COLON				= 12

         "language", //LANGUAGE			= 13
         "tagstart", //TAGSTART			= 14
         "tagend", //TAGEND			= 15
         "taglib", //TAGLIB			= 16
         "uri", //URI				= 17
         "prefix", //PREFIX 			= 18
         "define", //DEFINE 			= 19

         "PARAMDEF", //PARAMDEF			= 20

         "TAGNAME", //TAGNAME			= 21
         "TAGACTION", //TAGACTION			= 22
         "SLIST", //SLIST				= 23

         "!--", //COMMENT_BEGIN		= 24
         "--"		//COMMENT_END		= 25
      };
}

;
