package co.com.vanegas.microservice.resolveEnigmaApi.routes;

import co.com.vanegas.microservice.resolveEnigmaApi.model.client.ClientJsonApiBodyResponseSuccess;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class GetStepTwoClientRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:get-step-two")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("freemarker:templates/GetStepTwoClientTemplate.ftl")
                .log("Request: microservice step two ${body}")
                .to("http://localhost:8081/getStep")
                .convertBodyTo(String.class)
                .log("String Response micorservice step two ${body}")
                .unmarshal().json(JsonLibrary.Jackson, ClientJsonApiBodyResponseSuccess.class)
                .log("java Response micorservice step two ${body}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        ClientJsonApiBodyResponseSuccess stepTwoResponse = (ClientJsonApiBodyResponseSuccess) exchange.getIn().getBody();
                        if (stepTwoResponse.getData().get(0).getStep().equalsIgnoreCase("2")) {
                            exchange.setProperty("Step2", stepTwoResponse.getData().get(0).getStepDescription());
                            exchange.setProperty("Error", "0000");
                            exchange.setProperty("ErrorDescription", "No error");
                        } else {
                            exchange.setProperty("Error", "0001");
                            exchange.setProperty("ErrorDescription", "Error consulting the step two");
                        }
                    }
                })
                .log("Response code ${exchageProperty[Error]}");
    }
}
