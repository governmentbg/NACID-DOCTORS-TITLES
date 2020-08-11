package com.nacid.webspring;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;

public class RequestParameterInterfaceScopeResolver implements ScopeMetadataResolver {

    Logger logger = Logger.getLogger(RequestParameterInterfaceScopeResolver.class);
    public ScopeMetadata resolveScopeMetadata(BeanDefinition beanDefinition) {
        logger.debug("Setting scope of " + beanDefinition.getBeanClassName() +  " to  prototype. Previous scope: " + beanDefinition.getScope() );
        ScopeMetadata sm = new ScopeMetadata();
        sm.setScopeName("request");
        return sm;
//        return null;
    }

}
