package com.tuyufeng.cms.service.impl;


import java.util.List;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tuyufeng.cms.dao.ArticleMapper;
import com.tuyufeng.cms.domain.Article;
import com.tuyufeng.cms.service.ArticleService;


@Service
public class ArticleServiceImpl implements ArticleService {
	
	@Resource
	private ArticleMapper articleMapper;
	

	@Override
	public int insert(Article article) {
		return articleMapper.insert(article);
	}


	@Override
	public Map get(Integer id) {
		// TODO Auto-generated method stub
		return articleMapper.get(id);
	}


	@Override
	public List<Map> getTitles(Article article) {
		// TODO Auto-generated method stub
		return articleMapper.getTitles(article);
	}


	@Override
	public int update(Article article) {
		// TODO Auto-generated method stub
		return articleMapper.update(article);
	}

}
