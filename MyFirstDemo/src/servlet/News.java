package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DataBase;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class News
 */
@WebServlet("/News")
public class News extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public News() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		JSONArray jsonArray=new JSONArray();
		DataBase mysql=new DataBase();
		mysql.connect();
		String sql;
		if(request.getParameter("flag").equals("lease")||request.getParameter("flag").equals("purchase"))
		{
			if(request.getParameter("flag").equals("lease"))
				sql="select * from lease_news where to_tel='"+request.getSession().getAttribute("user")+"'";
			else
				sql="select * from purchase_news where to_tel='"+request.getSession().getAttribute("user")+"'";
			try {
				mysql.sqlQuery(sql);
				while(mysql.getRs().next())
				{
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("from_tel", mysql.getRs().getString("from_tel"));
					jsonObject.put("data", mysql.getRs().getString("data"));
					jsonObject.put("status", mysql.getRs().getString("status"));
					jsonObject.put("id", mysql.getRs().getString("id"));
					jsonArray.add(jsonObject);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			out.write(jsonArray.toString());
		}
		else if(request.getParameter("flag").equals("lease_send")||request.getParameter("flag").equals("purchase_send"))
		{
			if(request.getParameter("flag").equals("lease_send"))
				sql="insert into lease_news (from_tel,to_tel,data,status) values ('"+request.getSession().getAttribute("user")+"','"
				+request.getParameter("to_tel")+"','"+request.getParameter("sendnews_data")+"',0)";
			else
				sql="insert into purchase_news (from_tel,to_tel,data,status) values ('"+request.getSession().getAttribute("user")+"','"
				+request.getParameter("to_tel")+"','"+request.getParameter("sendnews_data")+"',0)";
			try {
				mysql.sqlUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			out.write("发送成功！");
		}
		else if(request.getParameter("flag").equals("lease_delete")||request.getParameter("flag").equals("purchase_delete"))
		{
			if(request.getParameter("flag").equals("lease_delete"))
				sql="delete from lease_news where id="+request.getParameter("id");
			else
				sql="delete from purchase_news where id="+request.getParameter("id");
			try {
				mysql.sqlUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			out.write("删除成功！");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
