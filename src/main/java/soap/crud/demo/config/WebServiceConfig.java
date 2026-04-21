package soap.crud.demo.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

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
                new ClassPathResource("xsd/weather.xsd")
        );
    }
}