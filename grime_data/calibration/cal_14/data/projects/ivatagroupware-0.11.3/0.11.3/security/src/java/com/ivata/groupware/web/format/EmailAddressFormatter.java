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
package com.ivata.groupware.web.format;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.servlet.jsp.PageContext;

import com.ivata.mask.web.format.HTMLFormatter;
import com.ivata.mask.web.format.URLFormat;
import com.ivata.mask.web.javascript.JavaScriptWindow;


/**
 * <p>Convert email addresses into links against the ivata groupware
 * email
 * system. This class can either convert single addreses or lists into
 * <code>HTML</code> anchors.</p>
 *
 * @since 2002-06-19
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 */
public class EmailAddressFormatter extends HTMLFormatter {

    /**
     * <p>Stores the string used as a separator between email
     * addresses.</p>
     *
     * @see #format
     *
     */
    private String separator = ";";

    /**
     * <p>Whether or not the email address itself should appear in the
     * link. If <code>false</code> the name of the person appears in the
     * link, otherwise the email address itself will appear.</p>
     */
    private boolean showAddress = false;

    /**
     * <p>Stores the current page context. This is needed to create the
     * <em>JavaScript</em> window.</p>
     */
    private PageContext pageContext;

    /**
     * <p>Format one email address as an anchor link. This expects the
     * address to be
     * formatted according to <a
     * href='http://www.faqs.org/rfcs/rfc822.HTML'>RFC822</a>.</p>
     *
     * @param addressString the emailAddress string to format.
     * @return a formatted email address linking to the ivata groupware
     * email system.
     */
    public String format(final String addressString) {
        // simple prerequisite: rubbish in -> rubbish out...
        if (addressString == null) {
            return null;
        }
        int startPosition, endPosition;
        InternetAddress address;

        // look for email address of the form "My Name" <my.name@provider.com>
        // TODO: change these links to links to the intranet
        try {
            if (((startPosition = addressString.indexOf('<')) != -1) &&
                ((endPosition = addressString.indexOf('>')) != -1)) {
                address = new InternetAddress(addressString.substring(startPosition + 1, endPosition), addressString.substring(0, startPosition));
            } else {
                address = new InternetAddress(addressString);
            }
            return format(address);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (javax.mail.internet.AddressException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Format an array of addresses as anchor links.</p>
     *
     * @param addresses the array of email adddresses to format.
     * @return a separated list of email address linking to the
     * ivata groupware email
     * system. Each address is separated by the separator specified in
     * {@link
     * #setSeperator setSeperator}.
     *
     */
    public String format(final Address[] addresses) {
        // simple prerequisite: rubbish in -> rubbish out...
        if (addresses == null) {
            return null;
        }
        return format(Arrays.asList(addresses));
    }

    /**
     * <p>Format a list of email addresses as anchor links.</p>
     *
     * @param addresses the list of email addresses to format.
     * @return a separated list of email address linking to the
     * ivata groupware email
     * system. Each address is separated by the separator specified in
     * {@link
     * #setSeperator setSeperator}.
     *
     */
    public String format(final List addresses) {
        // simple prerequisite: rubbish in -> rubbish out...
        if (addresses == null) {
            return null;
        }
        ListIterator addressesIterator = addresses.listIterator();
        String returnString = "";
        URLFormat URLFormat = new URLFormat();

        // go thro' all the emails and format them
        while (addressesIterator.hasNext()) {
            Object address = addressesIterator.next();
            String addressString;

            if ((address instanceof InternetAddress) || (address instanceof String)) {
                if (address instanceof InternetAddress) {
                    InternetAddress addressInternet = (InternetAddress) address;
                    String personalString;

                    addressString = addressInternet.getAddress();
                    if ((addressInternet.getPersonal() != null) &&
                        !addressInternet.getPersonal().equals("")) {

                        /* TODO: this code can be used to strip the quotes - not sure if this is needed
                         StringBuffer personalStringBuffer = new StringBuffer(addressInternet.getPersonal());
                         // strip leading and trailing quotes and spaces
                         char ch;
                         while((personalStringBuffer.length() > 0) &&
                         ((ch = personalStringBuffer.charAt(0)) == '"' || ch==' ')) {
                         personalStringBuffer.deleteCharAt(0);
                         }
                         int length = personalStringBuffer.length();
                         while((--length > 0) &&
                         ((ch = personalStringBuffer.charAt(length)) == '"' || ch==' ')) {
                         personalStringBuffer.deleteCharAt(length);
                         }
                         personalString = personalStringBuffer.toString();*/
                        personalString = addressInternet.getPersonal();
                    } else {
                        // if no name was given, use the address itself
                        personalString = addressInternet.getAddress();
                    }
                    // we can only format the link if the pageContext has been
                    // set
                    if (pageContext == null) {
                        if (personalString.equals(addressString)) {
                            addressString = personalString + " <" + addressString + ">";
                        }
                    } else {
                        // NOTE: please keep this up to date with the code in
                        // /webapp/mail/include/composePopUp.jspf
                        JavaScriptWindow popUp = new JavaScriptWindow();
                        HashMap composeParams = new HashMap();

                        if (personalString.equals(addressString)) {
                            composeParams.put("to", addressString);
                        } else {
                            // remove quotes from the personal string
                            StringBuffer to = new StringBuffer();

                            /**
                             * WE need quotes,
                             *
                            StringTokenizer st = new StringTokenizer(personalString, "\"");

                            while (st.hasMoreElements()) {
                                to.append(st.nextElement());
                            */
                            to.append(" <");
                            to.append(addressString);
                            to.append(">");
                            composeParams.put("to", to.toString());
                        }
                        popUp.setParams(composeParams);
                        popUp.setWindowName("composeWindow");
                        popUp.setFrameName("ivataCompose");
                        popUp.setPage("/mail/compose.jsp");
                        popUp.setHeight(540);
                        popUp.setWidth(550);
                        popUp.setHasScrollBars(false);
                        popUp.setPageContext(pageContext);
                        String tagBody = showAddress ? addressString : personalString;

                        // convert spaces in the name string
                        addressString = "<a href='' onclick='"
                                + popUp.toString()
                                + "return false'>" + super.format(tagBody) + "</a>";
                    }
                } else {
                    addressString = format((String) address);
                }
                // can't convert non-internet addresses
            } else {
                addressString = super.format(address.toString());
            }
            returnString += addressString;
            // print a semicolon between all the addresses
            if (addressesIterator.hasNext()) {
                returnString += separator + " ";
            }
        }
        return returnString;
    }

    /**
     * <p>Format a single address as an anchor link.</p>
     *
     * @param address the email Address to format.
     * @return a formatted email address linking to the ivata groupware
     * email system.
     *
     */
    public String format(final Address address) {
        // simple prerequisite: rubbish in -> rubbish out...
        if (address == null) {
            return null;
        }
        Address[  ] addresses = { address };

        return format(Arrays.asList(addresses));
    }

    /**
     * <p>Get the string used as a separator between email addresses.</p>
     *
     * @see #format
     *
     * @return the current value of the separator used between email
     * addresses.
     *
     */
    public final String getSeperator() {
        return separator;
    }

    /**
     * <p>Set the string used as a separator between email addresses.</p>
     *
     * @see #format
     *
     * @param separator the new value of the separator used between email
     * addresses.
     *
     */
    public final void setSeperator(final String separator) {
        this.separator = separator;
    }

    /**
     * <p>Default constructor.</p>
     *
     */
    public EmailAddressFormatter() {
        super();
    }

    /**
     * <p>Construct a new email formatter, using all of the formats of
     * another
     * formatter.</p>
     *
     */
    public EmailAddressFormatter(HTMLFormatter formatter) {
        super();
        // just set our formats to be the same as those of the formatter provided
        setFormats(new Vector(formatter.getFormats()));
    }

    /**
     * <p>Get whether or not the email address or the person's name should
     * appear in the body of the link.</p>
     *
     * @return <code>false</code> if the name of the person will appear in
     * the link, otherwise the email address itself will appear
     */
    public final boolean getShowAddress() {
        return showAddress;
    }

    /**
     * <p>Set whether or not the email address or the person's name should
     * appear in the body of the link.</p>
     *
     * @param showAddress set to <code>false</code> if the name of the
     * person should appear in the link, otherwise the email address
     * itself
     * will appear.
     */
    public final void setShowAddress(final boolean showAddress) {
        this.showAddress = showAddress;
    }

    /**
     * <p>Stores the current page context. This is needed to create the
     * <em>JavaScript</em> window.</p>
     *
     * @return the current value of pageContext.
     */
    public final PageContext getPageContext() {
        return pageContext;
    }

    /**
     * <p>Stores the current page context. This is needed to create the
     * <em>JavaScript</em> window.</p>
     *
     * @param pageContext the new value of pageContext.
     */
    public final void setPageContext(final PageContext pageContext) {
        this.pageContext = pageContext;
    }
}
