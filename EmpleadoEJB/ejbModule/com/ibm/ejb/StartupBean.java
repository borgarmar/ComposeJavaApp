package com.ibm.ejb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.postgresql.ds.PGPoolingDataSource;

import com.ibm.json.java.JSON;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.ibm.util.Constants;

@Startup
@Singleton
public class StartupBean 
{
	@PostConstruct
	void init()
	{
		// Check which type of Data Base is bound
		System.out.println("#### Checking which type of Data Base is bound...");
		String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
		
		if (VCAP_SERVICES != null)
		{
			JSONObject vcap;
			JSONObject service;
			JSONObject credentials;

			try 
			{
				vcap = (JSONObject) JSON.parse(VCAP_SERVICES);
				if(vcap.get(Constants.POSTGRESQL) != null)
				{
					System.out.println("#### Creating PostgreSQL DataSource...");
					
					service = (JSONObject) ((JSONArray) vcap.get(Constants.POSTGRESQL)).get(0);
					credentials = (JSONObject)service.get("credentials");
					
					String uri = (String) credentials.get("uri");
					String user = uri.substring(uri.indexOf("postgres://")+11,uri.indexOf(":",11));
					String password = uri.substring(uri.indexOf(":",11)+1,uri.indexOf("@"));
					String host = uri.substring(uri.indexOf("@")+1,uri.indexOf(":",uri.indexOf("@")));
					String port = uri.substring(uri.indexOf(":",uri.indexOf("@"))+1,uri.indexOf("/",uri.indexOf("@")));
					String name = uri.substring(uri.indexOf("/",uri.indexOf("@"))+1);

					Context initialContext = new InitialContext();
				    //initialContext.createSubcontext("jdbc"); Already created by Bluemix when using PostgreSQL
				    PGPoolingDataSource datasource = new PGPoolingDataSource();
				    datasource.setServerName(host);
				    datasource.setPortNumber(Integer.parseInt(port));
				    datasource.setUser(user);
				    datasource.setPassword(password);
				    datasource.setDatabaseName(name);
				    initialContext.bind("jdbc/SampleDB", datasource);	
					    
				    initSQLDataBase();
				}
				else
				{
					System.out.println("#### Unkown Data Base bounded...");
				}
			}
			catch(IOException ex) 
			{
				ex.printStackTrace();
			}
		    catch(NamingException ex) 
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
		}
	}

	private void initSQLDataBase() 
	{
		DataSource datasource = null;
		Connection con = null;
		DatabaseMetaData metadata = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BufferedReader br = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try 
		{
			Context initialContext = new InitialContext();
			datasource = (DataSource)initialContext.lookup("jdbc/SampleDB");
			con = datasource.getConnection();
			metadata = con.getMetaData();
			rs = metadata.getTables(null, null, "employee", null); // PostgreSQL by default cretaes tables using lowercase
			if(!rs.next())
			{
				System.out.println("#### Creating the schema SAMP...");
				pstmt = con.prepareStatement("CREATE SCHEMA SAMP");
				pstmt.executeUpdate();
				pstmt.close();

				System.out.println("#### Creating the table SAMP.EMPLOYEE...");
				pstmt = con.prepareStatement("CREATE TABLE SAMP.EMPLOYEE (EMPNO CHAR(6) NOT NULL, FIRSTNME VARCHAR(12) NOT NULL, MIDINIT CHAR(1) NOT NULL, LASTNAME VARCHAR(15) NOT NULL, WORKDEPT CHAR(3), PHONENO CHAR(4), HIREDATE DATE, JOB CHAR(8), EDLEVEL SMALLINT NOT NULL, SEX CHAR(1), BIRTHDATE DATE, SALARY DECIMAL(9 , 2), BONUS DECIMAL(9 , 2), COMM DECIMAL(9 , 2))");
				pstmt.executeUpdate();
				pstmt.close();

				System.out.println("#### Creating index EMP_IDX...");
				pstmt = con.prepareStatement("CREATE UNIQUE INDEX EMP_IDX ON SAMP.EMPLOYEE (EMPNO ASC)");
				pstmt.executeUpdate();
				pstmt.close();

				System.out.println("#### Inserting sample data into SAMP.EMPLOYEE...");
				pstmt = con.prepareStatement("INSERT INTO SAMP.EMPLOYEE (EMPNO, FIRSTNME, MIDINIT, LASTNAME, WORKDEPT, PHONENO, HIREDATE, JOB, EDLEVEL, SEX, BIRTHDATE, SALARY, BONUS, COMM) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				br = new BufferedReader(new InputStreamReader(StartupBean.class.getClassLoader().getResourceAsStream("resources/data.csv")));
				String tmp = null;
				while((tmp = br.readLine()) != null)
				{
					StringTokenizer st = new StringTokenizer(tmp, ",");
					while(st.hasMoreTokens())
					{
						pstmt.setString(1, st.nextToken());
						pstmt.setString(2, st.nextToken());
						pstmt.setString(3, st.nextToken());
						pstmt.setString(4, st.nextToken());
						pstmt.setString(5, st.nextToken());
						pstmt.setString(6, st.nextToken());
						pstmt.setDate(7, new Date(sdf.parse(st.nextToken()).getTime()));
						pstmt.setString(8, st.nextToken());
						pstmt.setInt(9, Integer.parseInt(st.nextToken()));
						pstmt.setString(10, st.nextToken());
						pstmt.setDate(11, new Date(sdf.parse(st.nextToken()).getTime()));
						pstmt.setFloat(12, Float.parseFloat(st.nextToken()));						
						pstmt.setFloat(13, Float.parseFloat(st.nextToken()));
						pstmt.setFloat(14, Float.parseFloat(st.nextToken()));
					}
					pstmt.addBatch();
				}
				pstmt.executeBatch();
				pstmt.close();
			}
			else
			{
				System.out.println("#### SAMP.EMPLOYEE table already exists...");
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
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		catch(ParseException ex)
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
			if(br != null) 
			{
				try 
				{
					br.close();
				}
				catch(IOException ex) 
				{
					ex.printStackTrace();
				}
			}
		}
	}
}