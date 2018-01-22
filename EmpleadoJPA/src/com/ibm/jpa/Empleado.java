package com.ibm.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity
//@Table(name="EMPLOYEE", schema = "SAMP")
public class Empleado implements Serializable 
{
//	@Id
//	@Column(name="EMPNO")
	private String Id;
//	@Column(name="FIRSTNME")
	private String Nombre;
//	@Column(name="LASTNAME")
	private String Apellidos;
	private static final long serialVersionUID = 1L;

	public Empleado() 
	{
		super();
	}
	
	public String getId() 
	{
		return this.Id;
	}

	public void setId(String Id) 
	{
		this.Id = Id;
	}
	
	public String getNombre() 
	{
		return this.Nombre;
	}

	public void setNombre(String Nombre) 
	{
		this.Nombre = Nombre;
	}   
	
	public String getApellidos() 
	{
		return this.Apellidos;
	}

	public void setApellidos(String Apellidos) 
	{
		this.Apellidos = Apellidos;
	}  
}