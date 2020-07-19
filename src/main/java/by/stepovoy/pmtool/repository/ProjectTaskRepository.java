package by.stepovoy.pmtool.repository;

import by.stepovoy.pmtool.domain.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {

    List<ProjectTask> findByProjectIdentifierOrderByPriority(String backlogId);

    ProjectTask findByProjectSequence(String sequence);
}
