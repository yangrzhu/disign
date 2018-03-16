package servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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
 * Servlet implementation class Upload
 */
@WebServlet("/Upload")
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Upload() {
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
		PrintWriter out=response.getWriter();
		//Ϊ�������ṩ������Ϣ
		DiskFileItemFactory factory=new DiskFileItemFactory();
		//�����������ʵ��
		ServletFileUpload sfu=new ServletFileUpload(factory);
		//��ʼ������ÿ�����������ݻ��װ��һ����Ӧ��FileItem������
		//sfu.setFileSizeMax(1024*400);
		List<FileItem> items;
		try {
			items = sfu.parseRequest(request);
			DataBase mysql=new DataBase();
			mysql.connect();
			if(items.get(items.size()-3).getString().equals("information"))
			{
				@SuppressWarnings("deprecation")
				String projectPath=request.getRealPath("");
				//String projectPath=request.getContextPath();
				String fileName=items.get(0).getName();
				int index=fileName.lastIndexOf(".");
				String pho_name=fileName.substring(0,index)+System.currentTimeMillis()+fileName.substring(index);
				String path=projectPath+"\\photo\\"+pho_name;
				System.out.println(path);
				FileOutputStream output=new FileOutputStream(path);
				output.write(items.get(0).get());
				output.flush();
				output.close();
				String sql="update user set photo='../photo/"+pho_name+"' where tel='"+request.getSession().getAttribute("user")+"'";
				mysql.sqlUpdate(sql);
				out.write("�༭�ɹ�");
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
				System.out.println(path);
				FileOutputStream output=new FileOutputStream(path);
				output.write(items.get(0).get());
				output.flush();
				output.close();
				String sql;
				if(items.get(items.size()-2).getString().equals("0"))
					sql="update lease_table set photo='../photo/"+pho_name+"',seller_tel='"+request.getSession().getAttribute("user")
						+"' where id='"+request.getSession().getAttribute("id")+"'";
				else
					sql="update lease_table set photo='../photo/"+pho_name+"' where id="+items.get(items.size()-1).getString();
				mysql.sqlUpdate(sql);
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
				System.out.println(path);
				FileOutputStream output=new FileOutputStream(path);
				output.write(items.get(0).get());
				output.flush();
				output.close();
				String sql;
				if(items.get(items.size()-2).getString().equals("0"))
					sql="update purchase_table set photo='../photo/"+pho_name+"',seller_tel='"+request.getSession().getAttribute("user")
				+"' where id='"+request.getSession().getAttribute("id")+"'";
				else
					sql="update purchase_table set photo='../photo/"+pho_name+"' where id="+items.get(items.size()-1).getString();
				mysql.sqlUpdate(sql);
			}
			/*//���ֱ���
			for(int i=0;i<items.size();i++)
			{
				FileItem item=items.get(i);
				//ifFormFileΪtrue����ʾ�ⲻ���ļ��ϴ�����
				//�ļ��ϴ���			
				if(!item.isFormField())
				{
					System.out.println("�ļ��ı���");
					@SuppressWarnings("deprecation")
					String projectPath=request.getRealPath("");
					//String projectPath=request.getContextPath();
					String fileName=item.getName();
					int index=fileName.lastIndexOf(".");
					String path=projectPath+"\\photo\\"+fileName.substring(0,index)+System.currentTimeMillis()+fileName.substring(index);
					System.out.println(path);
					FileOutputStream output=new FileOutputStream(path);
					output.write(item.get());
					output.flush();
					output.close();
					item.delete();
					out.write("�ϴ��ɹ�");
				}
				//��ͨ����
				else
				{
					System.out.println("��ͨ�ı���");
					System.out.println(item.getFieldName()+"  "+item.getString());
				}
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
