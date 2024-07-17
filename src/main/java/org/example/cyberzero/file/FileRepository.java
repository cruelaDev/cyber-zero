package org.example.cyberzero.file;

import org.example.cyberzero.file.entity.MyFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<MyFile, UUID> {
    @Transactional
    @Modifying
    @Query("DELETE FROM MyFile f WHERE f.generatedName = :filename")
    void deleteByFilename(String filename);
}
