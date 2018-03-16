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
 * Servlet implementation class LeaAndPur
 */
@WebServlet("/LeaAndPur")
public class LeaAndPur extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LeaAndPur() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		JSONArray jsonArray=new JSONArray();
		DataBase mysql=new DataBase();
		mysql.connect();
		String sql;
		if(request.getParameter("flag").equals("lease"))
			if(request.getParameter("find").equals("onload"))
			{
				System.out.println("select * from lease_table where addr='"+request.getParameter("addr")+"'");
				sql="select * from lease_table where addr='"+request.getParameter("addr")+"'";
			}
			else 
				sql="select * from lease_table where name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")+"'";
		else
			if(request.getParameter("find").equals("onload"))
				sql="select * from purchase_table where addr='"+request.getParameter("addr")+"'";
			else
				sql="select * from purchase_table where name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")+"'";
		try {
			mysql.sqlQuery(sql);
			while(mysql.getRs().next())
			{
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("photo", mysql.getRs().getString("photo"));
				jsonObject.put("name", mysql.getRs().getString("name"));
				jsonObject.put("introduction", mysql.getRs().getString("introduction"));
				jsonObject.put("id", mysql.getRs().getString("id"));
				jsonObject.put("seller_tel", mysql.getRs().getString("seller_tel"));
				jsonObject.put("price", mysql.getRs().getString("price"));
				jsonArray.add(jsonObject);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		out.write(jsonArray.toString());		
	}
}
