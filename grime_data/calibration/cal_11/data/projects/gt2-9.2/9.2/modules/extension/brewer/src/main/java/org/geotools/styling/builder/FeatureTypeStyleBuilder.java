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
package org.geotools.styling.builder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.geotools.Builder;
import org.geotools.feature.NameImpl;
import org.geotools.filter.IdBuilder;
import org.geotools.styling.Description;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.opengis.feature.type.Name;
import org.opengis.filter.Id;
import org.opengis.style.SemanticType;

/**
 * 
 *
 * @source $URL$
 */
public class FeatureTypeStyleBuilder extends AbstractStyleBuilder<FeatureTypeStyle> {
    String name;

    List<RuleBuilder> rules = new ArrayList<RuleBuilder>();

    DescriptionBuilder description = new DescriptionBuilder().unset();

    LinkedHashSet<Name> featureTypeNames = new LinkedHashSet<Name>();

    private IdBuilder<FeatureTypeStyleBuilder> definedFor = new IdBuilder<FeatureTypeStyleBuilder>(
            this);

    private Set<SemanticType> types = new LinkedHashSet<SemanticType>();

    // TODO : add semantic type identifier, provided it makes any sense to have it

    FeatureTypeStyleBuilder(StyleBuilder parent) {
        super(parent);
        reset();
    }

    public FeatureTypeStyleBuilder() {
        this(null);
    }

    public RuleBuilder rule() {
        RuleBuilder ruleBuilder = new RuleBuilder(this);
        rules.add(ruleBuilder);
        return ruleBuilder;
    }

    public FeatureTypeStyleBuilder name(String name) {
        this.name = name;
        return this;
    }

    public FeatureTypeStyleBuilder title(String title) {
        this.description.title(title);
        return this;
    }

    public DescriptionBuilder description() {
        return description;
    }

    /**
     * Accumulates another feature type name in the list of the feature type names for this
     * {@link FeatureTypeStyle}
     * 
     * @param featureTypeName
     * @return
     */
    public FeatureTypeStyleBuilder featureTypeName(String featureTypeName) {
        this.featureTypeNames.add(new NameImpl(featureTypeName));
        return this;
    }

    public String name() {
        return name;
    }

    public List<RuleBuilder> rules() {
        unset = false;
        return rules;
    }

    public FeatureTypeStyleBuilder rules(List<Rule> rules) {
        unset = false;
        for (Rule rule : rules) {
            this.rules.add(new RuleBuilder(this).reset(rule));
        }
        return this;
    }

    public FeatureTypeStyleBuilder description(Description description) {
        this.description.reset(description);
        this.unset = false;
        return this;
    }

    public LinkedHashSet<Name> featureTypeNames() {
        return featureTypeNames;
    }

    public void setFeatureTypeNames(List<Name> featureTypeNames) {
        this.featureTypeNames.addAll(featureTypeNames);
    }

    public IdBuilder<FeatureTypeStyleBuilder> definedFor() {
        return definedFor;
    }

    public void definedFor(Id fids) {
        this.definedFor.reset(fids);
    }

    public Set<SemanticType> types() {
        return types;
    }

    /**
     * Accumulates another feature type name in the list of the feature type names for this
     * {@link FeatureTypeStyle}
     * 
     * @param featureTypeName
     * @return
     */
    public FeatureTypeStyleBuilder featureTypeName(Name featureTypeName) {
        this.featureTypeNames.add(featureTypeName);
        unset = false;
        return this;
    }

    public FeatureTypeStyle build() {
        if (unset) {
            return null;
        }
        List<org.opengis.style.Rule> list = new ArrayList<org.opengis.style.Rule>();
        for (RuleBuilder ruleBuilder : rules) {
            list.add(ruleBuilder.build());
        }
        FeatureTypeStyle fts = sf.featureTypeStyle(name, description.build(), definedFor.build(),
                featureTypeNames, types, list);
        if (parent == null) {
            reset();
        }
        return fts;
    }

    public FeatureTypeStyleBuilder reset() {
        rules.clear();
        this.name = null;
        this.description.reset();
        this.definedFor.reset();
        this.featureTypeNames.clear();
        this.rules.clear();

        this.unset = false;
        return this;
    }

    public FeatureTypeStyleBuilder reset(FeatureTypeStyle fts) {
        if (fts == null) {
            return unset();
        }
        this.name = fts.getName();
        this.description.reset(fts.getDescription());
        this.definedFor.reset(fts.getFeatureInstanceIDs());
        this.featureTypeNames.clear();
        if (fts.featureTypeNames() != null) {
            this.featureTypeNames.addAll(fts.featureTypeNames());
        }
        this.rules.clear();
        if (fts.rules() != null) {
            for (Rule rule : fts.rules()) {
                this.rules.add(new RuleBuilder(this).reset(rule));
            }
        }
        this.unset = false;
        return this;
    }

    public FeatureTypeStyleBuilder unset() {
        return (FeatureTypeStyleBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().init(this);
    }

    private void init(FeatureTypeStyleBuilder other) {
        this.definedFor = other.definedFor;
        this.description = other.description;
        this.featureTypeNames = other.featureTypeNames;
        this.parent = other.parent;
        this.rules = other.rules;
        this.sf = other.sf;
        this.types = other.types;
        this.unset = other.unset;
    }

}
