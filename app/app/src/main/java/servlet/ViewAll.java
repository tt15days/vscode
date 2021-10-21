package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.Config_iroiro;
import jsoup.ItemBean;
import main.ProcMain;

/**
 * Servlet implementation class ViewAll
 */
public class ViewAll extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProcMain pm = new ProcMain(); 
		ArrayList<ItemBean> beans = null;
		
		//ここの数字を変えるだけで取得データを変えられる
		//Config_iroiro.Categoryを参照
		String cateNum = request.getParameter("category");
		String targetURL = Config_iroiro.CateURL[Integer.valueOf(cateNum)];
		
		beans = (ArrayList<ItemBean>) pm.parse(targetURL);

		if (beans != null) {
			pm.SetDBItems(beans);
		}
		
		request.setAttribute("beans", beans);
		//HttpSession s = request.getSession(true);
		//s.setAttribute("beans", beans);
		
		 request.getRequestDispatcher(Config_iroiro.strJSP[0]).forward(request, response);
	}

}
