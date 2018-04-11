package com.luoromeo.rpc.services;

import java.util.List;

import com.luoromeo.rpc.services.pojo.Person;

public interface JdbcPersonManage {
    int save(Person p);

    void query(Person p);

    List<Person> query();
}
