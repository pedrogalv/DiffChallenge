package com.waes.assignment.domain;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

/**
 * Created by Pedro on 25/05/2017.
 *
 * Representative class for diff request
 */

@Getter
@Builder
public class DiffRequest {

    @NotNull
    private String data;
}
