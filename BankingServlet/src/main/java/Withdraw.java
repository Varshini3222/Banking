

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Withdraw
 */
@WebServlet("/Withdraw")
public class Withdraw extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Withdraw() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
        int id = Integer.parseInt(request.getParameter("accid"));
        int amount = Integer.parseInt(request.getParameter("bal"));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankservlet", "root", "tiger");

            // Fetch current balance
            PreparedStatement ps1 = con.prepareStatement("SELECT bal FROM account WHERE accid = ?");
            ps1.setInt(1, id);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                int currentBalance = rs.getInt("bal");
                if (currentBalance >= amount) {
                    int newBalance = currentBalance - amount;

                    // Update account balance
                    PreparedStatement ps2 = con.prepareStatement("UPDATE account SET bal = ? WHERE accid = ?");
                    ps2.setInt(1, newBalance);
                    ps2.setInt(2, id);
                    ps2.executeUpdate();

                    // Record transaction
                    PreparedStatement ps3 = con.prepareStatement("INSERT INTO transactions (accid, type, amt, date) VALUES (?, 'withdraw', ?, NOW())");
                    ps3.setInt(1, id);
                    ps3.setInt(2, amount);
                    int rowsAffected = ps3.executeUpdate();
                    if (rowsAffected > 0) {
                        out.println("Withdrawal successful. New balance: " + newBalance);
                    } else {
                        out.println("Transaction recording failed.");
                    }
                } else {
                    out.println("Insufficient balance.");
                }
            } else {
                out.println("Account not found.");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error: " + e.getMessage());
        }
    }

}
