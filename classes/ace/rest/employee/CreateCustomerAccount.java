package ace.rest.employee;
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
import com.google.gson.GsonBuilder;

import databeans.Customer;
import model.CustomerDAO;
import model.Model;

@Path("/createCustomerAccount")
public class CreateCustomerAccount {

	@POST
    public Response createCustomer(@Context HttpServletRequest request,
    		@FormParam("firstname") String firstname, @FormParam("lastname") String lastname,
    		@FormParam("username") String username, @FormParam("password") String password,
    		@FormParam("addr_line1") String addr_line1, @FormParam("addr_line2") String addr_line2,
    		@FormParam("city") String city, @FormParam("state") String state, @FormParam("zip") String zip)
            throws ServletException {

			Model model = new Model();
			String output;
			Gson gson =  new GsonBuilder().disableHtmlEscaping().create();
			CustomerDAO customerDAO = model.getCustomerDAO();

			// if customer tries this method
			if (request.getSession().getAttribute("customer") != null) {
				output = "I'm sorry you are not authorized to perform that action";
				return Response.status(200).entity(gson.toJson(output)).build();
			}

			// if logged in
			if (request.getSession().getAttribute("employee") == null) {
				output = "You must log in prior to making this request";
				return Response.status(200).entity(gson.toJson(output)).build();
			}

			try {
				// Create customer need to check whether username has existed
				if (customerDAO.getByUserName(username) != null) {
					output = "I'm sorry, there was a problem creating the account";
					return Response.status(200).entity(gson.toJson(output)).build();
				}
				Customer user = new Customer();
				user.setUserName(username);
				user.setPassword(this.encryptPassword(password));
				user.setFirstName(firstname);
				user.setLastName(lastname);
				user.setAddr_line1(addr_line1);
				user.setAddr_line2(addr_line2);
				user.setCity(city);
				user.setState(state);
				user.setZip(zip);
				customerDAO.add(user);
				output = "The account has been successfully created";
				return Response.status(200).entity(gson.toJson(output)).build();

			} catch (RollbackException e) {
				output = "I'm sorry, there was a problem creating the account";
				return Response.status(200).entity(gson.toJson(output)).build();
			}
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
}
