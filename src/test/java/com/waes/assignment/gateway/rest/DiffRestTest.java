package com.waes.assignment.gateway.rest;

import com.waes.assignment.domain.DiffResponse;
import com.waes.assignment.domain.exceptions.DiffException;
import com.waes.assignment.usecase.ObtainDiff;
import com.waes.assignment.usecase.SaveDiff;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(DiffRest.class)
public class DiffRestTest {

    private static final String API = "/v1/diff/1";
    private static final String LEFT_API = "/v1/diff/1/left";
    private static final String RIGHT_API = "/v1/diff/1/right";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SaveDiff saveDiff;

    @MockBean
    private ObtainDiff obtainDiff;

    @Before
    public void setup(){
        DiffResponse response = DiffResponse.builder()
                .message("SOME MESSAGE")
                .diffs(Collections.singletonList("SOME DIFF"))
                .build();
        Mockito.when(obtainDiff.execute(anyLong())).thenReturn(response);
    }

    @Test
    public void successWhenPostingLeftDiffHavingData() throws Exception {
        //GIVEN: A valid data
        String validData = Base64.encodeBase64String("valid data".getBytes());

        //WHEN: A POST request is made to the left endpoint
        MockHttpServletResponse response = this.mvc.perform(post(LEFT_API).content("{ \"data\": \"" +validData+ "\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //THEN: The result status code is 200
        assertThat(response.getStatus(), equalTo(200));
    }

    @Test
    public void badRequestWhenPostingLeftDiffWithoutData() throws Exception {
        //GIVEN: No data

        //WHEN: A POST request is made to the left endpoint
        MockHttpServletResponse response = this.mvc.perform(post(LEFT_API).content("{ }")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //THEN: The result status code is 400
        assertThat(response.getStatus(), equalTo(400));
        assertThat(response.getContentAsString(), equalTo("{\"message\":\"Your request json is missing the following attributes: data\"}"));
    }

    @Test
    public void successWhenPostingRightDiffHavingData() throws Exception {
        //GIVEN: A valid data
        String validData = Base64.encodeBase64String("valid data".getBytes());

        //WHEN: A POST request is made to the right endpoint
        MockHttpServletResponse response = this.mvc.perform(post(RIGHT_API).content("{ \"data\": \"" +validData+ "\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //THEN: The result status code is 200
        assertThat(response.getStatus(), equalTo(200));
    }

    @Test
    public void badRequestWhenPostingRightDiffWithoutData() throws Exception {
        //GIVEN: No data

        //WHEN: A POST request is made to the right endpoint
        MockHttpServletResponse response = this.mvc.perform(post(RIGHT_API).content("{ }")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //THEN: The result status code is 400
        assertThat(response.getStatus(), equalTo(400));
        assertThat(response.getContentAsString(), equalTo("{\"message\":\"Your request json is missing the following attributes: data\"}"));
    }

    @Test
    public void sucessWhenGetDiffs() throws Exception {
        //GIVEN: Existing diffs saved in database

        //WHEN: A GET request is made to diff endpoint
        MockHttpServletResponse response = this.mvc.perform(get(API)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //THEN: The result status code is 200
        assertThat(response.getStatus(), equalTo(200));
        assertThat(response.getContentAsString(), equalTo("{\"message\":\"SOME MESSAGE\",\"diffs\":[\"SOME DIFF\"]}"));
    }

    @Test
    public void errorWhenGetDiffs() throws Exception {
        //GIVEN: Non existent diffs saved in database
        Mockito.when(obtainDiff.execute(anyLong())).thenThrow(new DiffException("SOME MESSAGE"));

        //WHEN: A GET request is made to diff endpoint
        MockHttpServletResponse response = this.mvc.perform(get(API)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //THEN: The result status code is 416
        assertThat(response.getStatus(), equalTo(412));
        assertThat(response.getContentAsString(), equalTo("{\"message\":\"SOME MESSAGE\"}"));
    }
}
