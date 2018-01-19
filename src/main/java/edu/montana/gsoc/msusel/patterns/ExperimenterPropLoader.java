package edu.montana.gsoc.msusel.patterns;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Slf4j
public class ExperimenterPropLoader {

    public static Properties loadProperties(String settingsDir) {
        Properties prop = new Properties();
        Path p = Paths.get(settingsDir + "experimenter.properties");

        if (Files.exists(p)) {
            try (BufferedReader br = Files.newBufferedReader(p)) {
                prop.load(br);
            } catch (IOException e) {
                log.error("Could not load Experimenter Properties file");
            }
        } else {
            log.warn("Could not find Experiment propertes in directory: " + p.toString());
        }

        return prop;
    }
}
