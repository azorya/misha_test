package com.zgconsultants.avro.tools;

import org.apache.commons.cli.CommandLine;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

class zConfMerger {

    private final Map<String, Object> conf;

    zConfMerger(CommandLine cmdl) throws FileNotFoundException {
        // get default config values
        Yaml yaml =  new Yaml();
        Class clazz = zMain.class;
        InputStream is = clazz.getResourceAsStream("/defaultConfig.yaml");
        conf = yaml.load(is);

        // replace/add values from user config
        String userConfigFile = cmdl.getOptionValue("configFile", "");
        if (!userConfigFile.isEmpty()) {
            conf.putAll(getUserConfig(userConfigFile));
        }

        // handle command line options
        String outDir = cmdl.getOptionValue("outDir", "");
        if (!outDir.isEmpty()) {
            conf.put("outDir", outDir);
        }

        String className = cmdl.getOptionValue("className", "");
        if (!className.isEmpty()) {
            conf.put("className", className);
        }

        String schemaRegistry = cmdl.getOptionValue("schemaRegistry", "");
        if (!schemaRegistry.isEmpty()) {
            conf.put("schemaRegistry", schemaRegistry);
        }

        conf.put("dump", cmdl.hasOption("dump"));
        conf.put("reg", cmdl.hasOption("reg"));
    }

    private Map<String, Object> getUserConfig(String userConfig) throws FileNotFoundException {

        Yaml yaml =  new Yaml();
        InputStream is = new FileInputStream(userConfig);

        return yaml.load(is);
    }

    Map<String, Object> getConf() {
        return conf;
    }
}
