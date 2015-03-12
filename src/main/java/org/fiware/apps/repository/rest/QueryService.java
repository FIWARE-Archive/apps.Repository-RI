/*
Modified BSD License
====================

Copyright (c) 2015, CoNWeTLab, Universidad Politecnica Madrid
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
* Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
* Neither the name of the SAP AG nor the
names of its contributors may be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL SAP AG BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.fiware.apps.repository.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.fiware.apps.repository.dao.VirtModelFactory;
import org.fiware.apps.repository.dao.VirtuosoQueryExecutionFactory;



import org.fiware.apps.repository.dao.impl.VirtuosoResourceDAO;
import org.fiware.apps.repository.settings.RepositorySettings;
import virtuoso.jena.driver.VirtGraph;

@Path("/services/query")
public class QueryService {
    VirtGraph virtGraph = new VirtGraph (RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT, 
                            RepositorySettings.VIRTUOSO_USER, RepositorySettings.VIRTUOSO_PASSWORD);
    private VirtuosoResourceDAO virtuosoResourceDAO = new VirtuosoResourceDAO(new VirtModelFactory(virtGraph),
            virtGraph, new VirtuosoQueryExecutionFactory());
    
    @Context
            UriInfo uriInfo;
    
    @GET
    public Response executeQuery(@HeaderParam("Accept") String accept, @QueryParam("query") String query) {
        return executeAnyQuery(query, accept);
    }
    
    @POST
    @Consumes("text/plain")
    public Response executeLongQuery(@HeaderParam("Accept") String accept, String content) {
        return executeAnyQuery(content, accept);
    }
    
    private Response executeAnyQuery(String query, String type) {
        String result = "";
        // Check type sparql query and execute the method
        try {
            if (query.toLowerCase().contains("select")) {
                return Response.status(Status.OK).entity(virtuosoResourceDAO.executeQuerySelect(query)).build();
            }
            if (query.toLowerCase().contains("construct")) {
                result = virtuosoResourceDAO.executeQueryConstruct(query, RestHelper.typeMap.get(type));
            }
            if (query.toLowerCase().contains("describe")) {
                result = virtuosoResourceDAO.executeQueryDescribe(query, RestHelper.typeMap.get(type));
            }
            if (query.toLowerCase().contains("ask")) {
                result = (virtuosoResourceDAO.executeQueryAsk(query)) ? "true" : "false";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Response.status(Status.OK).entity(result).build();
    }
}
