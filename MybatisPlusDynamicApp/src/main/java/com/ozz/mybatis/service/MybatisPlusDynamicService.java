package com.ozz.mybatis.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.ozz.mybatis.mapper.MyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MybatisPlusDynamicService {
    @Autowired
    MyMapper myMapper;

    @DS("ds2")
    public List<String> selectDynamicDb(long id) {
        return myMapper.selectTest(id);
    }

    public List<String> selectDynamicDb2(long id) {
        DynamicDataSourceContextHolder.push("ds2");
        try {
            return myMapper.selectTest(id);
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }
}
