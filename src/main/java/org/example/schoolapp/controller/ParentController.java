package org.example.schoolapp.controller;

import lombok.AllArgsConstructor;
import org.example.schoolapp.dto.Response;
import org.example.schoolapp.dto.request.ParentDtoRequest;
import org.example.schoolapp.dto.response.ParentDto;
import org.example.schoolapp.service.ParentService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "ap1/v1/parent")
public class ParentController {
    private final ParentService parentService;

    @GetMapping("/get-parent-by-id/{parentId}")
    public ResponseEntity<Response> getParentById(@PathVariable Long parentId) {
        ParentDto parentDto = parentService.getDtoById(parentId);
        return ResponseEntity.ok(new Response("Successfully retrieved Parent with Id: " + parentId, parentDto));
    }

    @GetMapping("/get-all-parent")
    public ResponseEntity<Response> getAllParent() {
        List<ParentDto> parents = parentService.getAllParent();

        if (parents.isEmpty())
            throw new ObjectNotFoundException("No Parent found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Parents.", parents));
    }

    @GetMapping("/get-all-active-parent")
    public ResponseEntity<Response> getAllActiveParent() {
        List<ParentDto> parents = parentService.getAllActiveParent();

        if (parents.isEmpty())
            throw new ObjectNotFoundException("No active Parent found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Parents.", parents));
    }

    @PostMapping(value = "/create-parent")
    public ResponseEntity<Response> createParent(@RequestBody ParentDtoRequest request) {
        ParentDto parentDto = parentService.createParent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Parent.", parentDto));
    }

    @PutMapping(value = "/update-parent")
    public ResponseEntity<Response> updateParent(@RequestBody ParentDtoRequest request) {
        ParentDto parentDto = parentService.updateParent(request);
        return ResponseEntity.ok(new Response("Successfully updated Parent with id: " + request.getId(), parentDto));
    }

    @DeleteMapping(value = "/delete-parent/{parentId}")
    public ResponseEntity<Response> deleteParent(@PathVariable Long parentId) {
        parentService.deleteParent(parentId);
        return ResponseEntity.ok(new Response("Successfully deleted Parent with id: " + parentId, null));
    }

    @PutMapping(value = "/restore-parent/{parentId}")
    public ResponseEntity<Response> restoreParent(@PathVariable Long parentId) {
        ParentDto parentDto = parentService.restoreParent(parentId);
        return ResponseEntity.ok(new Response("Successfully restored Parent with id: " + parentId, parentDto));
    }
}
