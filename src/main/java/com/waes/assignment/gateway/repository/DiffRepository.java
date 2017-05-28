package com.waes.assignment.gateway.repository;

import com.waes.assignment.domain.Diff;
import com.waes.assignment.domain.DiffSide;
import org.springframework.data.repository.Repository;

/**
 * Created by Pedro on 24/05/2017.
 *
 * Spring-Data jpa repository
 */
public interface DiffRepository  extends Repository<Diff, Long> {

    Diff save(Diff diff);

    Diff getByIdAndAndDiffSide(Long id, DiffSide diffSide);

}
