package com.zgconsultants.avro.tools;

import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

class zFileDump {

    private static final Logger LOG = LoggerFactory.getLogger(zFileDump.class);

    private final String  fileExt;
    private final String  outDir;
    private final boolean pretty;

    zFileDump(Map<String, Object> conf) {
        fileExt   = (String)conf.get("fileExtention");
        outDir    = (String)conf.get("outDir");
        pretty    = conf.containsKey("prettyDump");
    }

    private String class2FileName(String className) {
        String fileName = className;
        // add suffix
        fileName +=  "." + fileExt;
        // add full path if outDir is defined
        if (!outDir.isEmpty())
            fileName = outDir + "/" + fileName;

        LOG.debug(fileName);

        return fileName;
    }

    private void doDump(String s, String file) {
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(s);
        }
        catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    void dump(Schema schema) {

        String s = schema.toString(pretty);
        doDump(s, class2FileName(schema.getName()));
    }
}
