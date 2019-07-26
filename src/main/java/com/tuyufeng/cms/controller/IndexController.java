package com.tuyufeng.cms.controller;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.pagehelper.PageHelper;
import com.tuyufeng.cms.domain.Article;
import com.tuyufeng.cms.service.ArticleService;
import com.tuyufeng.cms.service.ChannelService;
import com.tuyufeng.cms.utils.CMSConstant;

@Controller
public class IndexController {
	
	
	@Resource
	private ChannelService channelService;
	
	@Resource
	private  ArticleService articleService;
	
	/**
	 * 
	 * @Title: toIndex 
	 * @Description: 进入首页
	 * @return
	 * @return: String
	 */
	@GetMapping({"/","index","toIndex"})
	public String toIndex(Article article,Model model) {
		//返回所有栏目
		List<Map> channels = channelService.selectChannels();
		//文章只能显示审核
		article.setStatus(CMSConstant.ARTICLE_STATUS_CHECKED);
		
		
		//如果用户没有点击栏目,就默认显示热点文章
		if(null==article.getChannelId()) {
			article.setHot(1);//1 热点文章
		List<Map> hotArticles = articleService.getTitles(article);
		model.addAttribute("hotArticles", hotArticles);
		}else {
			//查询栏目下的所有分类
			List<Map> categorys = channelService.selectCategorys(article.getChannelId());
			model.addAttribute("categorys", categorys);
		}
		
		
		//具体栏目或分类下的文章
		List<Map> articles = articleService.getTitles(article);
		//获取最新文章
		int pageSize =10;
		PageHelper.startPage(0, pageSize);
		List<Map> lasts = articleService.getTitles(null);
		
		
		model.addAttribute("channels", channels);
		model.addAttribute("article", article);
		model.addAttribute("articles", articles);
		model.addAttribute("lasts", lasts);
		return "index/index";
	}

}
