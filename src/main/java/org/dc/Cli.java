package org.dc;

import org.apache.commons.cli.*;
import org.dc.crypto.Crypto;


public class Cli {
    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("e", "encrypt", false, "encrypt")
                .addOption("d", "decrypt", false, "decrypt")
                .addOption("k", "key", true, "master key. (Required)")
                .addOption("m", "message", true, "message. (Required)");

        HelpFormatter formatter = new HelpFormatter();


        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Crypto crypto;

        if (cmd.hasOption("e")) {
            if (!cmd.hasOption("m") || !cmd.hasOption("k")) {
                formatter.printHelp("crypto", options);
                System.exit(1);
            }
            crypto = new Crypto(cmd.getOptionValue("k"));
            crypto.init();
            System.out.println(crypto.encryptString(cmd.getOptionValue("m")));
            System.exit(0);
        }

        if (cmd.hasOption("d")) {
            if (!cmd.hasOption("m") || !cmd.hasOption("k")) {
                formatter.printHelp("crypto", options);
                System.exit(1);
            }
            crypto = new Crypto(cmd.getOptionValue("k"));
            crypto.init();
            System.out.println(crypto.decryptString(cmd.getOptionValue("m")));
            System.exit(0);
        }

        formatter.printHelp("crypto", options);
        System.exit(1);
    }
}
