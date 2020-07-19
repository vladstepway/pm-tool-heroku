package by.stepovoy.pmtool.controller;

import by.stepovoy.pmtool.domain.ProjectTask;
import by.stepovoy.pmtool.service.ProjectTaskService;
import by.stepovoy.pmtool.service.ValidationErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {
    private final ProjectTaskService projectTaskService;
    private final ValidationErrorService validationErrorService;

    public BacklogController(ProjectTaskService projectTaskService,
                             ValidationErrorService validationErrorService) {
        this.projectTaskService = projectTaskService;
        this.validationErrorService = validationErrorService;
    }

    @PostMapping("/{backlogId}")
    public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask,
                                                     BindingResult result,
                                                     @PathVariable String backlogId,
                                                     Principal principal) {
        ResponseEntity<?> errorMap = validationErrorService.validate(result);
        if (errorMap != null) {
            return errorMap;
        }

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlogId, projectTask, principal.getName());

        return new ResponseEntity<>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{projectIdentifier}")
    public List<ProjectTask> getProjectBacklog(@PathVariable String projectIdentifier, Principal principal) {
        return projectTaskService.findBacklogById(projectIdentifier, principal.getName());
    }

    @GetMapping("/{projectIdentifier}/{projectTaskId}")
    public ResponseEntity<?> getProjectTask(@PathVariable String projectIdentifier, @PathVariable String projectTaskId, Principal principal) {
        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(projectIdentifier, projectTaskId, principal.getName());
        return new ResponseEntity<>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{projectIdentifier}/{projectTaskId}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
                                               @PathVariable String projectIdentifier, @PathVariable String projectTaskId, Principal principal) {
        ResponseEntity<?> errorMap = validationErrorService.validate(result);
        if (errorMap != null) {
            return errorMap;
        }

        ProjectTask updatedTask = projectTaskService.updateProjectTaskByProjectSequence(projectTask,
                projectIdentifier,
                projectTaskId,
                principal.getName());

        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{projectIdentifier}/{projectTaskId}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String projectIdentifier,
                                               @PathVariable String projectTaskId,
                                               Principal principal) {
        projectTaskService.deleteProjectTaskByProjectSequence(projectIdentifier, projectTaskId, principal.getName());
        return new ResponseEntity<>("Project task '" + projectTaskId + "' was deleted", HttpStatus.OK);
    }

}