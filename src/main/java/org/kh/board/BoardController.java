package org.kh.board;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Action List /ch19/list.do=ch19.action.ListAction
 * /ch19/writeForm.do=ch19.action.WriteFormAction
 * /ch19/writePro.do=ch19.action.WriteProAction
 * /ch19/content.do=ch19.action.ContentAction
 * /ch19/updateForm.do=ch19.action.UpdateFormAction
 * /ch19/updatePro.do=ch19.action.UpdateProAction
 * /ch19/deleteForm.do=ch19.action.DeleteFormAction
 * /ch19/deletePro.do=ch19.action.DeleteProAction
 * 
 * @author 최윤
 */
@Controller
public class BoardController {

	private static final Logger log = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	SqlMapClientTemplate sqlMap;

	@Autowired
	Date day;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	public String list(HttpServletRequest request) throws Throwable {

		String pageNum = request.getParameter("pageNum");// 페이지 번호

		if (pageNum == null) {
			pageNum = "1";
		}
		int pageSize = 10;// 한 페이지의 글의 개수
		int currentPage = Integer.parseInt(pageNum);
		int startRow = (currentPage - 1) * pageSize + 1;// 한 페이지의 시작글 번호
		int endRow = currentPage * pageSize;// 한 페이지의 마지막 글번호
		int count = 0;
		int number = 0;

		List articleList;
		count = (Integer) sqlMap.queryForObject("boardSQL.count");
		if (count > 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("start", startRow);
			map.put("end", endRow);
			articleList = sqlMap.queryForList("boardSQL.getArticles", map);
		} else {
			articleList = Collections.EMPTY_LIST;
		}
		
		log.info("\nlist:\t"+articleList);

		number = count - (currentPage - 1) * pageSize;// 글목록에 표시할 글번호
		// 해당 뷰에서 사용할 속성
		request.setAttribute("currentPage", new Integer(currentPage));
		request.setAttribute("startRow", new Integer(startRow));
		request.setAttribute("endRow", new Integer(endRow));
		request.setAttribute("count", new Integer(count));
		request.setAttribute("pageSize", new Integer(pageSize));
		request.setAttribute("number", new Integer(number));
		request.setAttribute("articleList", articleList);

		return "/ch19/list";// 해당 뷰
	}
}
