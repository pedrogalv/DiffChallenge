package com.waes.assignment.domain;

import lombok.*;

import java.util.List;

/**
 * Created by Pedro on 26/05/2017.
 *
 * Representative class for diff response
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class DiffResponse {

    private String message;
    private List<String> diffs;

}
