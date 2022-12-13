package me.tiary.domain.composite;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
public class TilTagId implements Serializable {
    private static final long serialVersionUID = 9017788639195921594L;

    @Column(nullable = false)
    private Long tilId;

    @Column(nullable = false)
    private Long tagId;
}