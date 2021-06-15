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
package org.geotools.ows.v2_0;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.ows20.Ows20Factory;

import org.geotools.ows.bindings.BoundingBoxTypeBinding;
import org.geotools.ows.bindings.UnitBinding;
import org.geotools.xlink.XLINKConfiguration;
import org.geotools.xml.ComplexEMFBinding;
import org.geotools.xml.Configuration;
import org.geotools.xml.SimpleContentComplexEMFBinding;
import org.geotools.xml.XMLConfiguration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/ows/2.0 schema.
 * 
 * @generated
 */
public class OWSConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */
    public OWSConfiguration() {
        super(OWS.getInstance());

        addDependency(new XMLConfiguration());
        addDependency(new XLINKConfiguration());
    }

    /**
     * Registers the bindings for the configuration.
     * 
     * @generated
     */
    @SuppressWarnings("unchecked")
    protected final void registerBindings(Map bindings) {
        // manually setup bindings
        bindings.put(OWS.BoundingBoxType, new BoundingBoxTypeBinding(Ows20Factory.eINSTANCE,
                OWS.BoundingBoxType));
        bindings.put(OWS.CodeType, new SimpleContentComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.CodeType));
        bindings.put(OWS.DomainMetadataType, new SimpleContentComplexEMFBinding(
                Ows20Factory.eINSTANCE, OWS.DomainMetadataType));
        bindings.put(OWS.LanguageStringType, new SimpleContentComplexEMFBinding(
                Ows20Factory.eINSTANCE, OWS.LanguageStringType));
        bindings.put(OWS.ValueType, new SimpleContentComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ValueType));
        bindings.put(OWS.UOM, new UnitBinding());

        // "automatic" bindings
        bindings.put(OWS.AbstractReferenceBaseType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.AbstractReferenceBaseType));
        bindings.put(OWS.AcceptFormatsType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.AcceptFormatsType));
        bindings.put(OWS.AcceptVersionsType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.AcceptVersionsType));
        bindings.put(OWS.AdditionalParametersBaseType, new ComplexEMFBinding(
                Ows20Factory.eINSTANCE, OWS.AdditionalParametersBaseType));
        bindings.put(OWS.AdditionalParametersType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.AdditionalParametersType));
        bindings.put(OWS.AddressType,
                new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.AddressType));
        bindings.put(OWS.BasicIdentificationType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.BasicIdentificationType));
        bindings.put(OWS.BoundingBoxType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.BoundingBoxType));
        bindings.put(OWS.CapabilitiesBaseType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.CapabilitiesBaseType));
        bindings.put(OWS.ContactType,
                new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.ContactType));
        bindings.put(OWS.ContentsBaseType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ContentsBaseType));
        bindings.put(OWS.DatasetDescriptionSummaryBaseType, new ComplexEMFBinding(
                Ows20Factory.eINSTANCE, OWS.DatasetDescriptionSummaryBaseType));
        bindings.put(OWS.DescriptionType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.DescriptionType));
        bindings.put(OWS.DomainType, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.DomainType));
        bindings.put(OWS.ExceptionType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ExceptionType));
        bindings.put(OWS.GetCapabilitiesType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.GetCapabilitiesType));
        bindings.put(OWS.GetResourceByIdType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.GetResourceByIdType));
        bindings.put(OWS.IdentificationType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.IdentificationType));
        bindings.put(OWS.KeywordsType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.KeywordsType));
        bindings.put(OWS.ManifestType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ManifestType));
        bindings.put(OWS.MetadataType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.MetadataType));
        bindings.put(OWS.NilValueType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.NilValueType));
        bindings.put(OWS.OnlineResourceType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.OnlineResourceType));
        bindings.put(OWS.PositionType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.PositionType));
        bindings.put(OWS.PositionType2D, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.PositionType2D));
        bindings.put(OWS.RangeType, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.RangeType));
        bindings.put(OWS.ReferenceGroupType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ReferenceGroupType));
        bindings.put(OWS.ReferenceType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ReferenceType));
        bindings.put(OWS.RequestMethodType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.RequestMethodType));
        bindings.put(OWS.ResponsiblePartySubsetType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ResponsiblePartySubsetType));
        bindings.put(OWS.ResponsiblePartyType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ResponsiblePartyType));
        bindings.put(OWS.SectionsType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.SectionsType));
        bindings.put(OWS.ServiceReferenceType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ServiceReferenceType));
        bindings.put(OWS.TelephoneType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.TelephoneType));
        bindings.put(OWS.UnNamedDomainType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.UnNamedDomainType));
        bindings.put(OWS.UpdateSequenceType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.UpdateSequenceType));
        bindings.put(OWS.WGS84BoundingBoxType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.WGS84BoundingBoxType));
        bindings.put(OWS._AdditionalParameter, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._AdditionalParameter));
        bindings.put(OWS._AllowedValues, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._AllowedValues));
        bindings.put(OWS._AnyValue, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS._AnyValue));
        bindings.put(OWS._DCP, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS._DCP));
        bindings.put(OWS._ExceptionReport, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._ExceptionReport));
        bindings.put(OWS._HTTP, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS._HTTP));
        bindings.put(OWS._NoValues, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS._NoValues));
        bindings.put(OWS._Operation, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS._Operation));
        bindings.put(OWS._OperationsMetadata, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._OperationsMetadata));
        bindings.put(OWS._ServiceIdentification, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._ServiceIdentification));
        bindings.put(OWS._ServiceProvider, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._ServiceProvider));
        bindings.put(OWS._ValuesReference, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._ValuesReference));
        bindings.put(OWS._rangeClosure, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._rangeClosure));
        bindings.put(OWS.CapabilitiesBaseType_Languages, new ComplexEMFBinding(
                Ows20Factory.eINSTANCE, OWS.CapabilitiesBaseType_Languages));
    }

    protected void configureContext(MutablePicoContainer container) {
        container.registerComponentInstance(Ows20Factory.eINSTANCE);
    }

    /**
     * Generates the bindings registrations for this class
     * 
     * @param args
     */
    public static void main(String[] args) {
        for (Field f : OWS.class.getFields()) {
            if ((f.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) != 0
                    && f.getType().equals(QName.class)) {
                System.out.println("bindings.put(OWS." + f.getName()
                        + ", new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS." + f.getName()
                        + "));");
            }
        }
    }
}