package com.nacid.bl.impl.apostille;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.nacid.bl.apostille.ApostilleApplicationsDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplication;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachment;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.apostille.objectss.ApostilleApplication;
import com.nacid.bl.impl.apostille.objectss.ApostilleFile;
import com.nacid.bl.impl.apostille.objectss.RegisterApostilleApplicationResponse;
import com.nacid.bl.impl.applications.regprof.RegprofApplicationAttachmentDataProviderImpl;
import com.nacid.bl.impl.applications.regprof.RegprofApplicationDataProviderImpl;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.db.applications.regprof.RegprofApplicationDB;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.nacid.bl.utils.UtilsDataProvider.APOSTILLE_SERVICE_URL;

/**
 * User: Georgi
 * Date: 1.4.2020 г.
 * Time: 12:28
 */
public class ApostilleApplicationsDataProviderImpl implements ApostilleApplicationsDataProvider {
    private NacidDataProviderImpl nacidDataProvider;
    private RegprofApplicationDataProviderImpl applicationsDataProvider;
    private RegprofApplicationDB applicationsDB;
    private ApostilleService apostilleRestService;

    public ApostilleApplicationsDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.nacidDataProvider = nacidDataProvider;
        this.applicationsDataProvider = nacidDataProvider.getRegprofApplicationDataProvider();
        this.applicationsDB = nacidDataProvider.getRegprofApplicationDataProvider().getDb();
        initApostilleService();

    }

    private void initApostilleService() {
        List<Object> providers = new ArrayList<>();
        providers.add(new JacksonJsonProvider());
        String url = nacidDataProvider.getUtilsDataProvider().getCommonVariableValue(APOSTILLE_SERVICE_URL);
        apostilleRestService = JAXRSClientFactory.create(url, ApostilleService.class, providers);

        ClientConfiguration config = WebClient.getConfig(apostilleRestService);
        config.getInInterceptors().add(new LoggingInInterceptor(-1));
        config.getOutInterceptors().add(new LoggingOutInterceptor(-1));

        config.getHttpConduit().getClient().setAllowChunking(false);
    }

    @Override
    public boolean isApplicationTransferredInApostilleSystem(int applicationId) {
        try {
            return applicationsDB.isApplicationTransferredInApostilleSystem(applicationId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public void registerApplicationInApostilleSystem(int applicationId) {
        try {
            RegprofApplication app = applicationsDataProvider.getRegprofApplication(applicationId);
            String externalSystemId = app.getApplicationDetails().getExternalSystemId();
            RegprofApplicationAttachmentDataProviderImpl attDp = nacidDataProvider.getRegprofApplicationAttachmentDataProvider();
            List<RegprofApplicationAttachment> certs = attDp.getAttachmentsForParentByTypes(applicationId, DocumentType.REGPROF_CERTIFICATES);
            if (CollectionUtils.isEmpty(certs)) {
                throw new RuntimeException("Cannot transfer the app. There should be certificate!");
            }

            //na teoriq edno zaqvlenie trqbva da ima MAX edin certificate, no kym 01.04.2020 v bazata ima 8 zaqvleniq s po 2 udostovereniq. Zatova vzemam tova s naj-golemiq nomer!
            RegprofApplicationAttachment cert = certs.stream().sorted(Comparator.comparing(RegprofApplicationAttachment::getId).reversed()).findFirst().get();
            if (StringUtils.isEmpty(cert.getScannedContentType())) {
                throw new RuntimeException("The certificate should have scanned content!!!!");
            }
            cert = attDp.getAttachment(cert.getId(), true, true);
            ApostilleApplication a = new ApostilleApplication();
            a.setExternalId(externalSystemId);
            a.setCertificate(new ApostilleFile());
            a.getCertificate().setContent(IOUtils.toByteArray(cert.getScannedContentStream()));
            a.getCertificate().setMimeType(cert.getScannedContentType());
            RegisterApostilleApplicationResponse response = apostilleRestService.registerApplication(a);
            if (response.getStatus() != 1) {
                throw new RuntimeException("Cannot send data to apostille system:" + (response.getErrors() == null || response.getErrors().length == 0 ? "" : Arrays.stream(response.getErrors()).collect(Collectors.joining("\n"))));
            }
            applicationsDB.updateApostilleTransferDate(applicationId, new Date());

        } catch (IOException | SQLException e) {
            throw Utils.logException(e);
        }
    }
    public RegisterApostilleApplicationResponse testRegisterAttachment(ApostilleApplication request) {
        return apostilleRestService.testRegisterAttachment(request);
    }
}
