package by.stepovoy.pmtool.service;

import by.stepovoy.pmtool.domain.Backlog;
import by.stepovoy.pmtool.domain.Project;
import by.stepovoy.pmtool.domain.ProjectTask;
import by.stepovoy.pmtool.exception.ProjectNotFoundException;
import by.stepovoy.pmtool.repository.BacklogRepository;
import by.stepovoy.pmtool.repository.ProjectRepository;
import by.stepovoy.pmtool.repository.ProjectTaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    private final BacklogRepository backlogRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    public ProjectTaskService(BacklogRepository backlogRepository, ProjectTaskRepository projectTaskRepository, ProjectRepository projectRepository, ProjectService projectService) {
        this.backlogRepository = backlogRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectRepository = projectRepository;
        this.projectService = projectService;
    }

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

        //all PTs to be added to a specific project -> project!= null -> BL exists
        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
        //set the BL to PT
        projectTask.setBacklog(backlog);
        //project sequence should be like : IDPRO-1 IDPRO-2
        Integer backlogSequence = backlog.getPTSequence();
        //update the BL sequence
        backlog.setPTSequence(++backlogSequence);
        //Add seq to project task
        projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);
        //initial priority when priority null
        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            projectTask.setPriority(3);
        }
        //initial status when status null
        if (projectTask.getStatus() == null || "".equals(projectTask.getStatus())) {
            projectTask.setStatus("TO-DO");
        }
        return projectTaskRepository.save(projectTask);
    }

    public List<ProjectTask> findBacklogById(String projectIdentifier, String username) {

        Project project = projectService.findProjectByIdentifier(projectIdentifier, username);

        if (project == null) {
            throw new ProjectNotFoundException("Project with ID '" + projectIdentifier + "' doesn't exist");
        }

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier);
    }

    public ProjectTask findPTByProjectSequence(String projectIdentifier, String projectTaskId, String username) {

        //make sure backlog exists
        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();

        //make sure project task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(projectTaskId);
        if (projectTask == null) {
            throw new ProjectNotFoundException("Project task '" + projectTaskId + "' not found");
        }

        //make sure project task id corresponds to right project
        if (!projectIdentifier.equals(projectTask.getProjectIdentifier())) {
            throw new ProjectNotFoundException("Project task '" + projectTaskId + "' doesn't exist in project '" + projectIdentifier + "'");
        }

        return projectTask;
    }


    public ProjectTask updateProjectTaskByProjectSequence(ProjectTask updatedProjectTask, String projectIdentifier, String projectTaskId, String username) {
        ProjectTask projectTask = findPTByProjectSequence(projectIdentifier, projectTaskId, username);
        projectTask = updatedProjectTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deleteProjectTaskByProjectSequence(String projectIdentifier, String projectTaskId, String username) {
        ProjectTask projectTask = findPTByProjectSequence(projectIdentifier, projectTaskId, username);
        projectTaskRepository.delete(projectTask);
    }
}
