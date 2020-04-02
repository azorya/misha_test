package com.zgconsultants.avro.tools;

import java.util.Arrays;
import org.apache.avro.Schema;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class zMain {
    private static final Logger LOG = LoggerFactory.getLogger(zMain.class);

    private final zFileDump     zfd;
    private final zSReg         zsr;
    private final zSchemaGetter zsg;

    private final boolean dump;
    private final boolean reg;

    private zMain(String[] args) throws Exception {

        CommandLine cmdl = parseArgs(args);

        Map<String, Object> conf =  new zConfMerger(cmdl).getConf();

        LOG.info("Using parameters: " + Arrays.toString(conf.entrySet().toArray()));

        dump = (boolean)conf.get("dump");
        reg  = (boolean)conf.get("reg");

        zfd = new zFileDump(conf);
        zsr = new zSReg(conf);
        zsg = new zSchemaGetter(conf);

        LOG.info("Completed");
    }


    private void doProcess(Schema schema) throws Exception {
        if (dump) {
            zfd.dump(schema);
            LOG.info("Dump for schema " + schema.getName() + " is completed.");
        }
        if (reg) {
            zsr.register(schema);
            LOG.info("Registration for schema " + schema.getName() + " is completed.");
        }
    }

    private void process() throws Exception {
        //zsg.getSchemas().forEach(schema -> doProcess(schema));
        for (Schema schema : zsg.getSchemas())
                doProcess(schema);
    }

    private static CommandLine parseArgs(String[] args) throws Exception {
        Options options = new Options();

        Option c = new Option("c", "className", true,
                "class name. Example: OrderMsg ");
        options.addOption(c);


        Option out = new Option("d", "outDir", true,
                "output directory");
        options.addOption(out);

        Option sch = new Option("s", "schemaRegistry", true,
                "schemaRegistry server. Example: http://10.10.0.141:8081");
        options.addOption(out);


        Option conf = new Option("conf", "configFile", true,
                "Full path to the user configFile");
        options.addOption(conf);


        Option dump = new Option("dump", "dump", false,
                "if dump schemas to the files");
        options.addOption(dump);

        Option reg = new Option("reg", "reg", false,
                "if registry our schemas in the schema registry service");
        options.addOption(reg);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter  = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            if (!cmd.hasOption("dump") && !cmd.hasOption("reg")) {
                formatter.printHelp("zSDump", options);
                throw new IllegalArgumentException("One must specify at least one of reg & dump options");
            }
        } catch (org.apache.commons.cli.ParseException e) {
            LOG.error(e.getMessage());
            formatter.printHelp("zSDump", options);

            throw e;
        }

        return cmd;
    }

    public static void main(String[] args) {

        try {
            zMain app = new zMain(args);
            app.process();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            System.exit(1);
        }
    }
}
