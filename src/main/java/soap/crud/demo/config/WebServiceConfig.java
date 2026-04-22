package soap.crud.demo.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.server.endpoint.adapter.method.MarshallingPayloadMethodProcessor;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPException;

@EnableWs
@Configuration
public class WebServiceConfig {

    // 📌 register servlet cho SOAP
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext context) {

        MessageDispatcherServlet servlet = new MessageDispatcherServlet();

        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);

        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean
    public SaajSoapMessageFactory messageFactory() throws SOAPException {

        MessageFactory saaj = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);

        return new SaajSoapMessageFactory(saaj);
    }

    // 📌 expose WSDL
    @Bean(name = "weather")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema weatherSchema) {

        DefaultWsdl11Definition wsdl = new DefaultWsdl11Definition();
        wsdl.setPortTypeName("WeatherPort");
        wsdl.setLocationUri("/ws");
        wsdl.setTargetNamespace("http://soap.demo.com/weather");
        wsdl.setSchema(weatherSchema);

        return wsdl;
    }

    // 📌 load XSD
    @Bean
    public XsdSchema weatherSchema() {
        return new SimpleXsdSchema(
                new ClassPathResource("xsd/weather.xsd"));
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.demo.soap.weather"); // package generated
        marshaller.setMtomEnabled(true);

        return marshaller;
    }

    @Bean
    public MarshallingPayloadMethodProcessor methodProcessor() {
        return new MarshallingPayloadMethodProcessor(marshaller());
    }

    @Bean
    public org.springframework.ws.server.endpoint.adapter.MessageEndpointAdapter messageEndpointAdapter(
            Jaxb2Marshaller marshaller) {

        return new org.springframework.ws.server.endpoint.adapter.MessageEndpointAdapter();
    }
}