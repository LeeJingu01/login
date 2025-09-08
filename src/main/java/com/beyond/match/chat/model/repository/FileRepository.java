package com.beyond.match.chat.model.repository;

import com.beyond.match.chat.model.vo.DmFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<DmFile,Integer> {
}
