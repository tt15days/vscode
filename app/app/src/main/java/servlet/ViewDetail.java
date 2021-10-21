package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.Config_iroiro;
import jsoup.ItemBean;
import main.ProcMain;

/**
 * Servlet implementation class ViewDetail
 */
public class ViewDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProcMain pm = new ProcMain();
		ItemBean b = new ItemBean();

		//jspにはinputタグのname属性=item_id value=商品ID
		String strID = request.getParameter("item_id");
		//GetItemDetailの引数にjspからのitem_idを入れる
		b = pm.GetItemDetail( strID );
		
		request.setAttribute("bean", b);
		request.getRequestDispatcher(Config_iroiro.strJSP[1]).forward(request, response);
		
	}

}
