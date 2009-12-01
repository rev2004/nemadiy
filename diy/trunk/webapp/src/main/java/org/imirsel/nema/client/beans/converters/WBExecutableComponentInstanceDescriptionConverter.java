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

package org.imirsel.nema.client.beans.converters;

import org.meandre.core.repository.ExecutableComponentInstanceDescription;
import org.imirsel.nema.client.beans.repository.WBExecutableComponentInstanceDescription;
import org.meandre.webapp.MeandreConverter;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Converts the workbench WBExecutableComponentInstanceDescription bean to the Meandre equivalent
 *
 * @author Boris Capitanu
 *
 */
public class WBExecutableComponentInstanceDescriptionConverter
        implements
        IBeanConverter<WBExecutableComponentInstanceDescription, ExecutableComponentInstanceDescription> {

    public ExecutableComponentInstanceDescription convert(
            WBExecutableComponentInstanceDescription wbCompInstance) {

        if (wbCompInstance == null) return null;

        Model model = ModelFactory.createDefaultModel();

        ExecutableComponentInstanceDescription compInstance =
            new ExecutableComponentInstanceDescription();
        compInstance.setName(wbCompInstance.getName());
        compInstance.setDescription(wbCompInstance.getDescription());
        compInstance.setExecutableComponent(model.createResource(wbCompInstance.getExecutableComponent()));
        compInstance.setExecutableComponentInstance(model.createResource(wbCompInstance.getExecutableComponentInstance()));
        compInstance.setProperties(MeandreConverter.WBPropertiesDescriptionConverter.convert(wbCompInstance.getProperties()));

        return compInstance;
    }

}
