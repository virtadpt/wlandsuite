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

package de.ailis.wlandsuite;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import de.ailis.wlandsuite.cli.ConvertProg;
import de.ailis.wlandsuite.io.WlfWriter;
import de.ailis.wlandsuite.utils.WlfUtils;


/**
 * Converts a standard image format file into a Wasteland WLF file.
 * 
 * @author Klaus Reimer (k@ailis.de)
 * @version $Revision$
 */

public class EncodeWlf extends ConvertProg
{
    /** The number of masks to read */
    private int masks = 0;


    /**
     * @see de.ailis.wlandsuite.cli.CLIProg#processOption(int,
     *      gnu.getopt.Getopt)
     */

    @Override
    protected void processOption(int opt, Getopt getopt)
    {
        switch (opt)
        {
            case 'm':
                this.masks = Integer.parseInt(getopt.getOptarg());
                break;
        }
    }


    /**
     * @see de.ailis.wlandsuite.cli.ConvertProg#convert(java.io.InputStream,
     *      java.io.OutputStream)
     */

    @Override
    protected void convert(InputStream input, OutputStream output)
        throws IOException
    {
        BufferedImage image;

        image = ImageIO.read(input);
        WlfWriter.getInstance().writeWlf(output,
            WlfUtils.split(image, this.masks));
    }


    /**
     * Main method
     * 
     * @param args
     *            Command line arguments
     */

    public static void main(String[] args)
    {
        EncodeWlf app;
        LongOpt[] longOpts;

        longOpts = new LongOpt[1];
        longOpts[0] = new LongOpt("masks", LongOpt.REQUIRED_ARGUMENT, null, 'm');

        app = new EncodeWlf();
        app.setHelp("help/encodewlf.txt");
        app.setProgName("encodewlf");
        app.setLongOpts(longOpts);
        app.start(args);
    }
}
