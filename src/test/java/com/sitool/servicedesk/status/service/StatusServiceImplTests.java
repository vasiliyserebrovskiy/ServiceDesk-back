package com.sitool.servicedesk.status.service;

import com.sitool.servicedesk.status.dto.request.CreateStatusRequest;
import com.sitool.servicedesk.status.dto.request.UpdateStatusRequest;
import com.sitool.servicedesk.status.dto.response.StatusDto;
import com.sitool.servicedesk.status.entity.Status;
import com.sitool.servicedesk.status.exceptions.StatusAlreadyExistException;
import com.sitool.servicedesk.status.exceptions.StatusNotFoundException;
import com.sitool.servicedesk.status.mapper.StatusMapper;
import com.sitool.servicedesk.status.repository.StatusRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatusServiceImplTests {

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private StatusMapper statusMapper;

    @InjectMocks
    private StatusServiceImpl statusService;

    @Test
    @DisplayName("Create status → created successfully")
    void shouldCreateStatusSuccessfully() {
        CreateStatusRequest request = new CreateStatusRequest(
                "Open", "Some description", true, false, false, false, false
        );

        when(statusRepository.existsByNameIgnoreCase("Open")).thenReturn(false);
        when(statusRepository.save(any(Status.class))).thenAnswer(i -> i.getArgument(0));
        when(statusMapper.statusToStatusDto(any(Status.class)))
                .thenReturn(new StatusDto(UUID.randomUUID(), "Open", "Some description", true, false, false, false, false));

        StatusDto result = statusService.createStatus(request);

        assertEquals("Open", result.name());
        assertTrue(result.isIncident());
        verify(statusRepository).save(any(Status.class));
    }

    @Test
    @DisplayName("Create status → name already exists → throws exception")
    void shouldThrowExceptionWhenStatusNameAlreadyExists() {
        CreateStatusRequest request = new CreateStatusRequest(
                "Open", "Some description", true, false, false, false, false
        );

        when(statusRepository.existsByNameIgnoreCase("Open")).thenReturn(true);

        assertThrows(StatusAlreadyExistException.class,
                () -> statusService.createStatus(request));

        verify(statusRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update status → updated successfully")
    void shouldUpdateStatusSuccessfully() {
        UUID statusId = UUID.randomUUID();

        Status existing = new Status();
        existing.setName("Open");
        existing.setDescription("Old description");
        existing.setIsIncident(true);
        existing.setIsProblem(false);
        existing.setIsRequest(false);
        existing.setIsChange(false);
        existing.setIsTask(false);

        UpdateStatusRequest request = new UpdateStatusRequest(
                "Open Updated", "New description", true, false, false, false, false
        );

        when(statusRepository.findById(statusId)).thenReturn(Optional.of(existing));
        when(statusRepository.existsByNameIgnoreCase("Open Updated")).thenReturn(false);
        when(statusMapper.statusToStatusDto(any()))
                .thenReturn(new StatusDto(statusId, "Open Updated", "New description", true, false, false, false, false));

        StatusDto result = statusService.updateStatus(statusId, request);

        assertEquals("Open Updated", result.name());
        verify(statusRepository).save(existing);
    }

    @Test
    @DisplayName("Update status → nothing changed → no save")
    void shouldNotSaveWhenNothingChanged() {
        UUID statusId = UUID.randomUUID();

        Status existing = new Status();
        existing.setName("Open");
        existing.setDescription("Some description");
        existing.setIsIncident(true);
        existing.setIsProblem(false);
        existing.setIsRequest(false);
        existing.setIsChange(false);
        existing.setIsTask(false);

        UpdateStatusRequest request = new UpdateStatusRequest(
                "Open", "Some description", true, false, false, false, false
        );

        when(statusRepository.findById(statusId)).thenReturn(Optional.of(existing));
        when(statusMapper.statusToStatusDto(any()))
                .thenReturn(new StatusDto(statusId, "Open", "Some description", true, false, false, false, false));

        statusService.updateStatus(statusId, request);

        verify(statusRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update status → not found → throws exception")
    void shouldThrowExceptionWhenUpdatingNonExistentStatus() {
        UUID statusId = UUID.randomUUID();
        UpdateStatusRequest request = new UpdateStatusRequest(
                "Open", "Some description", true, false, false, false, false
        );

        when(statusRepository.findById(statusId)).thenReturn(Optional.empty());

        assertThrows(StatusNotFoundException.class,
                () -> statusService.updateStatus(statusId, request));
    }

    @Test
    @DisplayName("Update status → new name already exists → throws exception")
    void shouldThrowExceptionWhenNewNameAlreadyExists() {
        UUID statusId = UUID.randomUUID();

        Status existing = new Status();
        existing.setName("Open");
        existing.setDescription("Some description");
        existing.setIsIncident(true);
        existing.setIsProblem(false);
        existing.setIsRequest(false);
        existing.setIsChange(false);
        existing.setIsTask(false);

        UpdateStatusRequest request = new UpdateStatusRequest(
                "In Progress", "Some description", true, false, false, false, false
        );

        when(statusRepository.findById(statusId)).thenReturn(Optional.of(existing));
        when(statusRepository.existsByNameIgnoreCase("In Progress")).thenReturn(true);

        assertThrows(StatusAlreadyExistException.class,
                () -> statusService.updateStatus(statusId, request));

        verify(statusRepository, never()).save(any());
    }

    @Test
    @DisplayName("Delete status → deleted successfully")
    void shouldDeleteStatusSuccessfully() {
        UUID statusId = UUID.randomUUID();
        Status existing = new Status();

        when(statusRepository.findById(statusId)).thenReturn(Optional.of(existing));

        statusService.deleteStatus(statusId);

        verify(statusRepository).delete(existing);
    }

    @Test
    @DisplayName("Delete status → not found → throws exception")
    void shouldThrowExceptionWhenDeletingNonExistentStatus() {
        UUID statusId = UUID.randomUUID();

        when(statusRepository.findById(statusId)).thenReturn(Optional.empty());

        assertThrows(StatusNotFoundException.class,
                () -> statusService.deleteStatus(statusId));

        verify(statusRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Get status by id → returns status")
    void shouldReturnStatusById() {
        UUID statusId = UUID.randomUUID();
        Status existing = new Status();
        StatusDto dto = new StatusDto(statusId, "Open", "Some description", true, false, false, false, false);

        when(statusRepository.findById(statusId)).thenReturn(Optional.of(existing));
        when(statusMapper.statusToStatusDto(existing)).thenReturn(dto);

        StatusDto result = statusService.getStatus(statusId);

        assertEquals(statusId, result.id());
        verify(statusRepository).findById(statusId);
    }

    @Test
    @DisplayName("Get status by id → not found → throws exception")
    void shouldThrowExceptionWhenStatusNotFound() {
        UUID statusId = UUID.randomUUID();

        when(statusRepository.findById(statusId)).thenReturn(Optional.empty());

        assertThrows(StatusNotFoundException.class,
                () -> statusService.getStatus(statusId));
    }

    @Test
    @DisplayName("Get all statuses → no type → returns all")
    void shouldReturnAllStatusesWhenNoType() {
        Status s1 = new Status();
        Status s2 = new Status();

        when(statusRepository.findAll()).thenReturn(List.of(s1, s2));
        when(statusMapper.statusToStatusDto(any()))
                .thenReturn(new StatusDto(UUID.randomUUID(), "Open", "", true, false, false, false, false));

        List<StatusDto> result = statusService.getAllStatuses(null);

        assertEquals(2, result.size());
        verify(statusRepository).findAll();
    }

    @Test
    @DisplayName("Get all statuses → type INCIDENT → returns incident statuses")
    void shouldReturnIncidentStatuses() {
        Status s1 = new Status();

        when(statusRepository.findAllByIsIncidentTrue()).thenReturn(List.of(s1));
        when(statusMapper.statusToStatusDto(any()))
                .thenReturn(new StatusDto(UUID.randomUUID(), "Open", "", true, false, false, false, false));

        List<StatusDto> result = statusService.getAllStatuses("INCIDENT");

        assertEquals(1, result.size());
        verify(statusRepository).findAllByIsIncidentTrue();
    }

    @Test
    @DisplayName("Get all statuses → type TASK → returns task statuses")
    void shouldReturnTaskStatuses() {
        Status s1 = new Status();

        when(statusRepository.findAllByIsTaskTrue()).thenReturn(List.of(s1));
        when(statusMapper.statusToStatusDto(any()))
                .thenReturn(new StatusDto(UUID.randomUUID(), "Open", "", false, false, false, false, true));

        List<StatusDto> result = statusService.getAllStatuses("TASK");

        assertEquals(1, result.size());
        verify(statusRepository).findAllByIsTaskTrue();
    }

    @Test
    @DisplayName("Get all statuses → unknown type → returns all")
    void shouldReturnAllStatusesWhenUnknownType() {
        when(statusRepository.findAll()).thenReturn(List.of());

        statusService.getAllStatuses("UNKNOWN");

        verify(statusRepository).findAll();
    }
}
