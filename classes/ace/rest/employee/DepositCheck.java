package ace.rest.employee;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import com.google.gson.Gson;

import databeans.Customer;
import databeans.MessageWrapper;
import databeans.MyTransaction;
import model.CustomerDAO;
import model.Model;
import model.TransactionDAO;

@Path("/depositCheck")
public class DepositCheck {
    @POST
    public Response deposit(@Context HttpServletRequest request,
            @FormParam("username") String username, @FormParam("cash") String cashString)
            throws ServletException {
        Model model = new Model();
        CustomerDAO customerDAO = model.getCustomerDAO();
        TransactionDAO transactionDAO = model.getTransactionDAO();
        Gson gson = new Gson();
        String output;

        if (request.getSession() == null) {
            output = "You must log in prior to making this request";
            return Response.status(200).entity(gson.toJson(new MessageWrapper(output))).build();
        }

        if (request.getSession().getAttribute("employee") == null) {
            output = "I'm sorry you are not authorized to preform that action";
            return Response.status(200).entity(gson.toJson(new MessageWrapper(output))).build();
        }

        try {
            // validate the customer & get customer id
            Customer customer = customerDAO.getByUserName(username);
            if (customer == null) {
                output = "I'm sorry, there was a problem depositing the money";
                return Response.status(200).entity(gson.toJson(new MessageWrapper(output))).build();
            }

            long cid = customer.getCustomerId();

            double cashDouble = Double.parseDouble(cashString);
            long cash = (long) (cashDouble * 100);

            Transaction.begin();

            // add to transactionform
            MyTransaction myTransaction = new MyTransaction();
            myTransaction.setAmount(cash);
            myTransaction.setCustomerId(cid);
            myTransaction.setTransactionType(MyTransaction.DEPOSIT_CHECK);
            myTransaction.setExecuteDate(new Date());
            transactionDAO.create(myTransaction);

            // add cash to customer form
            customer.setCash(customer.getCash() + cash);
            customerDAO.update(customer);

            Transaction.commit();

            output = "The account has been successfully updated";
        } catch (RollbackException e) {
            output = "I'm sorry, there was a problem depositing the money";
            return Response.status(200).entity(gson.toJson(new MessageWrapper(output))).build();
        } finally {
            if (Transaction.isActive())
                Transaction.rollback();
        }

        return Response.status(200).entity(gson.toJson(new MessageWrapper(output))).build();
    }
}
