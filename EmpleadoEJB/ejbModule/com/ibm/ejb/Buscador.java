package com.ibm.ejb;

import javax.ejb.Stateless;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;

import com.ibm.jpa.Empleado;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@Stateless
public class Buscador implements BuscadorLocal 
{
	//@PersistenceContext(unitName="EmpleadoJPA")
	//EntityManager em;
	
    public Buscador() 
    {
    }
    
    public Empleado buscar(String id)
    {
    	System.out.println("#### Searching employee with Id: " + id + "...");
    	//Empleado emp = (Empleado) em.find(Empleado.class, id);
    	
    	Empleado emp = null;
    	
    	DataSource datasource = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
    	try 
		{
			Context initialContext = new InitialContext();
			datasource = (DataSource)initialContext.lookup("jdbc/SampleDB");
			con = datasource.getConnection();
			
			pstmt = con.prepareStatement("SELECT FIRSTNME, LASTNAME FROM SAMP.EMPLOYEE WHERE EMPNO = ?");
			pstmt.setString(1,id);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				emp = new Empleado();
				emp.setNombre(rs.getString(1));
				emp.setApellidos(rs.getString(2));
			}
		}
		catch(NamingException ex)
		{
			ex.printStackTrace();
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		catch(Throwable ex)
		{
			ex.printStackTrace();
		}			
		finally 
		{
			if(pstmt != null) 
			{
				try 
				{
					pstmt.close();
				}
				catch(SQLException ex) 
				{
					ex.printStackTrace();
				}
			}
			if(con != null) 
			{
				try 
				{
					con.close();
				}
				catch(SQLException ex) 
				{
					ex.printStackTrace();
				}
			}
		}
    	
    	return emp;
    }
}