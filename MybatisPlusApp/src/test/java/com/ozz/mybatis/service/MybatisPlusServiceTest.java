package com.ozz.mybatis.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ozz.mybatis.model.MyDict;
import com.ozz.mybatis.util.MyBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class MybatisPlusServiceTest extends MyBaseTest {

    @Autowired
    MyService myService;
    @Autowired
    MybatisPlusService mybatisPlusService;

    @Test
    void testPage() {
        IPage<MyDict> page = new Page<>(1, 1, false);
        LambdaQueryWrapper<MyDict> query = new LambdaQueryWrapper<MyDict>().ge(MyDict::getId, 0L);
        mybatisPlusService.page(page, query);
        println(page.getRecords());
        Assert.isTrue(page.getRecords().size() == 1, StrUtil.EMPTY);
    }

    @Test
    void testUpdateBatchById() {
        long id = 1L;
        String name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss SSS"));
        List<String> list = selectName(id);
        Assert.isTrue(list.size() == 1 && !name.equals(list.get(0)), StrUtil.EMPTY);
        MyDict myDict = new MyDict();
        myDict.setId(id);
        myDict.setName(name);
        mybatisPlusService.updateBatchById(Collections.singletonList(myDict));
        list = selectName(id);
        Assert.isTrue(list.size() == 1 && name.equals(list.get(0)), StrUtil.EMPTY);
    }

    private List<String> selectName(long id) {
        return myService.selectJdbcTemplate("select name from t_dict where id=?", id);
    }
}
