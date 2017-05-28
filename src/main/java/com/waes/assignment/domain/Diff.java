package com.waes.assignment.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Pedro on 24/05/2017.
 *
 * Entity representation of diff request savad in database.
 *
 * It contains a composed index of id and diffSide, making possible to save two diffs with the same id provided in request
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@IdClass(DiffKey.class)
public class Diff implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Id
    private DiffSide diffSide;

    @Column(nullable = false)
    private String data;

}

/**
 * Composed Index handle inner class
 */
class DiffKey implements Serializable{

    private Long id;
    private DiffSide diffSide;
}
