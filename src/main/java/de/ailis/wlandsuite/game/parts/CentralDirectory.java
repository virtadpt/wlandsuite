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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import de.ailis.wlandsuite.game.GameException;
import de.ailis.wlandsuite.io.BitInputStreamWrapper;
import de.ailis.wlandsuite.io.BitOutputStreamWrapper;


/**
 * The central directory.
 * 
 * @author Klaus Reimer (k@ailis.de)
 * @version $Revision$
 */

public class CentralDirectory extends AbstractPart
{
    /** The strings offset */
    private int stringsOffset;

    /** The monster names offset */
    private int monsterNamesOffset;

    /** The monster data offset */
    private int monsterDataOffset;

    /** The action class master table */
    private int[] actionClassMasterTable = new int[16];


    /**
     * Constructor
     * 
     * @param bytes
     *            The game block data
     * @param offset
     *            The offset of the part in the game block
     */

    public CentralDirectory(byte[] bytes, int offset)
    {
        BitInputStreamWrapper bitStream;

        this.size = (16 + 3) * 2;
        this.offset = offset;
        bitStream = new BitInputStreamWrapper(new ByteArrayInputStream(bytes,
            offset, this.size));
        try
        {
            this.stringsOffset = bitStream.readWord();
            this.monsterNamesOffset = bitStream.readWord();
            this.monsterDataOffset = bitStream.readWord();
            for (int i = 0; i < 16; i++)
            {
                this.actionClassMasterTable[i] = bitStream.readWord();
            }
        }
        catch (IOException e)
        {
            throw new GameException(e.toString(), e);
        }
    }


    /**
     * Creates the central directory from XML.
     * 
     * @param element
     *            The XML element
     */

    @SuppressWarnings("unchecked")
    public CentralDirectory(Element element)
    {
        super();

        this.stringsOffset = Integer.parseInt(element.attributeValue(
            "stringsOffset").replace("0x", ""), 16);
        this.monsterNamesOffset = Integer.parseInt(element.attributeValue(
            "monsterNamesOffset").replace("0x", ""), 16);
        this.monsterDataOffset = Integer.parseInt(element.attributeValue(
            "monsterDataOffset").replace("0x", ""), 16);

        for (Element subElement: (List<Element>) element.elements())
        {
            int actionClass;
            int offset;

            actionClass = Integer.parseInt(subElement.attributeValue("actionClass")
                .replace("0x", ""), 16);
            offset = Integer.parseInt(subElement.attributeValue("offset")
                .replace("0x", ""), 16);
            this.actionClassMasterTable[actionClass] = offset;
        }
    }


    /**
     * @see de.ailis.wlandsuite.game.parts.Part#toXml()
     */

    public Element toXml()
    {
        Element element, subElement;

        element = DocumentHelper.createElement("centralDirectory");
        element.addAttribute("stringsOffset", String.format("0x%x",
            new Object[] { this.stringsOffset }));
        element.addAttribute("monsterNamesOffset", String.format("0x%x",
            new Object[] { this.monsterNamesOffset }));
        element.addAttribute("monsterDataOffset", String.format("0x%x",
            new Object[] { this.monsterDataOffset }));

        for (int i = 0; i < 16; i++)
        {
            subElement = DocumentHelper.createElement("actionClassOffset");
            subElement.addAttribute("actionClass", Integer.toHexString(i));
            subElement.addAttribute("offset", String.format("0x%x",
                new Object[] { this.actionClassMasterTable[i] }));
            element.add(subElement);
        }

        return element;
    }


    /**
     * @see de.ailis.wlandsuite.game.parts.Part#write(java.io.OutputStream)
     */

    public void write(OutputStream stream) throws IOException
    {
        BitOutputStreamWrapper bitStream;

        bitStream = new BitOutputStreamWrapper(stream);
        bitStream.writeWord(this.stringsOffset);
        bitStream.writeWord(this.monsterNamesOffset);
        bitStream.writeWord(this.monsterDataOffset);
        for (int offset: this.actionClassMasterTable)
        {
            bitStream.writeWord(offset);
        }
        bitStream.flush();
    }


    /**
     * Returns the monster data offset.
     * 
     * @return The monster data offset
     */

    public long getMonsterDataOffset()
    {
        return this.monsterDataOffset;
    }


    /**
     * Sets the monster data offset.
     * 
     * @param monsterDataOffset
     *            The monster data offset to set
     */

    public void setMonsterDataOffset(int monsterDataOffset)
    {
        this.monsterDataOffset = monsterDataOffset;
    }


    /**
     * Returns the monster names offset.
     * 
     * @return The monster names offset
     */

    public int getMonsterNamesOffset()
    {
        return this.monsterNamesOffset;
    }


    /**
     * Sets the monster names offset.
     * 
     * @param monsterNamesOffset
     *            The monster names offset to set
     */

    public void setMonsterNamesOffset(int monsterNamesOffset)
    {
        this.monsterNamesOffset = monsterNamesOffset;
    }


    /**
     * Returns the strings offset.
     * 
     * @return The strings offset
     */

    public int getStringsOffset()
    {
        return this.stringsOffset;
    }


    /**
     * Sets the strings offset.
     * 
     * @param stringsOffset
     *            The strings offset to set
     */

    public void setStringsOffset(int stringsOffset)
    {
        this.stringsOffset = stringsOffset;
    }

    /**
     * Returns the action class offset.
     * 
     * @param actionClass
     *            The action class
     * @return The offset
     */

    public int getActionClassOffset(int actionClass)
    {
        return this.actionClassMasterTable[actionClass];
    }


    /**
     * Sets an action class offset.
     * 
     * @param actionClass
     *            The action class
     * @param offset
     *            The offset
     */

    public void setActionClassOffset(byte actionClass, int offset)
    {
        this.actionClassMasterTable[actionClass] = offset;
    }
}
