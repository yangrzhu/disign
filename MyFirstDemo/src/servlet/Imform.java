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
 * Servlet implementation class Imform
 */
@WebServlet("/Imform")
public class Imform extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Imform() {
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
		// TODO Auto-generated method stub;	
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		if(request.getParameter("flag").equals("photo"))
		{
			try {
				getPhoto(request,response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(request.getParameter("flag").equals("logout"))		
			Logout(request,response);
		else if(request.getParameter("flag").equals("repassword"))
		{
			try {
				Repassword(request,response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(request.getParameter("flag").equals("information"))
		{
			try {
				Change_Inform(request,response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(request.getParameter("flag").equals("lease")||request.getParameter("flag").equals("purchase"))
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
	private void getPhoto(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException
	{
		PrintWriter out=response.getWriter();
		DataBase mysql=new DataBase();
		mysql.connect();
		String sql="select * from user where tel='"+request.getSession().getAttribute("user")+"'";
		mysql.sqlQuery(sql);
		if(mysql.getRs().next())
		{
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("photo", mysql.getRs().getString("photo"));
			jsonObject.put("name", mysql.getRs().getString("name"));
			jsonObject.put("tel", mysql.getRs().getString("tel"));
			out.write(jsonObject.toString());	
		}
		else
			out.write("error");
	}
	private void Repassword(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException
	{
		PrintWriter out=response.getWriter();
		DataBase mysql=new DataBase();
		mysql.connect();
		String sql="select * from user where tel='"+request.getSession().getAttribute("user")+"'";
		mysql.sqlQuery(sql);
		if(mysql.getRs().next())
		{
			if(request.getParameter("old_pass").equals(mysql.getRs().getString("password")))
			{
				String sql2="update user set password='"+request.getParameter("new_pass")+"' where tel='"+request.getSession().getAttribute("user")+"'";
				int flag=mysql.sqlUpdate(sql2);
				if(flag!=0)
				{
					out.write("修改成功");
					request.getSession().setAttribute("user", null);
				}
			}
			else
				out.write("原密码错误");
		}
		else
			out.write("error");
	}
	private void Logout(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		request.getSession().setAttribute("user", null);
		PrintWriter out=response.getWriter();
		out.write("退出登录!");
	}
	private void Rel_goods(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException
	{
		PrintWriter out=response.getWriter();
		DataBase mysql=new DataBase();
		mysql.connect();
		if(request.getParameter("flag").equals("lease"))
		{
			if(request.getParameter("isRe_edit").equals("0"))
			{
				mysql.sqlUpdate("insert into lease_table (photo,seller_tel,name,introduction,price,addr) values ('1','1','"
						+request.getParameter("name")+"','"+request.getParameter("introduction")+"','"+request.getParameter("price")+"','"+request.getParameter("addr")+"')");
				mysql.sqlQuery("select * from lease_table where photo='1'");
				if(mysql.getRs().next())
					request.getSession().setAttribute("id", mysql.getRs().getString("id"));
				out.write("发布成功");
			}
			else if(request.getParameter("isRe_edit").equals("1"))
			{
				mysql.sqlUpdate("update lease_table set name='"+request.getParameter("name")+"',introduction='"+request.getParameter("introduction")
				+"',price='"+request.getParameter("price")+"',addr='"+request.getParameter("addr")+"' where id="+request.getParameter("reEdit_id"));
				out.write("发布成功");
			}
			else
			{
				mysql.sqlUpdate("delete from lease_table where id="+request.getParameter("reEdit_id"));
				out.write("删除成功");
			}
		}
		if(request.getParameter("flag").equals("purchase"))
		{
			if(request.getParameter("isRe_edit").equals("0"))
			{
				mysql.sqlUpdate("insert into purchase_table (photo,seller_tel,name,introduction,price,addr) values ('1','1','"
						+request.getParameter("name")+"','"+request.getParameter("introduction")+"','"+request.getParameter("price")+"','"+request.getParameter("addr")+"')");
				mysql.sqlQuery("select * from purchase_table where photo='1'");
				if(mysql.getRs().next())
					request.getSession().setAttribute("id", mysql.getRs().getString("id"));
				out.write("发布成功");
			}
			else if(request.getParameter("isRe_edit").equals("1"))
			{
				mysql.sqlUpdate("update purchase_table set name='"+request.getParameter("name")+"',introduction='"+request.getParameter("introduction")
				+"',price='"+request.getParameter("price")+"',addr='"+request.getParameter("addr")+"' where id="+request.getParameter("reEdit_id"));
				out.write("发布成功");
			}
			else
			{
				mysql.sqlUpdate("delete from purchase_table where id="+request.getParameter("reEdit_id"));
				out.write("删除成功");
			}
		}

	}
	private void Change_Inform(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException
	{
		System.out.println(request.getParameter("sex"));
		PrintWriter out=response.getWriter();
		DataBase mysql=new DataBase();
		mysql.connect();
		if(request.getParameter("name").length()!=0)
			mysql.sqlUpdate("update user set name='"+request.getParameter("name")+"' where tel='"+request.getSession().getAttribute("user")+"'");
		if(request.getParameter("sex").length()!=0)
			mysql.sqlUpdate("update user set sex='"+request.getParameter("sex")+"' where tel='"+request.getSession().getAttribute("user")+"'");
		if(request.getParameter("addr").length()!=0)
			mysql.sqlUpdate("update user set addr='"+request.getParameter("addr")+"' where tel='"+request.getSession().getAttribute("user")+"'");
		out.write("编辑成功");
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