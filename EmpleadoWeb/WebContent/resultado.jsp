<%@page import="com.ibm.jpa.Empleado"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>EmpleadoApp</title>

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
	<link rel="stylesheet" href="css/style.css" >
</head>
<body style="background-color: #f5f7fa; color: black">
<nav class="navbar ">
  <a class="navbar-brand" >
    <img src="imagenes/cloud.png" width="30" height="30" class="d-inline-block align-top" alt="">
     &nbsp; IBM Cloud - Java +  Compose for PostgreeSQL
  </a>
</nav>
<div class="container">


<%
  		Empleado emp = (Empleado)request.getAttribute("empleado");
		if(emp != null)
		{
%>
		<p>
		<h1><%= emp.getNombre() %> <%= emp.getApellidos() %></h1>
		</p>
<%
		}
		else
		{
%>
		<p>
		<h1>No existe empleado con ese Id.</h1>
		</p>
<%
		}
%>

<p>
	<a href="/index.html">Realizar nueva búsqueda...</a>
</p>
<br/><br/>

</div>
<footer class="footer">
    <div class="container">
      <span class="text-muted text-center">Application developed by Chemi Ordax</span>
    </div>
  </footer>
</body>
</html>