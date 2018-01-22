package com.ibm.ejb;

import javax.ejb.Local;

import com.ibm.jpa.Empleado;

@Local
public interface BuscadorLocal 
{
	Empleado buscar(String id);
}