package com.waes.assignment;

import com.waes.assignment.domain.DiffRequest;
import com.waes.assignment.domain.DiffResponse;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiffIntegrationTest {

    private static final String API = "/v1/diff/1";
    private static final String LEFT_API = "/v1/diff/1/left";
    private static final String RIGHT_API = "/v1/diff/1/right";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testEqualDiffData(){
        ResponseEntity responseEntity;

        //GIVEN: A diff request
        DiffRequest request = DiffRequest.builder()
                                .data(Base64.encodeBase64String("1111111111111111111111111".getBytes()))
                                .build();

        //WHEN: This diff is sent to left diff api
        responseEntity = restTemplate.postForEntity(LEFT_API, request, null);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        //AND: This diff is sent to right diff api
        responseEntity = restTemplate.postForEntity(RIGHT_API, request, null);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        //AND: The diff result is requested
        responseEntity = restTemplate.getForEntity(API, DiffResponse.class, Collections.EMPTY_MAP);

        //THEN: The response is OK
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        //AND: It contains a message for equal diff
        assertThat(((DiffResponse)responseEntity.getBody()).getMessage(), equalTo("Left and Right data are equal"));
    }

    @Test
    public void testDifferentDiffData(){
        ResponseEntity responseEntity;

        //GIVEN: Different diff requests
        DiffRequest leftRequest = DiffRequest.builder()
                .data(Base64.encodeBase64String("1111111111111111111111111".getBytes()))
                .build();
        DiffRequest rightRequest = DiffRequest.builder()
                .data(Base64.encodeBase64String("1111111111111111111111112".getBytes()))
                .build();

        //WHEN: The left diff is sent to left diff api
        responseEntity = restTemplate.postForEntity(LEFT_API, leftRequest, null);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        //AND: The right diff is sent to right diff api
        responseEntity = restTemplate.postForEntity(RIGHT_API, rightRequest, null);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        //AND: The diff result is requested
        responseEntity = restTemplate.getForEntity(API, DiffResponse.class, Collections.EMPTY_MAP);

        //THEN: The response is OK
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        DiffResponse diffResponse = (DiffResponse)responseEntity.getBody();
        //AND: It contains a message for not equal diff
        assertThat(diffResponse.getMessage(), equalTo("Left and Right are not equal"));

        //AND: The diff list is not null
        assertThat(diffResponse.getDiffs(), not(empty()));
        assertThat(diffResponse.getDiffs(), hasItem("Offset: 24, Length: 1"));
    }

    @Test
    public void testDifferentDiffDataSize(){
        ResponseEntity responseEntity;

        //GIVEN: Different diff requests
        DiffRequest leftRequest = DiffRequest.builder()
                .data(Base64.encodeBase64String("1111111111111111111111111".getBytes()))
                .build();
        DiffRequest rightRequest = DiffRequest.builder()
                .data(Base64.encodeBase64String("111111111111111111111111".getBytes()))
                .build();

        //WHEN: The left diff is sent to left diff api
        responseEntity = restTemplate.postForEntity(LEFT_API, leftRequest, null);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        //AND: The right diff is sent to right diff api
        responseEntity = restTemplate.postForEntity(RIGHT_API, rightRequest, null);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        //AND: The diff result is requested
        responseEntity = restTemplate.getForEntity(API, DiffResponse.class, Collections.EMPTY_MAP);

        //THEN: The response is OK
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        DiffResponse diffResponse = (DiffResponse)responseEntity.getBody();
        //AND: It contains a message for not equal size diff
        assertThat(diffResponse.getMessage(), equalTo("Left and Right data do not have the same size"));

        //AND: The diff list is null
        assertThat(diffResponse.getDiffs(), nullValue());
    }

    @Test
    public void testDiffWhenSomeSideIsNotSaved(){
        ResponseEntity responseEntity;

        //GIVEN: Different diff requests
        DiffRequest leftRequest = DiffRequest.builder()
                .data(Base64.encodeBase64String("1111111111111111111111111".getBytes()))
                .build();
        DiffRequest rightRequest = DiffRequest.builder()
                .build();

        //WHEN: The left diff is sent to left diff api
        responseEntity = restTemplate.postForEntity(LEFT_API, leftRequest, null);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        //AND: The right diff is sent to right diff api
        responseEntity = restTemplate.postForEntity(RIGHT_API, rightRequest, null);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));

        //AND: The diff result is requested
        responseEntity = restTemplate.getForEntity(API, DiffResponse.class, Collections.EMPTY_MAP);

        //THEN: The response is PRECONDITION_FAILED
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.PRECONDITION_FAILED));

        DiffResponse diffResponse = (DiffResponse)responseEntity.getBody();
        //AND: It contains a message for not equal size diff
        assertThat(diffResponse.getMessage(), equalTo("Error: RIGHT diff was not provided"));

        //AND: The diff list is null
        assertThat(diffResponse.getDiffs(), nullValue());
    }
}
