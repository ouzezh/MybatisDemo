package com.ozz.mybatis.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ozz.mybatis.mapper.routing.datasource.TestMapper;
import com.ozz.mybatis.model.TestObject;
import com.ozz.mybatis.service.ITestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, TestObject> implements ITestService {
    public void page(int current, int size) {
        IPage<TestObject> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        page(page);
    }
}
