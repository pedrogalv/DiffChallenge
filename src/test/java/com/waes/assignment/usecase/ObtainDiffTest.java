package com.waes.assignment.usecase;

import com.waes.assignment.domain.Diff;
import com.waes.assignment.domain.DiffResponse;
import com.waes.assignment.domain.DiffSide;
import com.waes.assignment.domain.exceptions.DiffException;
import com.waes.assignment.gateway.repository.DiffRepository;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static com.waes.assignment.domain.DiffSide.LEFT;
import static com.waes.assignment.domain.DiffSide.RIGHT;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ObtainDiffTest {

    @InjectMocks
    private ObtainDiff obtainDiff;

    @Mock
    private DiffRepository diffRepository;

    private Diff leftDiff;
    private Diff rightDiff;

    @Before
    public void setup(){
        leftDiff = Diff.builder()
                .id(1L)
                .diffSide(LEFT)
                .build();

        rightDiff = Diff.builder()
                .id(1L)
                .diffSide(RIGHT)
                .build();

        Mockito.when(diffRepository.getByIdAndAndDiffSide(Matchers.anyLong(), Matchers.eq(DiffSide.LEFT))).thenReturn(leftDiff);
        Mockito.when(diffRepository.getByIdAndAndDiffSide(Matchers.anyLong(), Matchers.eq(DiffSide.RIGHT))).thenReturn(rightDiff);
    }

    @Test
    public void responseNotEqualsWhenDifferentData(){
        //GIVEN: Different left and right data
        leftDiff.setData(Base64.encodeBase64String("1111111111111".getBytes()));
        rightDiff.setData(Base64.encodeBase64String("1111122211331".getBytes()));

        //WHEN: The difference is requested
        DiffResponse response = obtainDiff.execute(1L);

        //THEN: A response having a message for not equals is returned, with a diff list
        assertThat(response.getMessage(), equalTo("Left and Right are not equal"));
        assertThat(response.getDiffs(), hasItem("Offset: 5, Length: 3"));
        assertThat(response.getDiffs(), hasItem("Offset: 10, Length: 2"));
    }

    @Test
    public void responseEqualsForSameData(){
        //GIVEN: Same left and right data
        leftDiff.setData(Base64.encodeBase64String("1111111111111".getBytes()));
        rightDiff.setData(Base64.encodeBase64String("1111111111111".getBytes()));

        //WHEN: The difference is requested
        DiffResponse response = obtainDiff.execute(1L);

        //THEN: A response having a message for equal is returned, with no list
        assertThat(response.getMessage(), equalTo("Left and Right data are equal"));
        assertThat(response.getDiffs(), nullValue());
    }

    @Test
    public void responseNotSameSize(){
        //GIVEN: Different left and right data sizes
        leftDiff.setData(Base64.encodeBase64String("1111111111111".getBytes()));
        rightDiff.setData(Base64.encodeBase64String("11111222".getBytes()));

        //WHEN: The difference is requested
        DiffResponse response = obtainDiff.execute(1L);

        //THEN: A response having a message for different size is returned, with no list
        assertThat(response.getMessage(), equalTo("Left and Right data do not have the same size"));
        assertThat(response.getDiffs(), nullValue());
    }

    @Test(expected = DiffException.class)
    public void exceptionThrownWhenNoDataLeft(){
        //GIVEN: No left data
        Mockito.when(diffRepository.getByIdAndAndDiffSide(Matchers.anyLong(), Matchers.eq(DiffSide.LEFT))).thenReturn(null);

        //WHEN: The difference is requested, an exception is expected
        obtainDiff.execute(1L);
    }

    @Test(expected = DiffException.class)
    public void exceptionThrownWhenNoDataRight(){
        //GIVEN: No right data
        Mockito.when(diffRepository.getByIdAndAndDiffSide(Matchers.anyLong(), Matchers.eq(DiffSide.RIGHT))).thenReturn(null);

        //WHEN: The difference is requested, an exception is expected
        obtainDiff.execute(1L);
    }

}