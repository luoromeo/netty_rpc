package com.luoromeo.rpc.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 15:10
 * @modified By
 */
public class NettyRpcRegistryParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String id = element.getAttribute("id");
        String ipAddr = element.getAttribute("ipAddr");
        String echoApiPort = element.getAttribute("echoApiPort");
        String protocolType = element.getAttribute("protocol");

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(NettyRpcRegistry.class);
        beanDefinition.getPropertyValues().addPropertyValue("ipAddr", ipAddr);
        beanDefinition.getPropertyValues().addPropertyValue("echoApiPort", echoApiPort);
        beanDefinition.getPropertyValues().addPropertyValue("protocol", protocolType);
        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);

        return beanDefinition;
    }
}
