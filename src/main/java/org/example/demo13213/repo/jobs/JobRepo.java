package org.example.demo13213.repo.jobs;

import org.example.demo13213.model.dao.Jobs;
import org.example.demo13213.model.dao.Users;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepo extends JpaRepository<Jobs, Long> {
    @Query("SELECT j FROM Jobs j WHERE j.id = :id ")
    Optional<Jobs> findJobsById(@Param("id") Long id);
}
