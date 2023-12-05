package com.ozz.mybatis;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ozz.mybatis.model.MyDict;
import com.ozz.mybatis.service.MyService;
import com.ozz.mybatis.service.MybatisDynamicService;
import com.ozz.mybatis.service.MybatisPlusService;
import com.ozz.mybatis.util.MyBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
class MybatisDynamicAppTests extends MyBaseTest {
	@Autowired
	MyService myService;
	@Autowired
	MybatisDynamicService mybatisDynamicService;
	@Autowired
	MybatisPlusService mybatisPlusService;

	@Test
	void contextLoads() {
		com.github.pagehelper.Page<String> page = myService.selectPage(1L);
		println(page.getResult());
		Assert.isTrue(page.getResult().size()==1 && !"n21".equals(page.getResult().get(0)), StrUtil.EMPTY);

		List<String> list = mybatisDynamicService.selectDb2(1L);
		println(list);
		Assert.isTrue(list.size()==1 && "n21".equals(list.get(0)), StrUtil.EMPTY);

		IPage<MyDict> mp = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 10, false);
		mybatisPlusService.page(mp, new QueryWrapper<MyDict>().eq("id", 1L));
		println(mp.getRecords());
		Assert.isTrue(mp.getRecords().size()==1 && "c1".equals(mp.getRecords().get(0).getCode()), StrUtil.EMPTY);
	}

}
