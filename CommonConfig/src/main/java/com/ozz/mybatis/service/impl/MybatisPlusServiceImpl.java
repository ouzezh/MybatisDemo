package com.ozz.mybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ozz.mybatis.mapper.MyMapper;
import com.ozz.mybatis.model.MyDict;
import com.ozz.mybatis.service.MybatisPlusService;
import org.springframework.stereotype.Service;

/**
 * mybatis plus
 */
@Service
public class MybatisPlusServiceImpl extends ServiceImpl<MyMapper, MyDict> implements
    MybatisPlusService {
}
