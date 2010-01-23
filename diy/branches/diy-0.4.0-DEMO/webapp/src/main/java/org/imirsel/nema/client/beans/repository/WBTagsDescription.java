/**
 * University of Illinois/NCSA
 * Open Source License
 *
 * Copyright (c) 2008, Board of Trustees-University of Illinois.
 * All rights reserved.
 *
 * Developed by:
 *
 * Automated Learning Group
 * National Center for Supercomputing Applications
 * http://www.seasr.org
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal with the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimers.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimers in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the names of Automated Learning Group, The National Center for
 *    Supercomputing Applications, or University of Illinois, nor the names of
 *    its contributors may be used to endorse or promote products derived from
 *    this Software without specific prior written permission.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * WITH THE SOFTWARE.
 */

package org.imirsel.nema.client.beans.repository;

import java.util.HashSet;
import java.util.Set;


public class WBTagsDescription implements  Cloneable {

    /** The set of tags linked to a component */
    private Set<String> setTags = null;

    /** Creates an empty tag description object.
     *
     */
    public WBTagsDescription ( ) {
        this(new HashSet<String>());
    }

    /** Creates a tag description object.
     *
     * @param setTags The set of tags
     */
    public WBTagsDescription ( Set<String> setTags ) {
        this.setTags = setTags;
    }


    /** Returns the set of tags.
     *
     * @return The set of tags
     */
    public Set<String> getTags () {
        return setTags;
    }

    /** Returns a string-fied version of the tags.
     *
     * @return The tag list
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        for ( String sTag:setTags )
            sb.append(", " + sTag);

        String result = sb.toString();
        return (result.startsWith(", ")) ? result.substring(2).trim() : "";
    }

    /**
     * Returns a deep copy of this object
     */
    public WBTagsDescription clone() {
        return
            new WBTagsDescription(new HashSet<String>(this.setTags));
    }
}
