package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.dao.UserDAO;
import data.entity.User;

/**
 * Servlet implementation class Servlet
 */
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 UserDAO userDAO = new UserDAO();
		 PrintWriter out = response.getWriter();  
         
		 try {
//			User user = userDAO.createUser("name", "name@gmail.com", "password", "Dmitry", "Zhukov");
//			User user = userDAO.createUser(
//					  request.getParameter("name")
//					, request.getParameter("email")
//					, request.getParameter("pass")
//					, request.getParameter("fname")
//					, request.getParameter("sname")
//			);
			out.println(request.getRequestURI());
//			out.println(request.getParameter("name"));
//			out.println(request.getParameter("email"));
//			out.println(request.getParameter("pass"));
//			out.println(request.getParameter("fname"));
//			out.println(request.getParameter("sname"));
//			System.out.println("handle sucsess");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("handle error");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
