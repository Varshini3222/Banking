

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Bank
 */
@WebServlet("/Bank")
public class Bank extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Bank() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out=response.getWriter();
		int id=Integer.parseInt(request.getParameter("t1"));
		out.println("UserID is:"+id);
		String name=request.getParameter("t2");
		out.println("User name is:"+name);
		int bal=Integer.parseInt(request.getParameter("t3"));
		out.println("Deposited salary is:"+bal);
		
		
		try {
			//1.loading driver class
			Class.forName("com.mysql.cj.jdbc.Driver");
			out.println("Driver class loaded");
			
			//2.establish a connection
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bankservlet","root","tiger");
			out.println("Connected to DB");
			
			//3.Prepare a statement
			PreparedStatement ps=con.prepareStatement("insert into account values(?,?,?)");
			ps.setInt(1,id);
			ps.setString(2, name);
			ps.setInt(3, bal);
			
			//4.submit query to DB
			int i=ps.executeUpdate();
			if(i>0)
				out.println("Record inserted");
			else
				out.println("Not inserted");
			
			//5.close the connection
			con.close();
			out.println("Connection released");
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
