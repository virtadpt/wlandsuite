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

import de.ailis.wlandsuite.utils.StringUtils;
import de.ailis.wlandsuite.utils.XmlUtils;
import org.dom4j.Element;

import de.ailis.wlandsuite.io.SeekableOutputStream;


/**
 * The doctor data used in the Special Building Actions.
 * 
 * @author Klaus Reimer (k@ailis.de)
 * @version $Revision$
 */

public class DoctorAction implements Action
{
    /** The new action class to set when leaving the doctor */
    private int newActionClass;

    /** The new action to set when leaving the doctor */
    private int newAction;

    /** The welcome message */
    private int message;

    /** The price for healing */
    private int healPrice;

    /** The price for examination */
    private int examinePrice;

    /** The price for curing */
    private int curePrice;

    /** The doctor name */
    private String name;


    /**
     * Creates and returns a new Doctor object by reading its data from the
     * specified stream.
     * 
     * @param stream
     *            The input stream
     * @return The Doctor
     * @throws IOException
     */

    public static DoctorAction read(InputStream stream) throws IOException
    {
        DoctorAction doctor;
        byte[] bytes;
        int p;

        doctor = new DoctorAction();

        doctor.newActionClass = stream.read();
        doctor.newAction = stream.read();
        doctor.message = stream.read();
        doctor.healPrice = stream.read();
        doctor.examinePrice = stream.read();
        doctor.curePrice = stream.read();

        // Read the name
        bytes = new byte[13];
        stream.read(bytes);
        p = 0;
        while (bytes[p] != 0 && p < 13)
            p++;
        doctor.name = new String(bytes, 0, p, "ASCII");

        return doctor;
    }


    /**
     * @see de.ailis.wlandsuite.game.parts.Action#write(de.ailis.wlandsuite.io.SeekableOutputStream,
     *      de.ailis.wlandsuite.game.parts.SpecialActionTable)
     */

    public void write(SeekableOutputStream stream,
        SpecialActionTable specialActionTable) throws IOException
    {
        stream.write(0x80);

        stream.write(this.newActionClass);
        stream.write(this.newAction);
        stream.write(this.message);
        stream.write(this.healPrice);
        stream.write(this.examinePrice);
        stream.write(this.curePrice);

        stream.write(this.name.getBytes("ASCII"), 0, Math.min(this.name
            .length(), 13));
        for (int i = this.name.length(); i < 13; i++)
        {
            stream.write(0);
        }
    }


    /**
     * Returns the doctor data as XML.
     * 
     * @param id
     *            The action id
     * @return The doctor data as XML
     */

    public Element toXml(int id)
    {
        Element element;

        element = XmlUtils.createElement("doctor");

        element.addAttribute("id", StringUtils.toHex(id));
        if (this.name != null && this.name.length() > 0)
        {
            element.addAttribute("name", this.name);
        }
        if (this.message != 0)
        {
            element.addAttribute("message", Integer.toString(this.message));
        }
        if (this.healPrice != 10)
        {
            element.addAttribute("healPrice", Integer.toString(this.healPrice));
        }
        if (this.examinePrice != 100)
        {
            element.addAttribute("examinePrice", Integer
                .toString(this.examinePrice));
        }
        if (this.curePrice != 150)
        {
            element.addAttribute("curePrice", Integer.toString(this.curePrice));
        }
        if (this.newActionClass != 255)
        {
            element.addAttribute("newActionClass", StringUtils
                .toHex(this.newActionClass));
        }
        if (this.newAction != 255)
        {
            element
                .addAttribute("newAction", StringUtils.toHex(this.newAction));
        }
        return element;
    }


    /**
     * Creates and returns a new Doctor object by reading its data from XML.
     * 
     * @param element
     *            The XML element
     * @return The doctor data
     */

    public static DoctorAction read(Element element)
    {
        DoctorAction doctor;

        doctor = new DoctorAction();
        doctor.name = element.attributeValue("name", "");
        doctor.message = StringUtils.toInt(element.attributeValue("message",
            "0"));
        doctor.healPrice = StringUtils.toInt(element.attributeValue("healPrice",
            "10"));
        doctor.examinePrice = StringUtils.toInt(element.attributeValue(
            "examinePrice", "100"));
        doctor.curePrice = StringUtils.toInt(element.attributeValue(
            "curePrice", "150"));
        doctor.newActionClass = StringUtils.toInt(element.attributeValue(
            "newActionClass", "255"));
        doctor.newAction = StringUtils.toInt(element.attributeValue(
            "newAction", "255"));

        return doctor;
    }


    /**
     * Returns the curePrice.
     * 
     * @return The curePrice
     */

    public int getCurePrice()
    {
        return this.curePrice;
    }


    /**
     * Sets the curePrice.
     * 
     * @param curePrice
     *            The curePrice to set
     */

    public void setCurePrice(int curePrice)
    {
        this.curePrice = curePrice;
    }


    /**
     * Returns the examinePrice.
     * 
     * @return The examinePrice
     */

    public int getExaminePrice()
    {
        return this.examinePrice;
    }


    /**
     * Sets the examinePrice.
     * 
     * @param examinePrice
     *            The examinePrice to set
     */

    public void setExaminePrice(int examinePrice)
    {
        this.examinePrice = examinePrice;
    }


    /**
     * Returns the healPrice.
     * 
     * @return The healPrice
     */

    public int getHealPrice()
    {
        return this.healPrice;
    }


    /**
     * Sets the healPrice.
     * 
     * @param healPrice
     *            The healPrice to set
     */

    public void setHealPrice(int healPrice)
    {
        this.healPrice = healPrice;
    }


    /**
     * Returns the message.
     * 
     * @return The message
     */

    public int getMessage()
    {
        return this.message;
    }


    /**
     * Sets the message.
     * 
     * @param message
     *            The message to set
     */

    public void setMessage(int message)
    {
        this.message = message;
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

    public void setName(String name)
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

    public void setNewAction(int newAction)
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

    public void setNewActionClass(int newActionClass)
    {
        this.newActionClass = newActionClass;
    }
}
