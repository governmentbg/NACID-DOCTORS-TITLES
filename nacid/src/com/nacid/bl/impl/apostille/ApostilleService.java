package com.nacid.bl.impl.apostille;

import com.nacid.bl.impl.apostille.objectss.ApostilleApplication;
import com.nacid.bl.impl.apostille.objectss.RegisterApostilleApplicationResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface ApostilleService {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/Public/AddCertificate")
    public RegisterApostilleApplicationResponse registerApplication(ApostilleApplication request);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/Public/AddCertificate/Test")
    public RegisterApostilleApplicationResponse testRegisterAttachment(ApostilleApplication request);
}
