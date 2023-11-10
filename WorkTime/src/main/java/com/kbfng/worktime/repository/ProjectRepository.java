package com.kbfng.worktime.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kbfng.worktime.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer>, ProjectRepositoryCustom {

	Page<Project> findAll(Pageable pageable);
	
	Optional<Project> findByPrjId(int prjId);
}
