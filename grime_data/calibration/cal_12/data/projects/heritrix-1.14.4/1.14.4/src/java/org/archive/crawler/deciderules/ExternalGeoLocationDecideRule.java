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
package org.archive.crawler.deciderules;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.CrawlHost;
import org.archive.crawler.settings.SimpleType;
import org.xbill.DNS.Address;

/**
 * A rule that can be configured to take alternate implementations
 * of the ExternalGeoLocationInterface.
 * If no implementation specified, or none found, returns configured decision.
 * If host in URI has been resolved checks CrawlHost for the country code
 * determination.
 * If country code is not present, does country lookup, and saves the country
 * code to <code>CrawlHost</code> for future consultation.
 * If country code is present in <code>CrawlHost</code>, compares it against
 * the configured code.
 * Note that if a host's IP address changes during the crawl, we still consider
 * the associated hostname to be in the country of its original IP address.
 * 
 * @author Igor Ranitovic
 */
public class ExternalGeoLocationDecideRule
extends PredicatedDecideRule {

    private static final long serialVersionUID = -32974116429860725L;

    private static final Logger LOGGER =
        Logger.getLogger(ExternalGeoLocationDecideRule.class.getName());
    static final String ATTR_IMPLEMENTATION = "implementation-class";
    static final String ATTR_COUNTRY_CODE = "country-code";
    static final String DEFAULT_COUNTRY_CODE = "--";
    private String countryCode;
    private ExternalGeoLookupInterface implementation = null;

    /**
     * @param name Name of this rule.
     */
    public ExternalGeoLocationDecideRule(String name) {
        super(name);
        setDescription("ExternalGeoLocationDecideRule. Rule that " +
            "instantiates implementations of the ExternalGeoLookupInterface. " +
            "The implementation needs to be present on the classpath. " +
            "On initialization, the implementation is instantiated (" +
            "assumption is that there is public constructor that takes +" +
            "country code).");
        addElementToDefinition(new SimpleType(ATTR_IMPLEMENTATION,
            "Name of implementation of ExternalGeoLookupInterface class to " +
            "instantiate.", ""));
        addElementToDefinition(new SimpleType(ATTR_COUNTRY_CODE,
                "Country code name.", ""));

    }
    
    protected boolean evaluate(Object obj) {
        ExternalGeoLookupInterface impl = getConfiguredImplementation(obj);
        if (impl == null) {
            return false;
        }
        CrawlHost crawlHost = null;
        String host;
        InetAddress address;
        try {
			if (obj instanceof CandidateURI) {
				host = ((CandidateURI) obj).getUURI().getHost();
				crawlHost = getSettingsHandler().getOrder()
				   .getController().getServerCache().getHostFor(host);
				if (crawlHost.getCountryCode() != null){
				   return (crawlHost.getCountryCode().equals(countryCode))
				   				? true : false;
				}
				address = crawlHost.getIP();
				if (address == null) {
					address = Address.getByName(host); 
				}
				crawlHost.setCountryCode((String)impl.lookup(address));
				if (crawlHost.getCountryCode().equals(countryCode)){
					LOGGER.fine("Country Code Lookup: " + " " + host +
							crawlHost.getCountryCode());
					return true;
				}
			}
		} catch (UnknownHostException e) {
			LOGGER.log(Level.FINE, "Failed dns lookup " + obj, e);
			if (crawlHost != null){
				crawlHost.setCountryCode(DEFAULT_COUNTRY_CODE);
			}
		} catch (URIException e) {
			LOGGER.log(Level.FINE, "Failed to parse hostname " + obj, e);
		}
		
		return false;
    }
    
    /**
	 * Get implementation, if one specified. If none specified, will keep trying
	 * to find one. Will be messy if the provided class is not-instantiable
	 * 
	 * @param o A context object.
	 * @return Instance of <code>ExternalGeoLookupInterface</code> or null.
	 */
    protected synchronized ExternalGeoLookupInterface
            getConfiguredImplementation(Object o) {
        if (this.implementation != null) {
            return this.implementation;
        }
        ExternalGeoLookupInterface result = null;
        try {
        	String className =
                (String)getAttribute(o, ATTR_IMPLEMENTATION);
            countryCode = (String)getAttribute(o, ATTR_COUNTRY_CODE);
            if (className != null && className.length() != 0) {
                Object obj = Class.forName(className).getConstructor(new Class[]
                      {String.class}).newInstance(new Object[] {countryCode});
                if (!(obj instanceof ExternalGeoLookupInterface)) {
                    LOGGER.severe("Implementation " + className + 
                        " does not implement ExternalGeoLookupInterface");
                }
                result = (ExternalGeoLookupInterface)obj;
                this.implementation = result;
            }
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        } 
        return result;
    }
}