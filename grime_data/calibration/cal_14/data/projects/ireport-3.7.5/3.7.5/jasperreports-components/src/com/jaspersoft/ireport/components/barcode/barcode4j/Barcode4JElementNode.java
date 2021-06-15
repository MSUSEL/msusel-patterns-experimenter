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
package com.jaspersoft.ireport.components.barcode.barcode4j;

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.components.barcode4j.BarcodeComponent;
import net.sf.jasperreports.components.barcode4j.CodabarComponent;
import net.sf.jasperreports.components.barcode4j.Code128Component;
import net.sf.jasperreports.components.barcode4j.Code39Component;
import net.sf.jasperreports.components.barcode4j.DataMatrixComponent;
import net.sf.jasperreports.components.barcode4j.EAN128Component;
import net.sf.jasperreports.components.barcode4j.EAN13Component;
import net.sf.jasperreports.components.barcode4j.EAN8Component;
import net.sf.jasperreports.components.barcode4j.FourStateBarcodeComponent;
import net.sf.jasperreports.components.barcode4j.Interleaved2Of5Component;
import net.sf.jasperreports.components.barcode4j.PDF417Component;
import net.sf.jasperreports.components.barcode4j.POSTNETComponent;
import net.sf.jasperreports.components.barcode4j.RoyalMailCustomerComponent;
import net.sf.jasperreports.components.barcode4j.UPCAComponent;
import net.sf.jasperreports.components.barcode4j.UPCEComponent;
import net.sf.jasperreports.components.barcode4j.USPSIntelligentMailComponent;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class Barcode4JElementNode extends ElementNode {

    public Barcode4JElementNode(JasperDesign jd, JRDesignElement element, Lookup doLkp)
    {
        super(jd, element,doLkp);
        setIconBaseWithExtension("com/jaspersoft/ireport/components/barcode/barcode-16.png");
        
    }

    @Override
    public String getDisplayName() {
        BarcodeComponent component = (BarcodeComponent)( (JRDesignComponentElement)getElement()).getComponent();
        String name = "";
        if (component instanceof CodabarComponent) name="Codabar";
        if (component instanceof EAN128Component) name="Codabar";
        if (component instanceof DataMatrixComponent) name="Data Matrix";
        if (component instanceof Code128Component) name="Code 128";
        if (component instanceof RoyalMailCustomerComponent) name="Royal Mail Customer";
        if (component instanceof USPSIntelligentMailComponent) name="USPS Intelligent Mail";
        if (component instanceof Interleaved2Of5Component) name="Interleaved 2 Of 5";
        if (component instanceof Code39Component) name="Code 39";
        if (component instanceof UPCAComponent) name="UPCA";
        if (component instanceof UPCEComponent) name="UPCE";
        if (component instanceof EAN13Component) name="EAN13";
        if (component instanceof EAN8Component) name="EAN8";
        if (component instanceof POSTNETComponent) name="PostNet";
        if (component instanceof PDF417Component) name="PDF417";

        return I18n.getString("Barcode4jElementNode.name", name);
    }




    /*
    @Override
    public Action[] getActions(boolean popup) {

        List<Action> actions = new ArrayList<Action>();
        Action[] originalActions = super.getActions(popup);

        actions.add(SystemAction.get(EditDatasetRunAction.class));
        for (int i=0; i<originalActions.length; ++i)
        {
            actions.add(originalActions[i]);
        }
        return actions.toArray(new Action[actions.size()]);
    }
    */

    @Override
    protected Sheet createSheet() {
        
        Sheet sheet = super.createSheet();
        
        // adding common properties...
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("Barcode");
        BarcodeComponent component = (BarcodeComponent)( (JRDesignComponentElement)getElement()).getComponent();
        set.setDisplayName(I18n.getString("Barcode"));
        //set.put(new BarbecueTypeProperty(component) );
        JRDesignDataset dataset = ModelUtils.getElementDataset(getElement(), getJasperDesign());

        set.put(new Barcode4JCodeExpressionProperty(component, dataset));
        set.put(new Barcode4JEvaluationTimeProperty(component, dataset));//, dataset));
        set.put(new Barcode4JEvaluationGroupProperty(component, dataset));
        set.put(new Barcode4JCodeExpressionProperty(component, dataset) );
        set.put(new Barcode4JPatternExpressionProperty(component, dataset) );
        set.put(new Barcode4JOrientationProperty(component) );
        set.put(new Barcode4JTextPositionProperty(component));
        set.put(new Barcode4JModuleWidthProperty(component));

        //set.put(new BarbecueDrawTextProperty(component) );
        //set.put(new BarbecueChecksumRequiredProperty(component) );
        //set.put(new BarbecueApplicationIdentifierExpressionProperty(component, ModelUtils.getElementDataset(getElement(), getJasperDesign())));
        
        if (component instanceof CodabarComponent)
        {
            set.put(new Barcode4JWideFactorProperty((CodabarComponent)component));
        }
        if (component instanceof EAN128Component)
        {
            set.put(new Barcode4JChecksumModeProperty(component));
        }
        if (component instanceof DataMatrixComponent)
        {
            set.put(new Barcode4JShapeProperty((DataMatrixComponent)component));
        }
        if (component instanceof FourStateBarcodeComponent)
        {
            set.put(new Barcode4JChecksumModeProperty(component));
            set.put(new Barcode4JAscenderHeightProperty((FourStateBarcodeComponent)component));
            set.put(new Barcode4JIntercharGapWidthProperty((FourStateBarcodeComponent)component));
            set.put(new Barcode4JTrackHeightProperty((FourStateBarcodeComponent)component));
            //set.put(new Barcode4JWideFactorProperty((CodabarComponent)component));
        }
        if (component instanceof Code39Component)
        {
            set.put(new Barcode4JChecksumModeProperty(component));
            set.put(new Barcode4JWideFactorProperty((Code39Component)component));
            set.put(new Barcode4JIntercharGapWidthProperty((Code39Component)component));
            set.put(new Barcode4JDisplayChecksumProperty(component));
            set.put(new Barcode4JDisplayStartStopProperty(component));
            set.put(new Barcode4JExtendedCharSetEnabledProperty(component));
        }
        if (component instanceof Interleaved2Of5Component)
        {
            set.put(new Barcode4JChecksumModeProperty(component));
            set.put(new Barcode4JDisplayChecksumProperty(component));
            set.put(new Barcode4JWideFactorProperty(component));
        }
        if (component instanceof UPCAComponent)
        {
            set.put(new Barcode4JChecksumModeProperty(component));
        }
        else if (component instanceof UPCEComponent)
        {
            set.put(new Barcode4JChecksumModeProperty(component));
        }
        else if (component instanceof EAN13Component)
        {
            set.put(new Barcode4JChecksumModeProperty(component));
        }
        else if (component instanceof EAN8Component)
        {
            set.put(new Barcode4JChecksumModeProperty(component));
        }
        else if (component instanceof POSTNETComponent)
        {
            set.put(new Barcode4JShortBarHeightProperty((POSTNETComponent)component));
            set.put(new Barcode4JChecksumModeProperty(component));
            set.put(new Barcode4JDisplayChecksumProperty(component));
            set.put(new Barcode4JIntercharGapWidthProperty(component));
            set.put(new Barcode4JBaselinePositionProperty((POSTNETComponent)component));
        }
        else if (component instanceof PDF417Component)
        {
            set.put(new Barcode4JMinColumnsProperty((PDF417Component)component));
            set.put(new Barcode4JMaxColumnsProperty((PDF417Component)component));
            set.put(new Barcode4JMinRowsProperty((PDF417Component)component));
            set.put(new Barcode4JMaxRowsProperty((PDF417Component)component));
            set.put(new Barcode4JWidthToHeightRatioProperty((PDF417Component)component));
            set.put(new Barcode4JErrorCorrectionLevelProperty((PDF417Component)component));
        }

        sheet.put( set);
        return sheet;
    }
 

}
