package com.hawen.thrift.service.impl;

import com.hawen.thrift.pojo.UserPojo;
import com.hawen.thrift.service.UserService;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService.Iface {
    @Override
    public UserPojo getUser(long id) throws TException {
        UserPojo userPojo = new UserPojo();
        userPojo.setId(id);
        userPojo.setUsername("userName_" + id);
        userPojo.setNote("note_" + id);
        return userPojo;
    }
}
