package ace.rest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.genericdao.RollbackException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import databeans.Customer;
import databeans.Employee;
import model.CustomerDAO;
import model.EmployeeDAO;
import model.Model;
@Path("/login")
public class Login {

    @POST
    public Response loginAction(@Context HttpServletRequest request,@FormParam("username") String username, @FormParam("password") String password)
            throws ServletException {
    	
    	Model model = new Model();
		Gson gson = new Gson();
		CustomerDAO customerDAO = model.getCustomerDAO();
		EmployeeDAO employeeDAO = model.getEmployeeDAO();
		String message;
		String output;
		
		try {
			// creating default admin
			if (employeeDAO.getByUserName("jadmin") == null) {
				Employee e = new Employee();
				e.setFirstName("Jane");
				e.setLastName("Admin");
				e.setUserName("jadmin");
				e.setPassword(this.encryptPassword("admin"));
				employeeDAO.create(e);
				System.out.println("Default admin account created");
			}
			
			
			Customer customer = customerDAO.getByUserName(username);
            Employee employee = employeeDAO.getByUserName(username);
            
            if (employee != null && employee.getPassword().equals(this.encryptPassword(password))) {
            	request.getSession().setAttribute("employee", employee);
            	message = "Welcome " + employee.getFirstName() + " ";
            	Result res = new Result(message, "employee");
            	return Response.status(200).entity(gson.toJson(res)).build();
            } else if (customer != null && customer.getPassword().equals(this.encryptPassword(password))) {
            	request.getSession().setAttribute("customer", customer);
            	message = "Welcome " + customer.getFirstName() + " ";
            	Result res = new Result(message, "customer");
            	return Response.status(200).entity(gson.toJson(res)).build();
            } else {
            	output = "The username/password combination that you entered is not correct";
            	return Response.status(200).entity(gson.toJson(output)).build();
            }
            
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return Response.status(200).entity("TestFail").build();
    }
    
	private String encryptPassword (String passwordToHash) {
		
        String generatedPassword = null;
        try {
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
        return generatedPassword;
	}
	
    private class Result {
    	String Message;
    	JsonArray Menu;
    	
    	public Result(String s, String selection) {
    		this.Message = s;
    		if (selection.equals("employee"))
    			this.Menu = this.getEmployeeMenu();
    		else
    			this.Menu = this.getCustomerMenu();
    	}
    	
    	public JsonArray getEmployeeMenu() {
    		JsonArray jarray = new JsonArray();
    		JsonObject jobj = new JsonObject();
    		jobj.addProperty("link", "/login");
    		jobj.addProperty("function", "Login");
    		jarray.add(jobj);
    		
    		JsonObject jobj1 = new JsonObject();
    		jobj1.addProperty("link", "/createCustomerAccount");
    		jobj1.addProperty("function", "Create customer account");
    		jarray.add(jobj1);
    		
    		JsonObject jobj2 = new JsonObject();
    		jobj2.addProperty("link", "/depositCheck");
    		jobj2.addProperty("function", "Deposit check");
    		jarray.add(jobj2);
    		
    		JsonObject jobj3 = new JsonObject();
    		jobj3.addProperty("link", "/createFund");
    		jobj3.addProperty("function", "Create fund");
    		jarray.add(jobj3);
    		
    		JsonObject jobj4 = new JsonObject();
    		jobj4.addProperty("link", "/transitionDay");
    		jobj4.addProperty("function", "Transition day");
    		jarray.add(jobj4);
    		
    		JsonObject jobj5 = new JsonObject();
    		jobj5.addProperty("link", "/logout");
    		jobj5.addProperty("function", "Logout");
    		jarray.add(jobj5);
    		
    		return jarray;
    	}
    	
    	public JsonArray getCustomerMenu() {
    		JsonArray jarray = new JsonArray();
    		JsonObject jobj = new JsonObject();
    		jobj.addProperty("link", "/login");
    		jobj.addProperty("function", "Login");
    		jarray.add(jobj);
    		
    		JsonObject jobj1 = new JsonObject();
    		jobj1.addProperty("link", "/viewPortfolio");
    		jobj1.addProperty("function", "View customer portfolio");
    		jarray.add(jobj1);
    		
    		JsonObject jobj2 = new JsonObject();
    		jobj2.addProperty("link", "/buyFund");
    		jobj2.addProperty("function", "Buy fund");
    		jarray.add(jobj2);
    		
    		JsonObject jobj3 = new JsonObject();
    		jobj3.addProperty("link", "/sellFund");
    		jobj3.addProperty("function", "Sell fund");
    		jarray.add(jobj3);
    		
    		JsonObject jobj4 = new JsonObject();
    		jobj4.addProperty("link", "/requestCheck");
    		jobj4.addProperty("function", "Request check");
    		jarray.add(jobj4);
    		
    		JsonObject jobj5 = new JsonObject();
    		jobj5.addProperty("link", "/logout");
    		jobj5.addProperty("function", "Logout");
    		jarray.add(jobj5);
    		
    		return jarray;
    	}
    }
    
    
}

