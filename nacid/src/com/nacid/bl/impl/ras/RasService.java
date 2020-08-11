package com.nacid.bl.impl.ras;

import com.nacid.bl.impl.ras.objects.CheckUinResponse;
import com.nacid.bl.impl.ras.objects.GetNomenclaturesResult;
import com.nacid.bl.impl.ras.objects.RegisterApplicationRequest;
import com.nacid.bl.impl.ras.objects.RegisterApplicationResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * User: ggeorgiev
 * Date: 23.10.2019 г.
 * Time: 16:51
 */
public interface RasService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/Public/CheckUin/{uin}")
    public CheckUinResponse checkUin(@PathParam("uin") String uin);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/Nomenclatures/Person/ResearchArea")
    public GetNomenclaturesResult getResearchAreas(@QueryParam("limit")int limit);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/Nomenclatures/Country")
    public GetNomenclaturesResult getCountries(@QueryParam("limit")int limit);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/Nomenclatures/Language")
    public GetNomenclaturesResult getLanguages(@QueryParam("limit")int limit);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/Public/Application")
    public RegisterApplicationResponse registerApplication(RegisterApplicationRequest request);
}
