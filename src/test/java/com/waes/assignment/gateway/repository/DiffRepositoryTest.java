package com.waes.assignment.gateway.repository;

import com.waes.assignment.domain.Diff;
import com.waes.assignment.domain.DiffSide;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DiffRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DiffRepository repository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void save() throws Exception {
        //GIVEN: A diff
        Diff diff = Diff.builder()
                .id(1L)
                .data("SOME DATA")
                .diffSide(DiffSide.RIGHT)
                .build();

        //WHEN: This diff is saved
        repository.save(diff);

        //THEN: This diff is available to queries
        Diff savedDiff = repository.getByIdAndAndDiffSide(1L, DiffSide.RIGHT);
        assertThat(savedDiff.getData(), equalTo("SOME DATA"));
        assertThat(savedDiff.getDiffSide(), equalTo(DiffSide.RIGHT));
    }

    @Test
    public void getByIdAndAndDiffSide() throws Exception {
        //GIVEN: A diff
        Diff diff = Diff.builder()
                .id(1L)
                .data("SOME DATA")
                .diffSide(DiffSide.LEFT)
                .build();

        //AND: It exists in database
        entityManager.persistAndFlush(diff);

        //WHEN: This a query for fetching this diff is made
        Diff savedDiff = repository.getByIdAndAndDiffSide(1L, DiffSide.LEFT);

        //THEN: This diff is returned with correct values
        assertThat(savedDiff.getData(), equalTo("SOME DATA"));
        assertThat(savedDiff.getDiffSide(), equalTo(DiffSide.LEFT));
    }

}