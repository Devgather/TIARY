package me.tiary.domain.composite;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class TilTagId implements Serializable {
    private static final long serialVersionUID = 9017788639195921594L;

    @Column
    private Long tilId;

    @Column
    private Long tagId;
}