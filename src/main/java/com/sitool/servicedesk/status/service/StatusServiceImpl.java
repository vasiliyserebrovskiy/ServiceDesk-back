package com.sitool.servicedesk.status.service;

import com.sitool.servicedesk.status.dto.request.CreateStatusRequest;
import com.sitool.servicedesk.status.dto.request.UpdateStatusRequest;
import com.sitool.servicedesk.status.dto.response.StatusDto;
import com.sitool.servicedesk.status.mapper.StatusMapper;
import com.sitool.servicedesk.status.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;
    private final StatusMapper statusMapper;

    @Override
    @Transactional
    public StatusDto createStatus(CreateStatusRequest request) {
        return null;
    }

    @Override
    @Transactional
    public StatusDto updateStatus(UUID statusId, UpdateStatusRequest request) {
        return null;
    }

    @Override
    @Transactional
    public void deleteStatus(UUID statusId) {

    }

    @Override
    @Transactional(readOnly = true)
    public StatusDto getStatus(UUID statusId) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusDto> getAllStatuses(String type) {
        return List.of();
    }
}
