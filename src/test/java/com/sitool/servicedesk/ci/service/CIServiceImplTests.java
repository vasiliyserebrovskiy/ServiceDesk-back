package com.sitool.servicedesk.ci.service;

import com.sitool.servicedesk.ci.dto.request.CreateCIRequest;
import com.sitool.servicedesk.ci.dto.request.UpdateCIRequest;
import com.sitool.servicedesk.ci.dto.response.CIDto;
import com.sitool.servicedesk.ci.entity.CI;
import com.sitool.servicedesk.ci.exceptions.CIAlreadyExistException;
import com.sitool.servicedesk.ci.exceptions.CINotFoundException;
import com.sitool.servicedesk.ci.mapper.CIMapper;
import com.sitool.servicedesk.ci.repository.CIRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CIServiceImplTests {

    @Mock
    private CIRepository ciRepository;

    @Mock
    private CIMapper ciMapper;

    @InjectMocks
    private CIServiceImpl ciService;

    @Test
    @DisplayName("Create CI → created successfully")
    void shouldCreateCISuccessfully() {
        CreateCIRequest request = new CreateCIRequest(
                "Core-SW-01", "Core network switch located in server room A",
                "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300"
        );

        when(ciRepository.existsByNameIgnoreCase("Core-SW-01")).thenReturn(false);
        when(ciRepository.save(any(CI.class))).thenAnswer(i -> i.getArgument(0));
        when(ciMapper.ciToCIDto(any(CI.class)))
                .thenReturn(new CIDto(UUID.randomUUID(), "Core-SW-01",
                        "Core network switch located in server room A",
                        "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300"));

        CIDto result = ciService.createCI(request);

        assertEquals("Core-SW-01", result.name());
        verify(ciRepository).save(any(CI.class));
    }

    @Test
    @DisplayName("Create CI → name already exists → throws exception")
    void shouldThrowExceptionWhenCINameAlreadyExists() {
        CreateCIRequest request = new CreateCIRequest(
                "Core-SW-01", "Core network switch located in server room A",
                "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300"
        );

        when(ciRepository.existsByNameIgnoreCase("Core-SW-01")).thenReturn(true);

        assertThrows(CIAlreadyExistException.class,
                () -> ciService.createCI(request));

        verify(ciRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update CI → updated successfully")
    void shouldUpdateCISuccessfully() {
        UUID ciId = UUID.randomUUID();

        CI existing = new CI();
        existing.setName("Core-SW-01");
        existing.setDescription("Old description");
        existing.setType("Network Equipment");
        existing.setManufacturer("Cisco");
        existing.setSerialNumber("FCW2142L0QK");
        existing.setModel("Catalyst 9300");

        UpdateCIRequest request = new UpdateCIRequest(
                "Core-SW-02", "New description",
                "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300"
        );

        when(ciRepository.findById(ciId)).thenReturn(Optional.of(existing));
        when(ciRepository.existsByNameIgnoreCase("Core-SW-02")).thenReturn(false);
        when(ciMapper.ciToCIDto(any()))
                .thenReturn(new CIDto(ciId, "Core-SW-02", "New description",
                        "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300"));

        CIDto result = ciService.updateCI(ciId, request);

        assertEquals("Core-SW-02", result.name());
        verify(ciRepository).save(existing);
    }

    @Test
    @DisplayName("Update CI → nothing changed → no save")
    void shouldNotSaveWhenNothingChanged() {
        UUID ciId = UUID.randomUUID();

        CI existing = new CI();
        existing.setName("Core-SW-01");
        existing.setDescription("Core network switch located in server room A");
        existing.setType("Network Equipment");
        existing.setManufacturer("Cisco");
        existing.setSerialNumber("FCW2142L0QK");
        existing.setModel("Catalyst 9300");

        UpdateCIRequest request = new UpdateCIRequest(
                "Core-SW-01", "Core network switch located in server room A",
                "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300"
        );

        when(ciRepository.findById(ciId)).thenReturn(Optional.of(existing));
        when(ciMapper.ciToCIDto(any()))
                .thenReturn(new CIDto(ciId, "Core-SW-01",
                        "Core network switch located in server room A",
                        "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300"));

        ciService.updateCI(ciId, request);

        verify(ciRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update CI → not found → throws exception")
    void shouldThrowExceptionWhenUpdatingNonExistentCI() {
        UUID ciId = UUID.randomUUID();
        UpdateCIRequest request = new UpdateCIRequest(
                "Core-SW-01", "Core network switch located in server room A",
                "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300"
        );

        when(ciRepository.findById(ciId)).thenReturn(Optional.empty());

        assertThrows(CINotFoundException.class,
                () -> ciService.updateCI(ciId, request));
    }

    @Test
    @DisplayName("Update CI → new name already exists → throws exception")
    void shouldThrowExceptionWhenNewNameAlreadyExists() {
        UUID ciId = UUID.randomUUID();

        CI existing = new CI();
        existing.setName("Core-SW-01");
        existing.setDescription("Core network switch located in server room A");
        existing.setType("Network Equipment");
        existing.setManufacturer("Cisco");
        existing.setSerialNumber("FCW2142L0QK");
        existing.setModel("Catalyst 9300");

        UpdateCIRequest request = new UpdateCIRequest(
                "Core-SW-02", "Core network switch located in server room A",
                "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300"
        );

        when(ciRepository.findById(ciId)).thenReturn(Optional.of(existing));
        when(ciRepository.existsByNameIgnoreCase("Core-SW-02")).thenReturn(true);

        assertThrows(CIAlreadyExistException.class,
                () -> ciService.updateCI(ciId, request));

        verify(ciRepository, never()).save(any());
    }

    @Test
    @DisplayName("Delete CI → deleted successfully")
    void shouldDeleteCISuccessfully() {
        UUID ciId = UUID.randomUUID();
        CI existing = new CI();

        when(ciRepository.findById(ciId)).thenReturn(Optional.of(existing));

        ciService.deleteCI(ciId);

        verify(ciRepository).delete(existing);
    }

    @Test
    @DisplayName("Delete CI → not found → throws exception")
    void shouldThrowExceptionWhenDeletingNonExistentCI() {
        UUID ciId = UUID.randomUUID();

        when(ciRepository.findById(ciId)).thenReturn(Optional.empty());

        assertThrows(CINotFoundException.class,
                () -> ciService.deleteCI(ciId));

        verify(ciRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Get CI by id → returns CI")
    void shouldReturnCIById() {
        UUID ciId = UUID.randomUUID();
        CI existing = new CI();
        CIDto dto = new CIDto(ciId, "Core-SW-01",
                "Core network switch located in server room A",
                "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300");

        when(ciRepository.findById(ciId)).thenReturn(Optional.of(existing));
        when(ciMapper.ciToCIDto(existing)).thenReturn(dto);

        CIDto result = ciService.getCIById(ciId);

        assertEquals(ciId, result.id());
        verify(ciRepository).findById(ciId);
    }

    @Test
    @DisplayName("Get CI by id → not found → throws exception")
    void shouldThrowExceptionWhenCINotFound() {
        UUID ciId = UUID.randomUUID();

        when(ciRepository.findById(ciId)).thenReturn(Optional.empty());

        assertThrows(CINotFoundException.class,
                () -> ciService.getCIById(ciId));
    }

    @Test
    @DisplayName("Get all CIs → returns list")
    void shouldReturnAllCIs() {
        CI ci1 = new CI();
        CI ci2 = new CI();

        when(ciRepository.findAll()).thenReturn(List.of(ci1, ci2));
        when(ciMapper.ciToCIDto(any()))
                .thenReturn(new CIDto(UUID.randomUUID(), "Core-SW-01",
                        "Core network switch located in server room A",
                        "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300"));

        List<CIDto> result = ciService.getAllCI();

        assertEquals(2, result.size());
        verify(ciRepository).findAll();
    }

    @Test
    @DisplayName("Get all CIs → empty list")
    void shouldReturnEmptyListWhenNoCIs() {
        when(ciRepository.findAll()).thenReturn(List.of());

        List<CIDto> result = ciService.getAllCI();

        assertEquals(0, result.size());
        verify(ciRepository).findAll();
    }
}
