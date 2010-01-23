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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WBPropertiesDescription implements Cloneable {

    /** The property key. */
    private Map<String, String> htValues = null;

    /** Create an empty property description.
     *
     * @param htValues The values
     */
    public WBPropertiesDescription () {
        this(new HashMap<String,String>());
    }

    /** Create a property description.
     *
     * @param htValues The values
     */
    public WBPropertiesDescription ( Map<String, String> htValues ) {
        this.htValues = htValues;
    }

    /** Returns the keys of the properties.
     *
     * @return The keys
     */
    public Set<String> getKeys () {
        return htValues.keySet();
    }

    /** Returns the values of the property.
     *
     * @return The values
     */
    public Collection<String> getValues () {
        return htValues.values();
    }

    /** Get the value for a given property value.
     *
     * @param sKey The key of the property to retrieve
     * @return The value
     */
    public String getValue ( String sKey ) {
        return htValues.get(sKey);
    }

    /** Gets a map for the property values.
     *
     * @return The value map
     */
    public Map<String,String> getValueMap () {
        return htValues;
    }

    /** Add a property to the properties.
     *
     * @param sKey The key
     * @param sValue The value
     */
    public void add ( String sKey, String sValue ) {
        htValues.put(sKey, sValue);
    }


    /** Remove a property from the properties.
     *
     * @param sKey The key
     */
    public void remove ( String sKey ) {
        htValues.remove(sKey);
    }

    /**
     * Returns a deep copy of this object
     */
    public WBPropertiesDescription clone() {
        return new WBPropertiesDescription(new HashMap<String, String>(this.htValues));
    }
}
