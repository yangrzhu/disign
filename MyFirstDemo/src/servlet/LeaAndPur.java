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
		String sql="";
		if(request.getParameter("flag").equals("lease"))
		{
			if(request.getParameter("choose_order").equals("time"))
			{
				if(request.getParameter("time_order").equals("0"))
					sql="select * from lease_table where name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")
					+"'";
				else if(request.getParameter("time_order").equals("1"))
					sql="select * from lease_table where name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")
					+"' order by time asc";
				else
					sql="select * from lease_table where name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")
					+"' order by time desc";
			}
			else if(request.getParameter("choose_order").equals("heat"))
			{
				if(request.getParameter("heat_order").equals("0"))
					sql="select * from lease_table where name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")
					+"'";
				else if(request.getParameter("heat_order").equals("1"))
					sql="select * from lease_table where name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")
					+"' order by frequency asc";
				else
					sql="select * from lease_table where name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")
					+"' order by frequency desc";
			}
			else
			{
				if(request.getParameter("price_order").equals("0"))
					sql="select * from lease_table where name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")
					+"'";
				else if(request.getParameter("price_order").equals("1"))
					sql="select * from lease_table where name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")
					+"' order by price asc";
				else
					sql="select * from lease_table where name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")
					+"' order by price desc";
			}
		}
		else
		{
			if(request.getParameter("choose_order").equals("time"))
			{
				if(request.getParameter("time_order").equals("0"))
					sql="select * from purchase_table where frequency>0 and name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")
					+"'";
				else if(request.getParameter("time_order").equals("1"))
					sql="select * from purchase_table where frequency>0 and name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")
					+"' order by time asc";
				else
					sql="select * from purchase_table where frequency>0 and name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")
					+"' order by time desc";
			}
			else
			{
				if(request.getParameter("price_order").equals("0"))
					sql="select * from purchase_table where frequency>0 and name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")
					+"'";
				else if(request.getParameter("price_order").equals("1"))
					sql="select * from purchase_table where frequency>0 and name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")
					+"' order by price asc";
				else
					sql="select * from purchase_table where frequency>0 and name like '%"+request.getParameter("name")+"%' and addr='"+request.getParameter("addr")
					+"' order by price desc";
			}
		}
		try {
			System.out.println(sql);
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
				jsonObject.put("addr", mysql.getRs().getString("addr"));
				jsonArray.add(jsonObject);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		out.write(jsonArray.toString());		
	}
}
