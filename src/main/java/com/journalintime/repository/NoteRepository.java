package com.journalintime.repository;

import com.journalintime.model.Note;
import com.journalintime.model.User;
import java.util.List;

public interface NoteRepository extends Repository<Note, Long> {
    List<Note> findByUser(User user);
}
