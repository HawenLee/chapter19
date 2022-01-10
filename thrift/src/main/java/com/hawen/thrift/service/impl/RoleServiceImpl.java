package com.hawen.thrift.service.impl;

import com.hawen.thrift.pojo.RolePojo;
import com.hawen.thrift.service.RoleService;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService.Iface {
    @Override
    public List<RolePojo> getRoleByUserId(long userId) throws TException {
        List<RolePojo> rolePojos = new ArrayList<>();
        for (long i = userId; i < userId + 3; i++) {
            RolePojo rolePojo = new RolePojo();
            rolePojo.setId(i);
            rolePojo.setRoleName("role_name_" + i);
            rolePojo.setNote("note" + i);
            rolePojos.add(rolePojo);
        }
        return rolePojos;
    }
}
