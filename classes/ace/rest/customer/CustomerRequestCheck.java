package ace.rest.customer;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import databeans.Customer;
import databeans.MessageWrapper;
import databeans.MyTransaction;
import model.CustomerDAO;
import model.FundPriceHistoryDAO;
import model.Model;
import model.TransactionDAO;

@Path("/requestCheck")
public class CustomerRequestCheck {
    @POST
    public Response requestCheck(@Context HttpServletRequest request, @FormParam("cashValue") String cashValue)
            throws ServletException {

        Model model = new Model();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        CustomerDAO customerDAO = model.getCustomerDAO();
        TransactionDAO transactionDAO = model.getTransactionDAO();
        FundPriceHistoryDAO priceDAO = model.getFundPriceHistoryDAO();
        MessageWrapper message = new MessageWrapper();

        HttpSession session = request.getSession(false);
        if (session == null) {
            message.setMessage("You must log in prior to making this request");
            return Response.status(200).entity(gson.toJson(message)).build();
        }

        if (session.getAttribute("customer") == null) {
            message.setMessage("I'm sorry you are not authorized to preform that action");
            return Response.status(200).entity(gson.toJson(message)).build();
        }

        try {
            Customer customer = (Customer) session.getAttribute("customer");

            double amountDouble = Double.parseDouble(cashValue);
            long amount = (long) (amountDouble * 100);

            Transaction.begin();
            {
                customer = customerDAO.read(customer.getCustomerId());

                long availableCash = customer.getCash();
                if (amount > availableCash) {
                    message.setMessage("I'm sorry, the amount requested is greater than the balance of your account");
                    return Response.status(200).entity(gson.toJson(message)).build();
                }
                Date lastTransitionDay = priceDAO.getLastTransitionDay();
                Date newDay = new Date(lastTransitionDay.getTime() + (1000 * 60 * 60 * 24)); 

                MyTransaction transaction = new MyTransaction();
                transaction.setCustomerId(customer.getCustomerId());
                transaction.setTransactionType(MyTransaction.REQUEST_CHECK);
                transaction.setAmount(amount);
                transaction.setExecuteDate(newDay); // To add time stamp immediately
                transactionDAO.addNoT(transaction);

                customer.setCash(availableCash - amount);
                customerDAO.updateNoT(customer);
            }

            Transaction.commit();

        } catch (RollbackException e) {
            e.printStackTrace();
            message.setMessage("Transaction Rollbacked");
            return Response.status(200).entity(gson.toJson(message)).build();
        } catch (NumberFormatException e2) {
        	e2.printStackTrace();
        }
        message.setMessage("The withdrawal was successfully completed");
        return Response.status(200).entity(gson.toJson(message)).build();
    }
}
