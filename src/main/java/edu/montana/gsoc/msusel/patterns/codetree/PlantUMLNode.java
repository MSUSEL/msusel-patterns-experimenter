package edu.montana.gsoc.msusel.patterns.codetree;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class PlantUMLNode {

    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder();
        builder.append("@startuml\n");
        builder.append("Bob -> Alice : hello\n");
        builder.append("@enduml\n");

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            SourceStringReader reader = new SourceStringReader(builder.toString());
            // Write the first image to "os"
            DiagramDescription desc = reader.outputImage(os, new FileFormatOption(FileFormat.PNG));

//            final String svg = new String(os.toByteArray(), Charset.forName("UTF-8"));

            byte[] imageInByte = os.toByteArray();

            InputStream in = new ByteArrayInputStream(imageInByte);
            BufferedImage bImage = ImageIO.read(in);

            JFrame frame = new JFrame("PlantUML");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(250, 250);
            Container c = frame.getContentPane();

            c.add(new JLabel(new ImageIcon(bImage)), BorderLayout.CENTER);

//            StringReader strReader = new StringReader(svg);
//            try {
//                SVGUniverse universe = new SVGUniverse();
//                universe.loadSVG(strReader, "test");
//
//                SVGPanel panel = new SVGPanel();
//                panel.setSvgUniverse(universe);
//                c.add(panel, BorderLayout.CENTER);
//            } catch (Exception ex) {
//                // do your error handling here
//            } finally {
//                strReader.close();
//            }

            frame.pack();
            frame.setVisible(true);
        } catch (IOException e) {

        }
    }
}
