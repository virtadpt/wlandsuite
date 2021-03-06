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

import org.dom4j.Element;

import de.ailis.wlandsuite.common.exceptions.GameException;
import de.ailis.wlandsuite.io.SeekableInputStream;
import de.ailis.wlandsuite.io.SeekableOutputStream;
import de.ailis.wlandsuite.utils.StringUtils;
import de.ailis.wlandsuite.utils.XmlUtils;


/**
 * A loot item used in loot bags.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @version $Revision$
 */

public class LootItem
{
    /** Constant for skill check */
    public static final int TYPE_ITEM = 0;

    /** Constant for item check */
    public static final int TYPE_ITEMGROUP = 1;

    /** Constant for attribute check */
    public static final int TYPE_FIXEDMONEY = 2;

    /** Constant for party member check */
    public static final int TYPE_RANDOMMONEY = 3;

    /** The XML names for the different check types */
    private static final String xmlNames[] = { "item", "itemGroup",
        "fixedMoney", "randomMoney" };

    /** The check type. One of the TYPE_* constants */
    private final int type;

    /** The check value (For example the item id or group id) */
    private final int value;

    /** The number of items (or the amount of money */
    private final int quantity;


    /**
     * Constructor
     *
     * @param type
     *            The item type
     * @param value
     *            The item value
     * @param quantity
     *            The item quantity
     */

    public LootItem(final int type, final int value, final int quantity)
    {
        this.type = type;
        this.value = value;
        this.quantity = quantity;
    }


    /**
     * Returns the item quantity.
     *
     * @return The item quantity
     */

    public int getQuantity()
    {
        return this.quantity;
    }


    /**
     * Returns the item value.
     *
     * @return The item value
     */

    public int getValue()
    {
        return this.value;
    }


    /**
     * Returns the type.
     *
     * @return The type
     */

    public int getType()
    {
        return this.type;
    }


    /**
     * Creates and returns a new item by reading its data from the specified
     * stream. If no more items are on the stream then null is returned.
     *
     * @param stream
     *            The input stream
     * @return The item
     * @throws IOException
     *             When file operation fails.
     */

    public static LootItem read(final SeekableInputStream stream) throws IOException
    {
        int type, value, b, quantity;
        boolean highBit;

        b = stream.read();
        if (b == 255) return null;
        highBit = (b & 128) == 128;
        value = b & 127;
        if (value == 0x5e)
        {
            type = highBit ? TYPE_FIXEDMONEY : TYPE_RANDOMMONEY;
            quantity = stream.readWord();
        }
        else
        {
            type = highBit ? TYPE_ITEM : TYPE_ITEMGROUP;
            quantity = stream.read();
        }

        return new LootItem(type, value, quantity);
    }


    /**
     * Writes the loot data to the specified stream.
     *
     * @param stream
     *            The output stream
     */

    public void write(final SeekableOutputStream stream)
    {
        int value;

        switch (this.type)
        {
            case TYPE_FIXEDMONEY:
                value = 0xde;
                break;

            case TYPE_RANDOMMONEY:
                value = 0x5e;
                break;

            default:
                value = this.value | (this.type == TYPE_ITEM ? 128 : 0);
        }

        stream.write(value);
        if (this.type == TYPE_FIXEDMONEY || this.type == TYPE_RANDOMMONEY)
        {
            stream.writeWord(this.quantity);
        }
        else
        {
            stream.write(this.quantity);
        }
    }


    /**
     * Returns the item data as XML.
     *
     * @return The item data as XML
     */

    public Element toXml()
    {
        Element element;

        element = XmlUtils.createElement(getXmlName(this.type));
        if (this.value != 0x5e)
        {
            element.addAttribute("value", Integer.toString(this.value));
        }
        if (this.quantity != 1 || this.type == TYPE_RANDOMMONEY
            || this.type == TYPE_FIXEDMONEY)
        {
            element.addAttribute("quantity", Integer.toString(this.quantity));
        }
        return element;
    }


    /**
     * Creates and returns a new LootItem object by reading its data from XML.
     *
     * @param element
     *            The XML element
     * @return The loot item data
     */

    public static LootItem read(final Element element)
    {
        int type, quantity, value;

        type = getType(element.getName());
        if (type == -1)
        {
            throw new GameException("Unknown item type: " + element.getName());
        }
        if (type != TYPE_FIXEDMONEY && type != TYPE_RANDOMMONEY)
        {
            value = StringUtils.toInt(element.attributeValue("value"));
        }
        else
        {
            value = 0x5e;
        }
        quantity = StringUtils.toInt(element.attributeValue("quantity", "1"));
        return new LootItem(type, value, quantity);
    }


    /**
     * Returns the type for the specified xml name. Returns -1 if no type was
     * found.
     *
     * @param xmlName
     *            The XML name
     * @return The type
     */

    private static int getType(final String xmlName)
    {
        for (int i = 0; i < xmlNames.length; i++)
        {
            if (xmlNames[i].equals(xmlName))
            {
                return i;
            }
        }
        return -1;
    }


    /**
     * Returns the XML name for the specified item type.
     *
     * @param type
     *            The check type
     * @return The XML name
     */

    private static String getXmlName(final int type)
    {
        return xmlNames[type];
    }
}
