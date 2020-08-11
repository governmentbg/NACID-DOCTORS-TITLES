package com.nacid.rest.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@RestController
@RequestMapping("/blob")
public class BlobController extends MainController {

    @RequestMapping("/content/{id}")
    public String getBlobContent(@PathVariable("id") int id) throws IOException {
        InputStream c = getNacidDataProvider().getApplicationAttachmentDataProvider().getBlobContent(id);
        byte[] content = IOUtils.toByteArray(c);
        return Base64.getEncoder().encodeToString(content);

    }
}
