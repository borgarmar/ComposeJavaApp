package com.ibm.servlet;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.ejb.BuscadorLocal;
import com.ibm.jpa.Empleado;

@WebServlet("/Controlador")
public class Controlador extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
	@EJB
	BuscadorLocal ejb;
	
    public Controlador() 
    {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String id = request.getParameter("id");
		Empleado emp = ejb.buscar(id);
		request.setAttribute("empleado", emp);
		request.getRequestDispatcher("/resultado.jsp").forward(request, response);
	}
}