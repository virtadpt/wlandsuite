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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.ailis.wlandsuite.cli.ConvertProg;
import de.ailis.wlandsuite.rawgame.Game;


/**
 * Encrypts a Game file.
 * 
 * @author Klaus Reimer (k@ailis.de)
 * @version $Revision$
 */

public class EncryptGame extends ConvertProg
{
    /**
     * @see de.ailis.wlandsuite.cli.ConvertProg#convert(java.io.InputStream,
     *      java.io.OutputStream)
     */

    @Override
    public void convert(InputStream input, OutputStream output)
        throws IOException
    {
        Game game = Game.read(input, false);
        game.write(output, true);
    }


    /**
     * Main method
     * 
     * @param args
     *            Command line arguments
     */

    public static void main(String[] args)
    {
        EncryptGame app;

        app = new EncryptGame();
        app.setHelp("help/encryptgame.txt");
        app.setProgName("encryptgame");
        app.start(args);
    }
}
