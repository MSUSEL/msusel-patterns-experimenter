package edu.montana.gsoc.msusel.patterns.sikulix;


import gr.uom.java.pattern.gui.MatrixFrame;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SikuliXTest {

    private static final String Pattern4Location = "/home/isaac/dev_tools/patterns/pattern4.jar";
    private static final String inputDirectory = "/home/git/msusel/msusel-patterns/target";
    private static final String outputXMLFile = "/home/git/msusel/msusel-patterns/test.xml";

    private static BufferedImage fileMenu = loadImage("/sikulix/file_menu.png");
    private static BufferedImage openDirectory = loadImage("/sikulix/open_directory_menu_item.png");
    private static BufferedImage folderInput = loadImage("/sikulix/folder_name_input.png");
    private static BufferedImage openFolder = loadImage("/sikulix/file_dialog_open_button.png");
    private static BufferedImage patternsMenu = loadImage("/sikulix/patterns_menu.png");
    private static BufferedImage allPatternsItem = loadImage("/sikulix/patterns_all_menu_item.png");
    private static BufferedImage menuBar = loadImage("/sikulix/menu_bar.png");
    private static BufferedImage exportXML = loadImage("/sikulix/export_xml_menu_item.png");
    private static BufferedImage saveFileInput = loadImage("/sikulix/save_dialog_file_name_input.png");
    private static BufferedImage saveFile = loadImage("/sikulix/save_dialog_save_button.png");
    private static BufferedImage windowButtons = loadImage("/sikulix/inner_window_buttons.png");

    public static void main(String args[]) {
        automateDetectorUI();

        System.out.println("Hello");
    }

    public static void automateDetectorUI() {
        Screen screen = new Screen();

        try {
            screen.find(new Pattern(fileMenu).exact());
            screen.click(new Pattern(fileMenu).exact());
            screen.find(new Pattern(openDirectory).targetOffset(-57, 0));
            screen.click(new Pattern(openDirectory).targetOffset(-57, 0));
            screen.type(folderInput, inputDirectory);
            screen.find(new Pattern(openFolder).exact());
            screen.click(new Pattern(openFolder).exact());
            screen.click(new Pattern(patternsMenu).exact());
            screen.click(new Pattern(allPatternsItem).similar(0.75f));
            screen.find(new Pattern(menuBar).similar(0.75f));
            screen.click(new Pattern(menuBar).similar(0.75f).targetOffset(-44, 0));
            screen.click(new Pattern(exportXML).targetOffset(-46, 0));
            screen.type(saveFileInput, outputXMLFile);
            screen.click(new Pattern(saveFile).exact());
            screen.find(new Pattern(windowButtons).exact());
            screen.click(new Pattern(windowButtons).targetOffset(22,-2));
        } catch (FindFailed e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage loadImage(String fileName){

        BufferedImage buff = null;
        try {
            buff = ImageIO.read(SikuliXTest.class.getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return buff;
    }
}
