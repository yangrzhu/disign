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

/**
 * Servlet implementation class LogAndReg
 */
@WebServlet("/LogAndReg")
public class LogAndReg extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogAndReg() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("flag").equals("login"))
			login(request,response);
		else
			register(request,response);
	}
	private void login(HttpServletRequest request, HttpServletResponse response)
	{
		DataBase myuser=new DataBase();
		myuser.connect();
		String sql="select * from user where tel='"+request.getParameter("tel")+"'";
		try {
			PrintWriter out=response.getWriter();
			myuser.sqlQuery(sql);
			if(myuser.getRs().next())
			{
				if(myuser.getRs().getString("password").equals(request.getParameter("password")))
				{
					out.println("success");
					System.out.println("登录成功!");
					request.getSession().setAttribute("user", request.getParameter("tel"));
					//response.sendRedirect("../myhtml/lease.html");					
				}
				else
				{
					out.println("error1");
					System.out.println("账号或密码错误!");
				}
			}
			else
			{
				out.println("error2");
				System.out.println("改账号还没被注册！");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
	private void register(HttpServletRequest request, HttpServletResponse response)
	{
		DataBase myuser=new DataBase();
		myuser.connect();
		String sql="select * from user where tel='"+request.getParameter("tel")+"'";
		try {
			PrintWriter out=response.getWriter();
			myuser.sqlQuery(sql);
			if(myuser.getRs().next())
			{
				out.println("error");
				System.out.println("改账号已经被注册！");
				
			}
			else
			{
				String str="insert into user (tel,password) values ('"+request.getParameter("tel")+"','"+request.getParameter("password")+"')";
				int n=myuser.sqlUpdate(str);
				if(n==1)
				{
					out.println("success");
					System.out.println("注册成功!");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
