package co.com.vanegas.microservice.resolveEnigmaApi.api;

import co.com.vanegas.microservice.resolveEnigmaApi.model.*;
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
import java.util.ArrayList;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-02-27T19:20:23.716-05:00[America/Bogota]")
@Controller
public class GetStepApiController implements GetStepApi {

    private static final Logger log = LoggerFactory.getLogger(GetStepApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @EndpointInject(uri = "direct:get-step-one")
    private FluentProducerTemplate producerTemplateResolveEnigma;

    //@org.springframework.beans.factory.annotation.Autowired
    public GetStepApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<JsonApiBodyResponseSuccess> getStepsPost(@ApiParam(value = "body", required = true) @Valid @RequestBody JsonApiBodyRequest body) {
        try {
            producerTemplateResolveEnigma.request();
            return new ResponseEntity<JsonApiBodyResponseSuccess>(objectMapper.readValue("{  \"data\" : [\r\n {\r\n    \"header\" :{ \r\n \"id\":\"id\", \r\n \"type\": \"type\" \r\n}, \r\n \"answer\": \"answer\" \r\n}", JsonApiBodyResponseSuccess.class), HttpStatus.OK);
        } catch (Exception e) {
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



    /*public ResponseEntity<?> getOneEnigmaStep(@ApiParam(value= "body", required = true) @Valid @RequestBody JsonApiBodyRequest body) {
        JsonApiBodyResponseSuccess response = new JsonApiBodyResponseSuccess();
        List<GetEnigmaStepResponse> dataResponse = new ArrayList<GetEnigmaStepResponse>();
        GetEnigmaStepResponse responseAttributes = new GetEnigmaStepResponse();
        responseAttributes.setHeader(body.getData().get(0).getHeader());
        responseAttributes.setStep(body.getData().get(0).getStep());

        if(body.getData().get(0).getStep().equalsIgnoreCase("1")) {
            responseAttributes.setStepDescription("Step 1: Open the refrigerator");
            dataResponse.add(responseAttributes);
            response.setData(dataResponse);
            return new ResponseEntity<JsonApiBodyResponseSuccess>(response, HttpStatus.OK);
        } else if(body.getData().get(0).getStep().equalsIgnoreCase("2")) {
            responseAttributes.setStepDescription("Step 2: Put theh giraffe in");
            dataResponse.add(responseAttributes);
            response.setData(dataResponse);
            return new ResponseEntity<JsonApiBodyResponseSuccess>(response, HttpStatus.OK);
        } else if (body.getData().get(0).getStep().equalsIgnoreCase("3"){
            responseAttributes.setStepDescription("Step 3: Close the refrigerator");
            dataResponse.add(responseAttributes);
            response.setData(dataResponse);
            return new ResponseEntity<JsonApiBodyResponseSuccess>(response, HttpStatus.OK);
        }
        JsonApiBodyResponseErrors responseError = new JsonApiBodyResponseErrors();
        List < ErrorDetail> errorsResponse = new ArrayList<ErrorDetail>();
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode("0001");
        errorDetail.setDetail("Error consulting the step");
        errorDetail.setId(body.getData().get(0).getHeader().getId());
        errorDetail.setSource("/getStep");
        errorDetail.setStatus("424");
        errorDetail.setTitle("Error");
        errorsResponse.add(errorDetail);
        responseError.setErrors(errorsResponse);
        return new ResponseEntity<JsonApiBodyResponseErrors>(responseError, HttpStatus.FAILED_DEPENDENCY);

    }

}

public ResponseEntity<?> getStepsPost(@ApiParam(value = "body" ,required=true )  @Valid @RequestBody JsonApiBodyRequest body) {
       try {

    	   response=producerTemplateResolveEnigma.withBody(body).request();
    	   Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	   System.out.println(timestamp + "Respuesta del Servicio");
    	   return new ResponseEntity<JsonApiBodyResponseSuccess>((JsonApiBodyResponseSuccess)response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Couldn't serialize response for content type application/json", e);
            return new ResponseEntity<JsonApiBodyResponseErrors>((JsonApiBodyResponseErrors)response, HttpStatus.FAILED_DEPENDENCY);
        }
}*/