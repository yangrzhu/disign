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
 * Servlet implementation class Product
 */
@WebServlet("/Product")
public class Product extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Product() {
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
		if(request.getParameter("flag").equals("lease")||request.getParameter("flag").equals("purchase"))
		{
			try {
				Rel_goods(request,response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(request.getParameter("flag").equals("mygoods_lease")||request.getParameter("flag").equals("mygoods_purchase"))
		{
			getMyGoods(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	private void Rel_goods(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException
	{
		PrintWriter out=response.getWriter();
		DataBase mysql=new DataBase();
		mysql.connect();
		if(request.getParameter("flag").equals("lease"))
		{
			mysql.sqlUpdate("insert into lease_table (photo,seller_tel,name,introduction,price) values ('1','1','"
			+request.getParameter("name")+"','"+request.getParameter("introduction")+"','"+request.getParameter("price")+"')");
			mysql.sqlQuery("select * from lease_table where photo='1'");
			if(mysql.getRs().next())
				request.getSession().setAttribute("id", mysql.getRs().getString("id"));
		}
		if(request.getParameter("flag").equals("purchase"))
		{
			mysql.sqlUpdate("insert into purchase_table (photo,seller_tel,name,introduction,price) values ('1','1','"
			+request.getParameter("name")+"','"+request.getParameter("introduction")+"','"+request.getParameter("price")+"')");
			mysql.sqlQuery("select * from purchase_table where photo='1'");
			if(mysql.getRs().next())
				request.getSession().setAttribute("id", mysql.getRs().getString("id"));
		}
		out.write("发布成功");
	}
	private void getMyGoods(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		PrintWriter out=response.getWriter();
		JSONArray jsonArray=new JSONArray();
		DataBase mysql=new DataBase();
		mysql.connect();
		String sql;
		if(request.getParameter("flag").equals("mygoods_lease"))
			sql="select * from lease_table where seller_tel='"+request.getSession().getAttribute("user")+"'";
		else
			sql="select * from purchase_table where seller_tel='"+request.getSession().getAttribute("user")+"'";
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
