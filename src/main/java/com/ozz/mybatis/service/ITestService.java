package com.ozz.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ozz.mybatis.model.TestObject;

public interface ITestService extends IService<TestObject> {
    public void page(int current, int size);
}
