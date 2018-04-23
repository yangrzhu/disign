package servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import dao.DataBase;

/**
 * Servlet implementation class Text
 */
@WebServlet("/Text")
public class Text extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Text() {
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
				//为解析类提供配置信息
				DiskFileItemFactory factory=new DiskFileItemFactory();
				//创建解析类的实例
				ServletFileUpload sfu=new ServletFileUpload(factory);
				//开始解析，每个表单域中数据会封装到一个对应的FileItem对象上
				//sfu.setFileSizeMax(1024*400);
				List<FileItem> items;
				try {
					items = sfu.parseRequest(request);
					DataBase mysql=new DataBase();
					mysql.connect();
					@SuppressWarnings("deprecation")
					String projectPath=request.getRealPath("");
					//String projectPath=request.getContextPath();
					String fileName=items.get(0).getName();
					int index=fileName.lastIndexOf(".");
					String pho_name=fileName.substring(0,index)+System.currentTimeMillis()+fileName.substring(index);
					String path=projectPath+"\\photo\\"+pho_name;
					FileOutputStream output=new FileOutputStream(path);
					output.write(items.get(0).get());
					output.flush();
					output.close();
					String sql;
					sql="insert into new_table (photo,name,introduction,price) values('../photo/"+pho_name+"','"+items.get(1).getString()+"','"
							+items.get(2).getString()+"',"+items.get(3).getString()+")";
					mysql.sqlUpdate(sql);
					/*if(items.get(items.size()-3).getString().equals("information"))
					{
						String sql;
						if(items.get(0).getSize()==0)
							sql="update user set photo=null where tel='"+request.getSession().getAttribute("user")+"'";
						else
						{
							@SuppressWarnings("deprecation")
							String projectPath=request.getRealPath("");
							//String projectPath=request.getContextPath();
							String fileName=items.get(0).getName();
							int index=fileName.lastIndexOf(".");
							String pho_name=fileName.substring(0,index)+System.currentTimeMillis()+fileName.substring(index);
							String path=projectPath+"\\photo\\"+pho_name;
							FileOutputStream output=new FileOutputStream(path);
							output.write(items.get(0).get());
							output.flush();
							output.close();
							sql="update user set photo='../photo/"+pho_name+"' where tel='"+request.getSession().getAttribute("user")+"'";
						}
						mysql.sqlUpdate(sql);
					}
					if(items.get(items.size()-3).getString().equals("lease"))
					{
						@SuppressWarnings("deprecation")
						String projectPath=request.getRealPath("");
						//String projectPath=request.getContextPath();
						String fileName=items.get(0).getName();
						int index=fileName.lastIndexOf(".");
						String pho_name=fileName.substring(0,index)+System.currentTimeMillis()+fileName.substring(index);
						String path=projectPath+"\\photo\\"+pho_name;
						FileOutputStream output=new FileOutputStream(path);
						output.write(items.get(0).get());
						output.flush();
						output.close();
						String sql;
						if(items.get(items.size()-2).getString().equals("0"))
							sql="update lease_table set photo='../photo/"+pho_name+"',seller_tel='"+request.getSession().getAttribute("user")
								+"' where seller_tel='1'";
						else
							sql="update lease_table set photo='../photo/"+pho_name+"' where id="+items.get(items.size()-1).getString();
						System.out.println(sql);
						System.out.println(mysql.sqlUpdate(sql));
					}
					if(items.get(items.size()-3).getString().equals("purchase"))
					{
						@SuppressWarnings("deprecation")
						String projectPath=request.getRealPath("");
						//String projectPath=request.getContextPath();
						String fileName=items.get(0).getName();
						int index=fileName.lastIndexOf(".");
						String pho_name=fileName.substring(0,index)+System.currentTimeMillis()+fileName.substring(index);
						String path=projectPath+"\\photo\\"+pho_name;
						FileOutputStream output=new FileOutputStream(path);
						output.write(items.get(0).get());
						output.flush();
						output.close();
						String sql;
						if(items.get(items.size()-2).getString().equals("0"))
							sql="update purchase_table set photo='../photo/"+pho_name+"',seller_tel='"+request.getSession().getAttribute("user")
						+"' where seller_tel='1'";
						else
							sql="update purchase_table set photo='../photo/"+pho_name+"' where id="+items.get(items.size()-1).getString();
						System.out.println(sql);
						System.out.println(mysql.sqlUpdate(sql));
					}*/
				} catch (FileUploadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
