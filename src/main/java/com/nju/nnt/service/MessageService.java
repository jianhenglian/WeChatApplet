package com.nju.nnt.service;

import com.nju.nnt.entity.Message;
import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    void insertMessage(Message message);
}
