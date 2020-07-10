package com.ozz.mybatis.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ozz.mybatis.mapper.routing.datasource.TestMapper;
import com.ozz.mybatis.model.TestObject;
import com.ozz.mybatis.service.IMybatisPlusService;
import org.springframework.stereotype.Service;

/**
 * mybatis plus
 */
@Service
public class MybatisPlusServiceImpl extends ServiceImpl<TestMapper, TestObject> implements
    IMybatisPlusService {
    public void page(int current, int size) {
        IPage<TestObject> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        page(page);
    }
}
