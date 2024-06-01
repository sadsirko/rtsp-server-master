package com.rtsp.rtspserver.service;

import com.rtsp.rtspserver.model.ConnectionLog;
import com.rtsp.rtspserver.repository.ConnectionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionLogService {

    @Autowired
    private ConnectionLogRepository connectionLogRepository;

    public ConnectionLog addConnectionLog(ConnectionLog connectionLog) {
        connectionLogRepository.addConnectionLog(connectionLog);
        return connectionLog;
    }

    public ConnectionLog getConnectionLogById(int connectionId) {
        return connectionLogRepository.getConnectionLogById(connectionId);
    }

    public List<ConnectionLog> getAllConnectionLogs() {
        return connectionLogRepository.getAllConnectionLogs();
    }

    public boolean updateConnectionLog(ConnectionLog connectionLog) {
        return connectionLogRepository.updateConnectionLog(connectionLog);
    }

    public boolean deleteConnectionLog(int connectionId) {
        return connectionLogRepository.deleteConnectionLog(connectionId);
    }
}

