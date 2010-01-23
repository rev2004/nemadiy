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

package org.meandre.webapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;

import org.imirsel.meandre.client.TransmissionException;
import org.imirsel.nema.client.beans.converters.IBeanConverter;
import org.imirsel.nema.client.beans.repository.WBExecutableComponentDescription;
import org.imirsel.nema.client.beans.repository.WBFlowDescription;
import org.imirsel.nema.client.beans.repository.WBLocation;
import org.imirsel.nema.service.impl.MeandreProxyWrapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.meandre.core.repository.ExecutableComponentDescription;
import org.meandre.core.repository.ExecutableComponentInstanceDescription;
import org.meandre.core.repository.FlowDescription;
import org.meandre.core.repository.LocationBean;
import org.meandre.core.repository.QueryableRepository;
import org.meandre.core.repository.RepositoryImpl;
import org.meandre.core.security.Role;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;

/**
 * @author Amit Kumar
 *
 */
public class Repository {
	
	public MeandreProxyWrapper meandreProxyWrapper;

   
    private static final IBeanConverter<URI, String> UriStringConverter =
        new IBeanConverter<URI, String>() {
            public String convert(URI url) {
                return url.toString();
            }
        };

   

    	public MeandreProxyWrapper getMeandreProxyWrapper() {
    		return meandreProxyWrapper;
    	}

    	public void setMeandreProxyWrapper(MeandreProxyWrapper meandreProxyWrapper) {
    		this.meandreProxyWrapper = meandreProxyWrapper;
    	}





    public Set<String> retrieveComponentUrls()
        throws  MeandreCommunicationException {
    	try {
            Set<URI> componentUrls = getMeandreProxyWrapper().retrieveComponentUris();
            return MeandreConverter.convert(componentUrls, UriStringConverter);
        }catch (TransmissionException e) {
            throw new MeandreCommunicationException(e);
        }
    }

    public WBExecutableComponentDescription retrieveComponentDescriptor(String componentURL)
        throws  MeandreCommunicationException {
        try {
            return MeandreConverter.ExecutableComponentDescriptionConverter.convert(
            		getMeandreProxyWrapper().retrieveComponentDescriptor(componentURL));
        }
        catch (TransmissionException e) {
            throw new MeandreCommunicationException(e);
        }
    }

    public Set<WBExecutableComponentDescription> retrieveComponentDescriptors()
        throws  MeandreCommunicationException {

        try {
            QueryableRepository repository = this.getMeandreProxyWrapper().getRepository();
            return MeandreConverter.convert(
                    repository.getAvailableExecutableComponentDescriptions(),
                    MeandreConverter.ExecutableComponentDescriptionConverter);
        }
        catch (TransmissionException e) {
            throw new MeandreCommunicationException(e);
        }
    }

    public Set<String> retrieveFlowUrls()
        throws  MeandreCommunicationException {

        try {
            return MeandreConverter.convert(getMeandreProxyWrapper().retrieveFlowUris(), UriStringConverter);
        }
        catch (TransmissionException e) {
            throw new MeandreCommunicationException(e);
        }
    }

    public WBFlowDescription retrieveFlowDescriptor(String flowURL)
        throws  MeandreCommunicationException {

        try {
            return MeandreConverter.FlowDescriptionConverter.convert(
            		getMeandreProxyWrapper().retrieveFlowDescriptor(flowURL));
        }
        catch (TransmissionException e) {
            throw new MeandreCommunicationException(e);
        }
    }

    public Set<WBFlowDescription> retrieveFlowDescriptors()
        throws  MeandreCommunicationException {

        try {
            QueryableRepository repository = this.getMeandreProxyWrapper().getRepository();
            return MeandreConverter.convert(
                    repository.getAvailableFlowDescriptions(),
                    MeandreConverter.FlowDescriptionConverter);
        }
        catch (TransmissionException e) {
            throw new MeandreCommunicationException(e);
        }
    }

    public boolean uploadFlow(WBFlowDescription wbFlow, boolean overwrite)
        throws  MeandreCommunicationException, CorruptedFlowException {

        FlowDescription flow = MeandreConverter.WBFlowDescriptionConverter.convert(wbFlow);

        String flowURI = flow.getFlowComponent().getURI();
              System.out.println("Uploading flow " + flowURI);
              System.out.println(System.getProperty("java.io.tmpdir"));


            String execStepMsg = "";
            try {
                Model flowModel = flow.getModel();

                String fName = flowURI.replaceAll(":|/", "_");
                String tempFolder = System.getProperty("java.io.tmpdir");
                if (!(tempFolder.endsWith("/") || tempFolder.endsWith("\\")))
                    tempFolder += System.getProperty("file.separator");
                System.out.println(tempFolder);
                FileOutputStream ntStream = new FileOutputStream(tempFolder + fName + ".nt");
                flowModel.write(ntStream, "N-TRIPLE");
                ntStream.close();

                FileOutputStream ttlStream = new FileOutputStream(tempFolder + fName + ".ttl");
                flowModel.write(ttlStream, "TTL");
                ttlStream.close();
                

                FileOutputStream rdfStream = new FileOutputStream(tempFolder + fName + ".rdf");
                flowModel.write(rdfStream, "RDF/XML-ABBREV");
                rdfStream.close();

                execStepMsg = "STEP1: Creating RepositoryImpl from flow model";
                RepositoryImpl repository = new RepositoryImpl(flowModel);
                execStepMsg = "STEP2: Retrieving available flows";
                Set<FlowDescription> flows = repository.getAvailableFlowDescriptions();
                execStepMsg = "STEP3: Getting flow";
                flow = flows.iterator().next();
                if (flow == null)
                    throw new CorruptedFlowException("The flow obtained is null!");
            }
            catch (Exception e) {

                CorruptedFlowException corruptedFlowException = (execStepMsg != null) ?
                        new CorruptedFlowException(execStepMsg, e) : (CorruptedFlowException) e;

                throw corruptedFlowException;
            }
       
        try {
        	getMeandreProxyWrapper().uploadFlow(flow, overwrite);
            return true;
        }catch (TransmissionException e) {
            throw new MeandreCommunicationException(e);
        }
    }

 
    public boolean removeResource(String resourceURL)
        throws  MeandreCommunicationException {

        try {
            return getMeandreProxyWrapper().removeResource(resourceURL);
        }
        catch (TransmissionException e) {
            throw new MeandreCommunicationException(e);
        }
    }

    
   
  

}
