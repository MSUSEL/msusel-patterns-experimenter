package edu.montana.gsoc.msusel.arc.db

@Singleton
class ConfigLoader {

    Properties loadConfiguration(File props) {
        if (!props)
            throw new IllegalArgumentException("loadConfiguration: props cannot be null")

        if (!props.exists()) {
            System.err << "Config file ${props.name} does not exist\n"
            System.exit 1
        }

        Properties p = new Properties()
        p.load(props.newInputStream())

        return p
    }
}
