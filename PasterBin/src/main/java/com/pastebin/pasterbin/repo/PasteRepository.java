package com.pastebin.pasterbin.repo;
import com.pastebin.pasterbin.entity.Paste;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PasteRepository extends JpaRepository<Paste, Long> {
    List<Paste> findByTitle(String title);
    List<Paste> findAllByExpirationTimeBefore(LocalDateTime time);

}
