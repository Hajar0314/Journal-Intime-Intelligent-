package com.journalintime.repository;

import com.journalintime.model.NoteTag;
import java.util.Optional;

public interface NoteTagRepository extends Repository<NoteTag, Long> {
    Optional<NoteTag> findByName(String name);
}
