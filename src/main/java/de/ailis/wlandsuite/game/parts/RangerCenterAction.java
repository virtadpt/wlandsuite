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

package de.ailis.wlandsuite.game.parts;

import java.io.IOException;
import java.io.InputStream;

import org.dom4j.Element;

import de.ailis.wlandsuite.io.SeekableOutputStream;
import de.ailis.wlandsuite.utils.StringUtils;
import de.ailis.wlandsuite.utils.XmlUtils;


/**
 * The ranger center data used in the Special Building Actions.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @version $Revision$
 */

public class RangerCenterAction implements Action
{
    /** The new action class to set when leaving the library */
    private int newActionClass;

    /** The new action to set when leaving the library */
    private int newAction;

    /** The library name */
    private String name;


    /**
     * Creates and returns a new Ranger Center object by reading its data from the
     * specified stream.
     *
     * @param stream
     *            The input stream
     * @return The Ranger Center
     * @throws IOException
     *             When file operation fails.
     */

    public static RangerCenterAction read(final InputStream stream) throws IOException
    {
        RangerCenterAction rangerCenter;
        byte[] bytes;
        int p;

        rangerCenter = new RangerCenterAction();

        rangerCenter.newActionClass = stream.read();
        rangerCenter.newAction = stream.read();

        // Read the name
        bytes = new byte[13];
        stream.read(bytes);
        p = 0;
        while (bytes[p] != 0 && p < 13)
            p++;
        rangerCenter.name = new String(bytes, 0, p, "ASCII");

        return rangerCenter;
    }


    /**
     * @see de.ailis.wlandsuite.game.parts.Action#write(de.ailis.wlandsuite.io.SeekableOutputStream,
     *      de.ailis.wlandsuite.game.parts.SpecialActionTable)
     */

    @Override
    public void write(final SeekableOutputStream stream,
        final SpecialActionTable specialActionTable) throws IOException
    {
        stream.write(0x83);

        stream.write(this.newActionClass);
        stream.write(this.newAction);

        stream.write(this.name.getBytes("ASCII"), 0, Math.min(this.name
            .length(), 13));
        for (int i = this.name.length(); i < 13; i++)
        {
            stream.write(0);
        }
    }


    /**
     * Returns the ranger center data as XML.
     *
     * @param id
     *            The action id
     * @return The ranger center data as XML
     */

    @Override
    public Element toXml(final int id)
    {
        Element element;

        element = XmlUtils.createElement("rangerCenter");

        element.addAttribute("id", StringUtils.toHex(id));
        if (this.name != null && this.name.length() != 0)
        {
            element.addAttribute("name", this.name);
        }
        if (this.newActionClass == 255)
        {
            element.addAttribute("newActionClass", StringUtils.toHex(this.newActionClass));
        }
        if (this.newAction == 255)
        {
            element.addAttribute("newAction", StringUtils.toHex(this.newAction));
        }

        return element;
    }


    /**
     * Creates and returns a new Ranger Center object by reading its data from XML.
     *
     * @param element
     *            The XML element
     * @return The Ranger Center data
     */

    public static RangerCenterAction read(final Element element)
    {
        RangerCenterAction library;

        library = new RangerCenterAction();
        library.name = element.attributeValue("name", "");
        library.newActionClass = StringUtils.toInt(element
            .attributeValue("newActionClass", "255"));
        library.newAction = StringUtils.toInt(element.attributeValue("newAction", "255"));

        return library;
    }


    /**
     * Returns the name.
     *
     * @return The name
     */

    public String getName()
    {
        return this.name;
    }


    /**
     * Sets the name.
     *
     * @param name
     *            The name to set
     */

    public void setName(final String name)
    {
        this.name = name;
    }


    /**
     * Returns the newAction.
     *
     * @return The newAction
     */

    public int getNewAction()
    {
        return this.newAction;
    }


    /**
     * Sets the newAction.
     *
     * @param newAction
     *            The newAction to set
     */

    public void setNewAction(final int newAction)
    {
        this.newAction = newAction;
    }


    /**
     * Returns the newActionClass.
     *
     * @return The newActionClass
     */

    public int getNewActionClass()
    {
        return this.newActionClass;
    }


    /**
     * Sets the newActionClass.
     *
     * @param newActionClass
     *            The newActionClass to set
     */

    public void setNewActionClass(final int newActionClass)
    {
        this.newActionClass = newActionClass;
    }
}
