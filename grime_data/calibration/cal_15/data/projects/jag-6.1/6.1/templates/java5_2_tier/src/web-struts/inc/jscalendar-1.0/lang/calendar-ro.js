/*
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
// ** I18N
Calendar._DN = new Array
("Duminică",
 "Luni",
 "Marţi",
 "Miercuri",
 "Joi",
 "Vineri",
 "Sâmbătă",
 "Duminică");
Calendar._SDN_len = 2;
Calendar._MN = new Array
("Ianuarie",
 "Februarie",
 "Martie",
 "Aprilie",
 "Mai",
 "Iunie",
 "Iulie",
 "August",
 "Septembrie",
 "Octombrie",
 "Noiembrie",
 "Decembrie");

// tooltips
Calendar._TT = {};

Calendar._TT["INFO"] = "Despre calendar";

Calendar._TT["ABOUT"] =
"DHTML Date/Time Selector\n" +
"(c) dynarch.com 2002-2005 / Author: Mihai Bazon\n" + // don't translate this this ;-)
"Pentru ultima versiune vizitaţi: http://www.dynarch.com/projects/calendar/\n" +
"Distribuit sub GNU LGPL.  See http://gnu.org/licenses/lgpl.html for details." +
"\n\n" +
"Selecţia datei:\n" +
"- Folosiţi butoanele \xab, \xbb pentru a selecta anul\n" +
"- Folosiţi butoanele " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " pentru a selecta luna\n" +
"- Tineţi butonul mouse-ului apăsat pentru selecţie mai rapidă.";
Calendar._TT["ABOUT_TIME"] = "\n\n" +
"Selecţia orei:\n" +
"- Click pe ora sau minut pentru a mări valoarea cu 1\n" +
"- Sau Shift-Click pentru a micşora valoarea cu 1\n" +
"- Sau Click şi drag pentru a selecta mai repede.";

Calendar._TT["PREV_YEAR"] = "Anul precedent (lung pt menu)";
Calendar._TT["PREV_MONTH"] = "Luna precedentă (lung pt menu)";
Calendar._TT["GO_TODAY"] = "Data de azi";
Calendar._TT["NEXT_MONTH"] = "Luna următoare (lung pt menu)";
Calendar._TT["NEXT_YEAR"] = "Anul următor (lung pt menu)";
Calendar._TT["SEL_DATE"] = "Selectează data";
Calendar._TT["DRAG_TO_MOVE"] = "Trage pentru a mişca";
Calendar._TT["PART_TODAY"] = " (astăzi)";
Calendar._TT["DAY_FIRST"] = "Afişează %s prima zi";
Calendar._TT["WEEKEND"] = "0,6";
Calendar._TT["CLOSE"] = "Închide";
Calendar._TT["TODAY"] = "Astăzi";
Calendar._TT["TIME_PART"] = "(Shift-)Click sau drag pentru a selecta";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%d-%m-%Y";
Calendar._TT["TT_DATE_FORMAT"] = "%A, %d %B";

Calendar._TT["WK"] = "spt";
Calendar._TT["TIME"] = "Ora:";
