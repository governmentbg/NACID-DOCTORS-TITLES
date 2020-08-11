package com.nacid.rest.controller;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: Georgi
 * Date: 4.5.2020 г.
 * Time: 22:08
 */
@RestController
@RequestMapping("/nomenclatures")
public class NomenclaturesController extends MainController {

    @RequestMapping("/clear")
    @ResponseBody
    public String clear() {
        NomenclaturesDataProvider nomenclaturesDP = getNacidDataProvider().getNomenclaturesDataProvider();
        nomenclaturesDP.resetAllNomenclatures();
        return "done";
    }
}
