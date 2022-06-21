package com.nju.nnt.service.impl;

import com.nju.nnt.mapper.MessageMapper;
import com.nju.nnt.service.MessageService;
import com.nju.nnt.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageMapper messageMapper;
    @Override
    public void insertMessage(Message message) {
       messageMapper.insert(message);
    }
}
