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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.ailis.wlandsuite.cli.UnpackProg;
import de.ailis.wlandsuite.game.Game;
import de.ailis.wlandsuite.game.blocks.GameMap;
import de.ailis.wlandsuite.io.SeekableInputStream;
import de.ailis.wlandsuite.rawgame.GameBlockType;


/**
 * Unpacks a game file into a directory.
 * 
 * @author Klaus Reimer (k@ailis.de)
 * @version $Revision$
 */

public class UnpackGame extends UnpackProg
{
    /**
     * @see de.ailis.wlandsuite.cli.UnpackProg#unpack(java.io.InputStream,
     *      java.io.File)
     */

    @Override
    public void unpack(InputStream input, File output) throws IOException
    {
        List<Integer> sizes;
        SeekableInputStream gameStream;
        int mapNo;
        int offset;
        GameBlockType type;
        Game game;
        File mapsDir;

        // Parse the game file
        game = Game.read(input);

        // Write all maps as XML to disk
        mapNo = 0;
        for (GameMap map: game.getMaps())
        {
            File mapFile;
            FileOutputStream outputStream;

            mapFile = new File(String.format("%s%cmap%02d.xml", new Object[] {
                output.getPath(), File.separatorChar, mapNo }));
            
            outputStream = new FileOutputStream(mapFile);
            try
            {
                map.writeXml(outputStream);
            }
            finally
            {
                outputStream.close();
            }
            
            // Increase map counter
            mapNo++;
        }
    }


    /**
     * Main method
     * 
     * @param args
     *            Command line arguments
     */

    public static void main(String[] args)
    {
        UnpackGame app;
        app = new UnpackGame();
        app.setHelp("help/unpackgame.txt");
        app.setProgName("unpackgame");
        app.start(args);
    }
}
