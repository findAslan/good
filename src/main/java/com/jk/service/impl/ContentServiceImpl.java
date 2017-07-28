package com.jk.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jk.model.Content;
import com.jk.service.ContentService;
import com.xiaoleilu.hutool.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author cuiP
 * Created by JK on 2017/4/19.
 */
@Transactional
@Service
public class ContentServiceImpl extends BaseServiceImpl<Content> implements ContentService {


    @Transactional(readOnly = true)
    @Override
    public PageInfo<Content> findPage(Integer pageNum, Integer pageSize, Long catId, String title, String startTime, String endTime) {

        Example example = new Example(Content.class);
        Example.Criteria criteria = example.createCriteria();

        if(null != catId){
            criteria.andEqualTo("contentCatId", catId);
        }if(StringUtils.isNotEmpty(title)){
            criteria.andLike("title", "%"+title+"%");
        }if(startTime != null && endTime != null){
            criteria.andBetween("createTime", DateUtil.beginOfDay(DateUtil.parse(startTime)), DateUtil.endOfDay(DateUtil.parse(endTime)));
        }

        //排序
        example.orderBy("createTime").desc();

        //分页
        PageHelper.startPage(pageNum,pageSize);

        List<Content> contentList = this.selectByExample(example);
        return new PageInfo<Content>(contentList);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<Content> findPageNews(Integer pageNum, Integer pageSize, Long catId) {
        return this.findPage(pageNum, pageSize, catId, null, null, null);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<Content> findTop3ByCatId(Long catId) {
        return this.findPageNews(1, 3, catId);
    }
}
