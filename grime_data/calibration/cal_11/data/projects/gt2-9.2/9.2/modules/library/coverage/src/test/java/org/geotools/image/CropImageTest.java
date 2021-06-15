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
package org.geotools.image;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.TileCache;
import javax.media.jai.operator.ExtremaDescriptor;
import javax.media.jai.operator.SubtractDescriptor;

import org.geotools.image.crop.GTCropDescriptor;
import org.junit.Assert;
import org.junit.Test;

import com.sun.media.jai.util.SunTileCache;

public class CropImageTest {

    @Test
    public void testCropImagePB() {
        BufferedImage source = buildSource();
        
        ParameterBlock pb = buildParameterBlock(source);
        
        RenderedOp cropped = JAI.create("crop", pb);
        RenderedOp gtCropped = JAI.create("GTCrop", pb);
        assertImageEquals(cropped, gtCropped);
    }

    @Test
    public void testTileCache() {
        TileCache tc = new SunTileCache();
        RenderingHints hints = new RenderingHints(JAI.KEY_TILE_CACHE, tc);
        
        BufferedImage source = buildSource();
        ParameterBlock pb = buildParameterBlock(source);
        
        RenderedOp gtCropped = JAI.create("GTCrop", pb, hints);
        gtCropped.getColorModel(); // force to compute the image
        assertSame(tc,  gtCropped.getRenderingHint(JAI.KEY_TILE_CACHE));
    }
    
    @Test
    public void testNullTileCache() {
        RenderingHints hints = new RenderingHints(JAI.KEY_TILE_CACHE, null);
        
        BufferedImage source = buildSource();
        ParameterBlock pb = buildParameterBlock(source);
        
        RenderedOp gtCropped = JAI.create("GTCrop", pb, hints);
        gtCropped.getColorModel(); // force to compute the image
        assertNull(gtCropped.getRenderingHint(JAI.KEY_TILE_CACHE));
    }
    
    @Test
    public void testNullTileCacheDescriptor() {
        RenderingHints hints = new RenderingHints(JAI.KEY_TILE_CACHE, null);
        
        BufferedImage source = buildSource();
        
        RenderedOp gtCropped = GTCropDescriptor.create(source, 10f, 10f, 20f, 20f, hints);
        gtCropped.getColorModel(); // force to compute the image
        assertNull(gtCropped.getRenderingHint(JAI.KEY_TILE_CACHE));
    }
    
    private BufferedImage buildSource() {
        BufferedImage source = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) source.getGraphics();
        g.setPaint(new GradientPaint(new Point(0, 0), Color.WHITE, new Point(100, 100), Color.BLACK));
        g.dispose();
        return source;
    }

    private void assertImageEquals(RenderedOp first, RenderedOp second) {
        RenderedOp difference = SubtractDescriptor.create(first, second, null);
        RenderedOp stats = ExtremaDescriptor.create(difference, null, 1, 1, false, 1, null);
        
        double[] minimum = (double[]) stats.getProperty("minimum");
        double[] maximum = (double[]) stats.getProperty("maximum");
        assertEquals(minimum[0], maximum[0], 0.0);
        assertEquals(minimum[1], maximum[1], 0.0);
        assertEquals(minimum[2], maximum[2], 0.0);
    }

    private ParameterBlock buildParameterBlock(BufferedImage source) {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(source);
        pb.add((float) 10);
        pb.add((float) 50);
        pb.add((float) 20);
        pb.add((float) 20);
        return pb;
    }
}
