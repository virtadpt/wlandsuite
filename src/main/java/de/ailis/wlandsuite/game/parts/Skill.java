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
import java.io.OutputStream;

import org.dom4j.Element;

import de.ailis.wlandsuite.utils.StringUtils;
import de.ailis.wlandsuite.utils.XmlUtils;


/**
 * A skill as used by the Char class.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @version $Revision$
 */

public class Skill
{
    /** The skill id */
    private int id;

    /** The skill level */
    private int level;

    /** If this skill is a special skill */
    private boolean special;


    /**
     * Creates and returns a new skill by reading its data from the specified
     * stream.
     *
     * @param stream
     *            The input stream
     * @return The skill
     * @throws IOException
     *             When file operation fails.
     */

    public static Skill read(final InputStream stream) throws IOException
    {
        Skill skill;

        skill = new Skill();

        skill.id = stream.read();
        skill.level = stream.read();

        return skill;
    }


    /**
     * Writes the skill to the specified stream.
     *
     * @param stream
     *            The output stream
     * @throws IOException
     *             When file operation fails.
     */

    public void write(final OutputStream stream) throws IOException
    {
        stream.write(this.id);
        stream.write(this.level);
    }


    /**
     * Returns the skill data as XML.
     *
     * @return The skill data as XML
     */

    public Element toXml()
    {
        Element element;

        element = XmlUtils.createElement("skill");
        element.addAttribute("id", Integer.toString(this.id));
        element.addAttribute("level", Integer.toString(this.level));
        if (this.special)
        {
            element.addAttribute("special", "true");
        }

        return element;
    }


    /**
     * Creates and returns a new skill object by reading its data from XML.
     *
     * @param element
     *            The XML element
     * @return The skill data
     */

    public static Skill read(final Element element)
    {
        Skill skill;

        skill = new Skill();

        skill.id = StringUtils.toInt(element.attributeValue("id"));
        skill.level = StringUtils.toInt(element.attributeValue("level"));
        skill.special = Boolean.parseBoolean(element.attributeValue("special",
            "false"));

        return skill;
    }


    /**
     * Returns the skill.
     *
     * @return The skill
     */

    public int getId()
    {
        return this.id;
    }


    /**
     * Sets the skill.
     *
     * @param skill
     *            The skill to set
     */

    public void setId(final int skill)
    {
        this.id = skill;
    }


    /**
     * Returns the level.
     *
     * @return The level
     */

    public int getLevel()
    {
        return this.level;
    }


    /**
     * Sets the level.
     *
     * @param level
     *            The level to set
     */

    public void setLevel(final int level)
    {
        this.level = level;
    }


    /**
     * Returns the special.
     *
     * @return The special
     */

    public boolean isSpecial()
    {
        return this.special;
    }


    /**
     * Sets the special.
     *
     * @param special
     *            The special to set
     */

    public void setSpecial(final boolean special)
    {
        this.special = special;
    }
}
