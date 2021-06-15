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
package org.archive.crawler.settings.refinements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.archive.crawler.settings.CrawlerSettings;
import org.archive.net.UURI;


/**
 * This class acts as a mapping between refinement criterias and a settings
 * object.
 *
 * @author John Erik Halse
 *
 */
public class Refinement {
    private final CrawlerSettings owner;
    private String description;
    private String operator = "Admin";
    private String organization = "";
    private String audience = "";
    private String reference;
    private List<Criteria> criteria = new ArrayList<Criteria>();


    /**
     * Create a new instance of Refinement
     *
     * @param owner the settings object that owns the refinement.
     * @param reference a name that combined with the owner uniquely identifies
     *            the refinement.
     */
    public Refinement(CrawlerSettings owner, String reference) {
        this.owner = owner;
        this.reference = reference;
        owner.addRefinement(this);
    }

    /** Create a new instance of Refinement
     *
     * @param owner the settings object that owns the refinement.
     * @param reference a name that combined with the owner uniquely identifies
     *            the refinement.
     * @param descr A textual description of the refinement.
     */
    public Refinement(CrawlerSettings owner, String reference, String descr) {
        this(owner, reference);
        this.description = descr;
    }

    /**
     * Check if a URI is within the bounds of every criteria set for this
     * refinement.
     *
     * @param uri the URI that shoulb be checked.
     * @return true if within bounds.
     */
    public boolean isWithinRefinementBounds(UURI uri) {
        if (uri == null || uri == null) {
            return false;
        }
        for (Iterator it = criteria.iterator(); it.hasNext();) {
            if (!((Criteria) it.next()).isWithinRefinementBounds(uri)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return the description of this refinement.
     *
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description for this refinement.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get an <code>ListIterator</code> over the criteria set for this
     * refinement.
     *
     * @return Returns an iterator over the criteria.
     */
    public ListIterator criteriaIterator() {
        return criteria.listIterator();
    }

    /**
     * Add a new criterion to this refinement.
     *
     * @param criterion the criterion to add.
     */
    public void addCriteria(Criteria criterion) {
        if (!criteria.contains(criterion)) {
            criteria.add(criterion);
        }
    }

    /**
     * Get the reference to this refinement's settings object.
     *
     * @return Returns the reference.
     */
    public String getReference() {
        return reference;
    }

    /**
     * Set the reference to this refinement's settings object.
     *
     * @param reference The reference to set.
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Get the <code>CrawlerSettings</code> object this refinement refers to.
     *
     * @return the settings object this refinement refers to.
     */
    public CrawlerSettings getSettings() {
        String parentScope = owner.getScope() == null ? "" : owner.getScope();
        CrawlerSettings settings = owner.getSettingsHandler()
                .getOrCreateSettingsObject(parentScope, getReference());
        settings.setDescription((getDescription()));
        return settings;
    }

    public boolean equals(Object o) {
        if (this == o
                || (o instanceof Refinement && this.reference
                        .equals(((Refinement) o).reference))) {
            return true;
        }
        return false;
    }

    /**
     * @return Returns the audience.
     */
    public String getAudience() {
        return this.audience;
    }
    /**
     * @param audience The audience to set.
     */
    public void setAudience(String audience) {
        this.audience = audience;
    }
    /**
     * @return Returns the operator.
     */
    public String getOperator() {
        return this.operator;
    }
    /**
     * @param operator The operator to set.
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }
    /**
     * @return Returns the organziation.
     */
    public String getOrganization() {
        return this.organization;
    }
    /**
     * @param organization The organziation to set.
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
