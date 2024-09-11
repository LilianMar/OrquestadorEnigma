package co.com.vanegas.microservice.resolveEnigmaApi.api;

import co.com.vanegas.microservice.resolveEnigmaApi.model.GetEnigmaStepResponse;
import co.com.vanegas.microservice.resolveEnigmaApi.model.JsonApiBodyRequest;
import co.com.vanegas.microservice.resolveEnigmaApi.model.JsonApiBodyResponseSuccess;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.apache.camel.EndpointInject;
import org.apache.camel.FluentProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-02-27T19:20:23.716-05:00[America/Bogota]")
@Controller
public class GetStepApiController implements GetStepApi {

    private static final Logger log = LoggerFactory.getLogger(GetStepApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @EndpointInject(uri = "direct:get-step-one")
    private FluentProducerTemplate producerTemplateResolveEnigma;

   public GetStepApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<JsonApiBodyResponseSuccess> getStepsPost(@ApiParam(value = "body" ,required=true )  @Valid @RequestBody JsonApiBodyRequest body) {
        try{
            producerTemplateResolveEnigma.request();
            return new ResponseEntity<JsonApiBodyResponseSuccess>(objectMapper.readValue("{  \"data\" : [\r\n {\r\n    \"header\" :{ \r\n \"id\":\"id\", \r\n \"type\": \"type\" \r\n}, \r\n \"answer\": \"answer\" \r\n}" , JsonApiBodyResponseSuccess.class), HttpStatus.OK);
        }catch (Exception e){
            log.error("Couldn't serialize response for content type application/json", e);
            return new ResponseEntity<JsonApiBodyResponseSuccess>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<List<JsonApiBodyResponseSuccess>> getStep(@ApiParam(value = "request body get enigma step" ,required=true )  @Valid @RequestBody JsonApiBodyRequest body) {
        String accept = request.getHeader("Accept");

        GetEnigmaStepResponse getEnigmaStepResponse = new GetEnigmaStepResponse();
        getEnigmaStepResponse.answer("Abrir la nevera");

        getEnigmaStepResponse.header(body.getData().get(0).getHeader());

        JsonApiBodyResponseSuccess responseSuccess = new JsonApiBodyResponseSuccess();
        responseSuccess.addDataItem(getEnigmaStepResponse);

        List<JsonApiBodyResponseSuccess> response = List.of(responseSuccess);
        return new ResponseEntity<List<JsonApiBodyResponseSuccess>>(response, HttpStatus.OK);
    }

}