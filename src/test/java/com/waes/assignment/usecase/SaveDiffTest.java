package com.waes.assignment.usecase;

import com.waes.assignment.domain.DiffRequest;
import com.waes.assignment.domain.DiffSide;
import com.waes.assignment.gateway.repository.DiffRepository;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SaveDiffTest {

    @InjectMocks
    private SaveDiff saveDiff;

    @Mock
    private DiffRepository diffRepository;

    @Test
    public void execute() throws Exception {
        //GIVEN: A diff request
        DiffRequest diffRequest = DiffRequest.builder().data(Base64.encodeBase64String("12321312312323".getBytes())).build();

        //WHEN: A request to save it is made
        saveDiff.execute(1L, diffRequest, DiffSide.LEFT);

        //THEN: No errors are returned

    }

}