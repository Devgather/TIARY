package me.tiary.repository;

import me.tiary.domain.TilTag;
import me.tiary.domain.composite.TilTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TilTagRepository extends JpaRepository<TilTag, TilTagId> {
}