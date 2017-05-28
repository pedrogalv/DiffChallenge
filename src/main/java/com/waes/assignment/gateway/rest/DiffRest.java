package com.waes.assignment.gateway.rest;

import com.waes.assignment.domain.DiffRequest;
import com.waes.assignment.domain.DiffResponse;
import com.waes.assignment.domain.DiffSide;
import com.waes.assignment.usecase.ObtainDiff;
import com.waes.assignment.usecase.SaveDiff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Pedro on 24/05/2017.
 *
 * Rest controller responsible fir handling api calls. The processing of the calls are responsibility of the use cases
 */
@RestController
@RequestMapping("/v1/diff/{ID}")
public class DiffRest {


    private final SaveDiff saveDiff;
    private final ObtainDiff obtainDiff;

    @Autowired
    public DiffRest(SaveDiff saveDiff, ObtainDiff obtainDiff) {
        this.saveDiff = saveDiff;
        this.obtainDiff = obtainDiff;
    }

    @RequestMapping(method = RequestMethod.GET)
    public DiffResponse getDiff(@PathVariable("ID") Long id) {
        return obtainDiff.execute(id);
    }

    @RequestMapping(path = "/left",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public void addLeftDiff(@PathVariable("ID") Long id, @Valid @RequestBody DiffRequest diffRequest) {
        saveDiff.execute(id, diffRequest, DiffSide.LEFT);
    }

    @RequestMapping(value="/right",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public void addRightDiff(@PathVariable("ID") Long id, @Valid @RequestBody DiffRequest diffRequest) {
        saveDiff.execute(id, diffRequest, DiffSide.RIGHT);
    }

}
