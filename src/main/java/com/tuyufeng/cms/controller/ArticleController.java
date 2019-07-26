package com.tuyufeng.cms.controller;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @ClassName: ArticleController 
 * @Description: 文章控制器
 * @author: charles
 * @date: 2019年7月23日 上午10:20:01
 */

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuyufeng.cms.domain.Article;
import com.tuyufeng.cms.domain.User;
import com.tuyufeng.cms.service.ArticleService;
import com.tuyufeng.cms.utils.CMSConstant;
import com.tuyufeng.cms.utils.PageUtil;

@RequestMapping("article")
@Controller
public class ArticleController {

	@Resource
	private ArticleService articleService;

	
	
	/**
	 * 更新文章
	 * @Title: update 
	 * @Description: TODO
	 * @param article
	 * @return
	 * @return: boolean
	 */
	@ResponseBody
	@PostMapping("update")
	public  boolean update(Article article) {
	  return 	articleService.update(article)>0;
		
	}
/**
 * 
 * @Title: get 
 * @Description: 文章详情
 * @param id
 * @param model
 * @return
 * @return: String
 */
	@GetMapping("get")
	public String get(Integer id, Model model) {
		Map map = articleService.get(id);
		model.addAttribute("map", map);
		return "my/article/articledetail";
	}
	
	/**
	 * 
	 * @Title: get 
	 * @Description: 文章详情
	 * @param id
	 * @param model
	 * @return
	 * @return: String
	 */
		@GetMapping("getByAdmin")
		public String getByAdmin(Integer id, Model model) {
			Map map = articleService.get(id);
			model.addAttribute("map", map);
			return "admin/article/articledetail";
		}

	/**
	 * 
	 * @Title: getTitles
	 * @Description: 查询所有文章标题
	 * @param id
	 * @param model
	 * @return
	 * @return: String
	 */
	@GetMapping("getTitles")
	public String getTitles(@RequestParam(defaultValue="1")Integer page ,
			@RequestParam(defaultValue="2") Integer pageSize,Article article,
			
			Model model, HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		
			//查询用户自己文章
		  User user = (User) session.getAttribute(CMSConstant.LOGIN_GENERAL);
		  article.setUserId(user.getId());
		
		PageHelper.startPage(page, pageSize);
		
		List<Map> titles = articleService.getTitles(article);
		
		PageInfo<Map> pageInfo = new PageInfo<>(titles);
		String pages = PageUtil.page(page, pageInfo.getPages(), "/article/getTitles", pageSize);
		
		model.addAttribute("titles", titles);
		model.addAttribute("pages", pages);
		return "my/article/articletitles";
	}
	
	/**
	 * 
	 * @Title: getTitlesByAdmin 
	 * @Description: 管理员查询
	 * @param page
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 * @return: String
	 */
	@GetMapping("getTitlesByAdmin")
	public String getTitlesByAdmin(Article article,	@RequestParam(defaultValue="1")Integer page ,
			@RequestParam(defaultValue="3") Integer pageSize,
			
			Model model, HttpServletRequest request) {

		HttpSession session = request.getSession(false);
				
		PageHelper.startPage(page, pageSize);
		
		List<Map> titles = articleService.getTitles(article);
		
		PageInfo<Map> pageInfo = new PageInfo<>(titles);
		String pages = PageUtil.page(page, pageInfo.getPages(), "/article/getTitlesByAdmin", pageSize);
		
		model.addAttribute("titles", titles);
		model.addAttribute("pages", pages);
		model.addAttribute("article", article);//查询条件
		return "admin/article/articletitles";
	}

	/**
	 * 
	 * @Title: toPublish
	 * @Description: 去发布文章
	 * @return
	 * @return: String
	 */
	@GetMapping("toPublish")
	public String toPublish() {

		return "my/article/publish";
	}

	/**
	 * 
	 * @Title: publish
	 * @Description: 发布文章
	 * @param article
	 * @param file
	 * @return
	 * @return: boolean
	 */
	@ResponseBody
	@PostMapping("publish")
	public boolean publish(Article article, MultipartFile file,HttpServletRequest request) {

		if (!file.isEmpty()) {
			String path = "d:/pic/";
			// 上传的文件原始名称
			String originalFilename = file.getOriginalFilename();
			// 为防止名称重复.文件改名为uuid
			String filename = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));

			File f = new File(path + filename);

			try {
				file.transferTo(f);// 把文件写入硬盘
				// 封装图片
				article.setPicture(filename);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		article.setStatus(CMSConstant.ARTICLE_STATUS_NEW);
		article.setDeleted(0);
		article.setHot(0);
		article.setHits(0);
		
		
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute(CMSConstant.LOGIN_GENERAL);
		
		article.setUserId(user.getId());// 
		return articleService.insert(article) > 0;

	}
}
