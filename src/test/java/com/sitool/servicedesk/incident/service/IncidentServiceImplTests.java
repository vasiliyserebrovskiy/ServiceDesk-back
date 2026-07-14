package com.sitool.servicedesk.incident.service;

import com.sitool.servicedesk.category.entity.Category;
import com.sitool.servicedesk.category.exceptions.CategoryNotFoundException;
import com.sitool.servicedesk.category.repository.CategoryRepository;
import com.sitool.servicedesk.incident.dto.request.CreateIncidentRequest;
import com.sitool.servicedesk.incident.dto.request.UpdateIncidentRequest;
import com.sitool.servicedesk.incident.dto.response.IncidentDto;
import com.sitool.servicedesk.incident.dto.response.NextIncidentNumberResponse;
import com.sitool.servicedesk.incident.entity.Incident;
import com.sitool.servicedesk.incident.exceptions.IncidentAlreadyExistException;
import com.sitool.servicedesk.incident.exceptions.IncidentNotFoundException;
import com.sitool.servicedesk.incident.mapper.IncidentMapper;
import com.sitool.servicedesk.incident.repository.IncidentRepository;
import com.sitool.servicedesk.servicenow.service.ServiceNowIntegrationService;
import com.sitool.servicedesk.shared.enums.Impact;
import com.sitool.servicedesk.shared.enums.Priority;
import com.sitool.servicedesk.shared.enums.Urgency;
import com.sitool.servicedesk.status.entity.Status;
import com.sitool.servicedesk.status.repository.StatusRepository;
import com.sitool.servicedesk.status.exceptions.StatusNotFoundException;
import com.sitool.servicedesk.user.entity.User;
import com.sitool.servicedesk.user.exceptions.UserNotFoundException;
import com.sitool.servicedesk.user.repository.UserRepository;
import com.sitool.servicedesk.utils.BaseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IncidentServiceImplTests {

    @Mock
    private IncidentRepository incidentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private StatusRepository statusRepository;
    @Mock
    private IncidentMapper mapper;
    @Mock
    private ServiceNowIntegrationService serviceNowIntegrationService;

    @InjectMocks
    private IncidentServiceImpl incidentService;

    private UUID incidentId;
    private UUID requesterId;
    private UUID categoryId;
    private UUID statusId;

    @BeforeEach
    void setUp() {
        incidentId = UUID.randomUUID();
        requesterId = UUID.randomUUID();
        categoryId = UUID.randomUUID();
        statusId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Get next incident number → returns formatted number")
    void shouldReturnNextIncidentNumber() {
        when(incidentRepository.getNextNumber()).thenReturn(1L);

        NextIncidentNumberResponse result = incidentService.getNextIncidentNumber();

        assertEquals("INC0000001", result.number());
    }

    @Test
    @DisplayName("Create incident → created successfully")
    void shouldCreateIncidentSuccessfully() {
        CreateIncidentRequest request = new CreateIncidentRequest(
                "INC0000001", requesterId, categoryId, null,
                statusId, Priority.LOW, Impact.LOW, Urgency.LOW,
                null, null, null, "Short description", "Some description", false
        );

        User requester = new User();
        Category category = new Category();
        Status status = new Status();
        LocalDateTime dateTime = LocalDateTime.now();

        IncidentDto dto = new IncidentDto(
                incidentId, "INC0000001", requesterId, categoryId, null,
                statusId, Priority.LOW, Impact.LOW, Urgency.LOW,
                null, null, null, "Short description", "Some description",
                null, false, null, dateTime
        );

        when(incidentRepository.existsByNumber("INC0000001")).thenReturn(false);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(statusRepository.findById(statusId)).thenReturn(Optional.of(status));
        when(incidentRepository.saveAndFlush(any(Incident.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.toIncidentDto(any(Incident.class))).thenReturn(dto);

        IncidentDto result = incidentService.createIncident(request);

        assertEquals("INC0000001", result.number());
        assertEquals(Priority.LOW, result.priority());
        verify(incidentRepository).saveAndFlush(any(Incident.class));
    }

    @Test
    @DisplayName("Create incident → number already exists → throws exception")
    void shouldThrowExceptionWhenIncidentNumberAlreadyExists() {
        CreateIncidentRequest request = new CreateIncidentRequest(
                "INC0000001", requesterId, categoryId, null,
                statusId, Priority.LOW, Impact.LOW, Urgency.LOW,
                null, null, null, "Short description", "Some description", false
        );

        when(incidentRepository.existsByNumber("INC0000001")).thenReturn(true);

        assertThrows(IncidentAlreadyExistException.class,
                () -> incidentService.createIncident(request));

        verify(incidentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create incident → requester not found → throws exception")
    void shouldThrowExceptionWhenRequesterNotFound() {
        CreateIncidentRequest request = new CreateIncidentRequest(
                "INC0000001", requesterId, categoryId, null,
                statusId, Priority.LOW, Impact.LOW, Urgency.LOW,
                null, null, null, "Short description", "Some description", false
        );

        when(incidentRepository.existsByNumber("INC0000001")).thenReturn(false);
        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> incidentService.createIncident(request));

        verify(incidentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create incident → category not found → throws exception")
    void shouldThrowExceptionWhenCategoryNotFound() {
        CreateIncidentRequest request = new CreateIncidentRequest(
                "INC0000001", requesterId, categoryId, null,
                statusId, Priority.LOW, Impact.LOW, Urgency.LOW,
                null, null, null, "Short description", "Some description", false
        );

        when(incidentRepository.existsByNumber("INC0000001")).thenReturn(false);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(new User()));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> incidentService.createIncident(request));

        verify(incidentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create incident → status not found → throws exception")
    void shouldThrowExceptionWhenStatusNotFound() {
        CreateIncidentRequest request = new CreateIncidentRequest(
                "INC0000001", requesterId, categoryId, null,
                statusId, Priority.LOW, Impact.LOW, Urgency.LOW,
                null, null, null, "Short description", "Some description", false
        );

        when(incidentRepository.existsByNumber("INC0000001")).thenReturn(false);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(new User()));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(new Category()));
        when(statusRepository.findById(statusId)).thenReturn(Optional.empty());

        assertThrows(StatusNotFoundException.class,
                () -> incidentService.createIncident(request));

        verify(incidentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update incident → updated successfully")
    void shouldUpdateIncidentSuccessfully() {
        UUID newStatusId = UUID.randomUUID();
        LocalDateTime dateTime = LocalDateTime.now();

        User requester = new User();
        setId(requester, requesterId);

        Category category = new Category();
        setId(category, categoryId);

        Status oldStatus = new Status();
        setId(oldStatus, statusId);

        Status newStatus = new Status();
        setId(newStatus, newStatusId);

        Incident existing = new Incident();
        existing.setNumber("INC0000001");
        existing.setRequester(requester);
        existing.setCategory(category);
        existing.setStatus(oldStatus);
        existing.setImpact(Impact.LOW);
        existing.setUrgency(Urgency.LOW);
        existing.setPriority(Priority.LOW);
        existing.setShortDescription("Old description");

        UpdateIncidentRequest request = new UpdateIncidentRequest(
                requesterId, categoryId, null, newStatusId, Priority.LOW,
                Impact.LOW, Urgency.LOW,
                null, null, null,
                "New short description", null
        );

        IncidentDto dto = new IncidentDto(
                incidentId, "INC0000001", requesterId, categoryId, null,
                newStatusId, Priority.LOW, Impact.LOW, Urgency.LOW,
                null, null, null, "New short description", null,
                null, false, null, dateTime
        );

        when(incidentRepository.findById(incidentId)).thenReturn(Optional.of(existing));
        when(statusRepository.findById(newStatusId)).thenReturn(Optional.of(newStatus));
        when(mapper.toIncidentDto(any())).thenReturn(dto);

        IncidentDto result = incidentService.updateIncident(incidentId, request);

        assertEquals("New short description", result.shortDescription());
        verify(incidentRepository).save(existing);
    }

    @Test
    @DisplayName("Update incident → not found → throws exception")
    void shouldThrowExceptionWhenUpdatingNonExistentIncident() {
        UpdateIncidentRequest request = new UpdateIncidentRequest(
                requesterId, categoryId, null, statusId, Priority.LOW,
                Impact.LOW, Urgency.LOW,
                null, null, null,
                "Short description", null
        );

        when(incidentRepository.findById(incidentId)).thenReturn(Optional.empty());

        assertThrows(IncidentNotFoundException.class,
                () -> incidentService.updateIncident(incidentId, request));
    }

    @Test
    @DisplayName("Get incident by id → returns incident")
    void shouldReturnIncidentById() {
        Incident existing = new Incident();
        LocalDateTime dateTime = LocalDateTime.now();
        IncidentDto dto = new IncidentDto(
                incidentId, "INC0000001", requesterId, categoryId, null,
                statusId, Priority.LOW, Impact.LOW, Urgency.LOW,
                null, null, null, "Short description", null,
                null, false, null, dateTime
        );

        when(incidentRepository.findById(incidentId)).thenReturn(Optional.of(existing));
        when(mapper.toIncidentDto(existing)).thenReturn(dto);

        IncidentDto result = incidentService.getIncident(incidentId);

        assertEquals(incidentId, result.id());
        verify(incidentRepository).findById(incidentId);
    }

    @Test
    @DisplayName("Get incident by id → not found → throws exception")
    void shouldThrowExceptionWhenIncidentNotFound() {
        when(incidentRepository.findById(incidentId)).thenReturn(Optional.empty());

        assertThrows(IncidentNotFoundException.class,
                () -> incidentService.getIncident(incidentId));
    }

    @Test
    @DisplayName("Get all incidents → returns list")
    void shouldReturnAllIncidents() {
        Incident i1 = new Incident();
        Incident i2 = new Incident();
        LocalDateTime dateTime = LocalDateTime.now();

        when(incidentRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(i1, i2));
        when(mapper.toIncidentDto(any())).thenReturn(
                new IncidentDto(UUID.randomUUID(), "INC0000001", requesterId, categoryId, null,
                        statusId, Priority.LOW, Impact.LOW, Urgency.LOW,
                        null, null, null, "Short description", null,
                        null, false, null, dateTime)
        );

        List<IncidentDto> result = incidentService.getAllIncidents();

        assertEquals(2, result.size());
        verify(incidentRepository).findAllByOrderByCreatedAtDesc();
    }

    private void setId(Object entity, UUID id) {
        try {
            Field field = BaseEntity.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
/* OLD Test with sync method
    @Test
    @DisplayName("Create incident → syncToServiceNow true → calls ServiceNow sync")
    void shouldCallServiceNowSyncWhenSyncToServiceNowIsTrue() {
        CreateIncidentRequest request = new CreateIncidentRequest(
                "INC0000001", requesterId, categoryId, null,
                statusId, Priority.LOW, Impact.LOW, Urgency.LOW,
                null, null, null, "Short description", "Some description", true
        );

        User requester = new User();
        Category category = new Category();
        Status status = new Status();
        LocalDateTime dateTime = LocalDateTime.now();

        IncidentDto dto = new IncidentDto(
                incidentId, "INC0000001", requesterId, categoryId, null,
                statusId, Priority.LOW, Impact.LOW, Urgency.LOW,
                null, null, null, "Short description", "Some description",
                null, false, null, dateTime
        );

        when(incidentRepository.existsByNumber("INC0000001")).thenReturn(false);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(statusRepository.findById(statusId)).thenReturn(Optional.of(status));
        when(incidentRepository.saveAndFlush(any(Incident.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.toIncidentDto(any(Incident.class))).thenReturn(dto);

        incidentService.createIncident(request);

        verify(serviceNowIntegrationService).syncIncidentToServiceNow(any(Incident.class)); // sync method
    } */

    @Test
    @DisplayName("Create incident → syncToServiceNow true → calls ServiceNow async sync")
    void shouldCallServiceNowSyncWhenSyncToServiceNowIsTrue() {
        CreateIncidentRequest request = new CreateIncidentRequest(
                "INC0000001", requesterId, categoryId, null,
                statusId, Priority.LOW, Impact.LOW, Urgency.LOW,
                null, null, null, "Short description", "Some description", true
        );

        User requester = new User();
        Category category = new Category();
        Status status = new Status();
        LocalDateTime dateTime = LocalDateTime.now();

        IncidentDto dto = new IncidentDto(
                incidentId, "INC0000001", requesterId, categoryId, null,
                statusId, Priority.LOW, Impact.LOW, Urgency.LOW,
                null, null, null, "Short description", "Some description",
                null, false, null, dateTime
        );

        when(incidentRepository.existsByNumber("INC0000001")).thenReturn(false);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(statusRepository.findById(statusId)).thenReturn(Optional.of(status));
        when(incidentRepository.saveAndFlush(any(Incident.class))).thenAnswer(i -> {
            Incident incidentArg = i.getArgument(0);
            setId(incidentArg, incidentId);
            return incidentArg;
        });
        when(mapper.toIncidentDto(any(Incident.class))).thenReturn(dto);

        incidentService.createIncident(request);

        verify(serviceNowIntegrationService).syncIncidentToServiceNowAsync(incidentId);
    }

    @Test
    @DisplayName("Create incident → syncToServiceNow false → does not call ServiceNow sync")
    void shouldNotCallServiceNowSyncWhenSyncToServiceNowIsFalse() {
        CreateIncidentRequest request = new CreateIncidentRequest(
                "INC0000001", requesterId, categoryId, null,
                statusId, Priority.LOW, Impact.LOW, Urgency.LOW,
                null, null, null, "Short description", "Some description", false
        );

        User requester = new User();
        Category category = new Category();
        Status status = new Status();
        LocalDateTime dateTime = LocalDateTime.now();

        IncidentDto dto = new IncidentDto(
                incidentId, "INC0000001", requesterId, categoryId, null,
                statusId, Priority.LOW, Impact.LOW, Urgency.LOW,
                null, null, null, "Short description", "Some description",
                null, false, null, dateTime
        );

        when(incidentRepository.existsByNumber("INC0000001")).thenReturn(false);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(statusRepository.findById(statusId)).thenReturn(Optional.of(status));
        when(incidentRepository.saveAndFlush(any(Incident.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.toIncidentDto(any(Incident.class))).thenReturn(dto);

        incidentService.createIncident(request);

        verify(serviceNowIntegrationService, never()).syncIncidentToServiceNow(any());
    }
}
