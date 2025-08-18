package com.microservice_project.repository;

import com.microservice_project.dto.ProjectDTO;
import com.microservice_project.entity.Project;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

  @Modifying
  @Transactional
  @Query(value = "UPDATE Project p SET " +
      "title = COALESCE(:title, title), " +
      "description = COALESCE(:description, description), " +
      "image = COALESCE(:image, image) " +
      "WHERE id_project = :id_project", nativeQuery = true)
  void updateProject(@Param("id_project") Long id_project,
                     @Param("title") String title,
                     @Param("description") String description,
                     @Param("image") String image);

  @Query("SELECT p FROM Project p WHERE p.available = true")
  List<Project> getAvailableProjects();

  @Query("SELECT p FROM Project p WHERE p.available = true AND p.id_project = :id_project")
  Project findAvailableById(@Param("id_project") Long id_project);
}
