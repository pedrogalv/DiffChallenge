package com.waes.assignment.usecase;

import com.waes.assignment.domain.Diff;
import com.waes.assignment.domain.DiffRequest;
import com.waes.assignment.domain.DiffSide;
import com.waes.assignment.gateway.repository.DiffRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Pedro on 25/05/2017.
 *
 * UseCase responsible for saving the diff request received
 */
@Slf4j
@Component
public class SaveDiff {

    private DiffRepository diffRepository;

    @Autowired
    public SaveDiff(DiffRepository diffRepository) {
        this.diffRepository = diffRepository;
    }

    public void execute(Long id, DiffRequest request, DiffSide diffSide){
        log.info("Saving {} diff with ID {}", diffSide.name(), id);
        diffRepository.save(new Diff().builder()
                .id(id)
                .diffSide(diffSide)
                .data(request.getData())
                .build());
    }
}
