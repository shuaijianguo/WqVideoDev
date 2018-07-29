package com.wq.mapper;

import com.wq.pojo.SearchRecords;
import com.wq.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
    public List<String> getHotWords();
}