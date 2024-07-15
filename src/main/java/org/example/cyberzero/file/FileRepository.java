package org.example.cyberzero.file;

import org.example.cyberzero.file.entity.MyFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<MyFile, UUID> {
}
