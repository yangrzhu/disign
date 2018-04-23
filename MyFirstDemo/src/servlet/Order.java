package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DataBase;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class Order
 */
@WebServlet("/Order")
public class Order extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Order() {
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
		String sql,sql2;
		if(request.getParameter("action").equals("get"))
		{
			if(request.getParameter("flag").equals("leaseover"))
				sql="select * from lease_order where seller_tel='"+request.getSession().getAttribute("user")+"' and seller_delete=0 and verify is not null"
					+ " or buyer_tel='"+request.getSession().getAttribute("user")+"' and buyer_delete=0 and verify is not null";
			else if(request.getParameter("flag").equals("leaseget"))
				sql="select * from lease_order where buyer_tel='"+request.getSession().getAttribute("user")+"' and verify is null";
			else if(request.getParameter("flag").equals("leasegive"))
				sql="select * from lease_order where seller_tel='"+request.getSession().getAttribute("user")+"' and verify is null";
			else if(request.getParameter("flag").equals("purchaseover"))
				sql="select * from purchase_order where seller_tel='"+request.getSession().getAttribute("user")+"' and seller_delete=0 and verify is not null"
					+ " or buyer_tel='"+request.getSession().getAttribute("user")+"' and buyer_delete=0 and verify is not null";
			else if(request.getParameter("flag").equals("purchaseget"))
				sql="select * from purchase_order where buyer_tel='"+request.getSession().getAttribute("user")+"' and verify is null";
			else
				sql="select * from purchase_order where seller_tel='"+request.getSession().getAttribute("user")+"' and verify is null";
			try {
				mysql.sqlQuery(sql);
				while(mysql.getRs().next())
				{
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("id", mysql.getRs().getString("id"));
					jsonObject.put("photo", mysql.getRs().getString("photo"));
					jsonObject.put("name", mysql.getRs().getString("name"));
					jsonObject.put("introduction", mysql.getRs().getString("introduction"));
					jsonObject.put("buyer_tel", mysql.getRs().getString("buyer_tel"));
					jsonObject.put("seller_tel", mysql.getRs().getString("seller_tel"));
					jsonObject.put("price", mysql.getRs().getString("price"));
					jsonObject.put("time", mysql.getRs().getString("time"));
					jsonObject.put("verify", mysql.getRs().getString("verify"));
					jsonArray.add(jsonObject);
				}
			} catch (SQLException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			out.write(jsonArray.toString());
		}
		else if(request.getParameter("action").equals("generate"))
		{
			SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(request.getParameter("flag").equals("lease_now"))
			{		
				sql="insert into lease_order (photo,name,introduction,price,buyer_tel,seller_tel,time) values('"
				+request.getParameter("photo")+"','"+request.getParameter("name")+"','"+request.getParameter("introduction")
				+"','"+request.getParameter("price")+"','"+request.getSession().getAttribute("user")+"','"+request.getParameter("seller_tel")
				+"','"+dateformat.format(new Date())+"')";
				out.write("下单成功，等待处理");
			}
			else if(request.getParameter("flag").equals("purchase_now"))
			{
				try {
					mysql.sqlUpdate("update purchase_table set time='"+dateformat.format(new Date())+"' where id="+request.getParameter("goods_id"));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sql="insert into purchase_order (photo,name,introduction,price,buyer_tel,seller_tel,time,goods_id) values('"
				+request.getParameter("photo")+"','"+request.getParameter("name")+"','"+request.getParameter("introduction")
				+"','"+request.getParameter("price")+"','"+request.getSession().getAttribute("user")+"','"+request.getParameter("seller_tel")
				+"','"+dateformat.format(new Date())+"',"+request.getParameter("goods_id")+")";
				out.write("下单成功，等待处理");
			}
			else if(request.getParameter("flag").equals("purchase_verify"))
			{
				if(request.getParameter("verify").equals("1"))
				{
					try {
						mysql.sqlQuery("select * from purchase_order where id="+request.getParameter("id"));
						if(mysql.getRs().next())
							mysql.sqlUpdate("update purchase_table set number=number-1 where id="+mysql.getRs().getString("goods_id"));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				sql="update purchase_order set verify="+request.getParameter("verify")+",buyer_delete=0,seller_delete=0 where id="+request.getParameter("id");
				out.write("处理成功");
			}
			else
			{
				if(request.getParameter("verify").equals("1"))
				{
					try {
						mysql.sqlUpdate("update lease_table set frequency=frequency+1,time='"+dateformat.format(new Date())+"' where id="+request.getParameter("goods_id"));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}		
				sql="update lease_order set verify="+request.getParameter("verify")+",buyer_delete=0,seller_delete=0 where id="+request.getParameter("id");
				System.out.println(sql);
				out.write("处理成功");
			}
			try {
				mysql.sqlUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else
		{
			if(request.getParameter("flag").equals("lease"))
			{
				sql="update lease_order set buyer_delete=1 where id="+request.getParameter("id")+" and buyer_tel='"
				+request.getSession().getAttribute("user")+"'";
				sql2="update lease_order set seller_delete=1 where id="+request.getParameter("id")+" and seller_tel='"
						+request.getSession().getAttribute("user")+"'";
			}
			else
			{
				sql="update purchase_order set buyer_delete=1 where id="+request.getParameter("id")+" and buyer_tel='"
				+request.getSession().getAttribute("user")+"'";
				sql2="update purchase_order set seller_delete=1 where id="+request.getParameter("id")+" and seller_tel='"
						+request.getSession().getAttribute("user")+"'";
			}
			try {
				mysql.sqlUpdate(sql);
				mysql.sqlUpdate(sql2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.write("删除成功");
		}
		//SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//System.out.println(dateformat.format(new Date()));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
