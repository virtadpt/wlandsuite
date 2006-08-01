/*
 * $Id$
 * Copyright (C) 2006 Klaus Reimer <k@ailis.de>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package de.ailis.wlandsuite.cli;

import java.io.IOException;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import de.ailis.wlandsuite.utils.ResourceUtils;


/**
 * CLI program base class
 * 
 * @author Klaus Reimer (k@ailis.de)
 * @version $Revision$
 */

public abstract class CLIProg
{
    /** The help text resource */
    private String help = "help/launcher.txt";

    /** The version information text resource */
    private String version = "version.txt";

    /** The program name */
    private String progName = "wlandsuite";

    /** The long options of the program */
    private LongOpt[] longOpts = new LongOpt[0];

    /** Debug flag */
    private boolean debug = false;


    /**
     * Display command line help
     */

    private void help()
    {
        System.out.print(ResourceUtils.getText(this.help));
        System.exit(0);
    }


    /**
     * Display version information
     */

    private void version()
    {
        System.out.print(ResourceUtils.getText(this.version));
        System.exit(0);
    }


    /**
     * Outputs a usage error message
     * 
     * @param message
     *            The error message
     */

    protected void wrongUsage(String message)
    {
        System.err.println(this.progName + ": " + message);
        System.err.println("Try '" + this.progName
            + " --help' for more information.");
        System.exit(2);
    }


    /**
     * Aborts the program with an error message and exit code 1
     * 
     * @param message
     *            The error message
     */

    protected void error(String message)
    {
        System.err.println(this.progName + ": ERROR! " + message);
        System.exit(1);
    }


    /**
     * Displays a warning message and continues the program
     * 
     * @param message
     *            The warning message
     */

    protected void warn(String message)
    {
        System.err.println(this.progName + ": WARNING! " + message);
    }


    /**
     * Outputs an informational message
     * 
     * @param message
     *            The informational message
     */

    protected void info(String message)
    {
        System.err.println(this.progName + ": " + message);
    }


    /**
     * Process command line options
     * 
     * @param args
     *            Command line arguments
     * @return The parameters which are left after processing the options
     */

    private String[] processOptions(String[] args)
    {
        int c;
        LongOpt[] allLongOpts;
        String[] params;
        Getopt getopt;
        int i, p;
        StringBuilder shortOpts;

        // Setup long options
        allLongOpts = new LongOpt[3 + this.longOpts.length];
        allLongOpts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
        allLongOpts[1] = new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'V');
        allLongOpts[2] = new LongOpt("debug", LongOpt.NO_ARGUMENT, null, 'd');
        i = 3;
        for (LongOpt longOpt: this.longOpts)
        {
            allLongOpts[i] = longOpt;
            i++;
        }

        // Build the shortOpts
        shortOpts = new StringBuilder();
        for (LongOpt longOpt: allLongOpts)
        {
            shortOpts.append((char) longOpt.getVal());
            if (longOpt.getHasArg() == LongOpt.REQUIRED_ARGUMENT)
            {
                shortOpts.append(':');
            }
        }

        // Setup short options
        getopt = new Getopt(this.progName, args, shortOpts.toString(),
            allLongOpts);

        // Process options
        while ((c = getopt.getopt()) != -1)
        {
            switch (c)
            {
                case 'd':
                    this.debug = true;
                    break;
                case 'h':
                    help();
                    break;
                case 'V':
                    version();
                    break;
                default:
                    processOption(c, getopt);
            }
        }

        // Generate parameters,
        params = new String[args.length - getopt.getOptind()];
        p = 0;
        for (i = getopt.getOptind(); i < args.length; i++)
        {
            params[p] = args[i];
            p++;
        }
        return params;
    }


    /**
     * Processes a single option.
     * 
     * @param opt
     *            The option
     * @param getopt
     *            The Getopt object
     */

    protected void processOption(int opt, Getopt getopt)
    {
        // Empty
    }


    /**
     * The code wich should be executed when the program is run
     * 
     * @param params
     *            The parameters without the already processed options
     * @throws IOException
     */

    protected abstract void run(String[] params) throws IOException;


    /**
     * Runs the program
     * 
     * @param args
     *            The command line arguments.
     */

    protected void start(String[] args)
    {
        try
        {
            // Process command line arguments and run the program
            run(processOptions(args));
            info("Success");
        }
        catch (Exception e)
        {
            if (this.debug || e.getMessage() == null)
            {
                e.printStackTrace();
                System.exit(1);
            }
            else
            {
                error(e.getMessage());
            }
        }
    }


    /**
     * Sets the help resource.
     * 
     * @param help
     *            The help resource to set
     */

    public void setHelp(String help)
    {
        this.help = help;
    }


    /**
     * Sets the long options.
     * 
     * @param longOpts
     *            The long options to set
     */

    public void setLongOpts(LongOpt[] longOpts)
    {
        this.longOpts = longOpts;
    }


    /**
     * Sets the program name
     * 
     * @param progName
     *            The program name to set
     */

    public void setProgName(String progName)
    {
        this.progName = progName;
    }
}
