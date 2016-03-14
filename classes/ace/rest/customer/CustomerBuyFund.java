package ace.rest.customer;

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
import com.google.gson.GsonBuilder;

import databeans.Customer;
import databeans.Employee;
import databeans.Fund;
import databeans.MyTransaction;
import model.CustomerDAO;
import model.FundDAO;
import model.FundPriceHistoryDAO;
import model.Model;
import model.PositionDAO;
import model.TransactionDAO;

@Path("/buyFund")
public class CustomerBuyFund {
    @POST

    public Response getMsg(@Context HttpServletRequest request, @FormParam("fundSymbol") String fundSymbol,
            @FormParam("cashValue") String cashValue) throws ServletException {

        Model model = new Model();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        CustomerDAO customerDAO = model.getCustomerDAO();
        TransactionDAO transactionDAO = model.getTransactionDAO();
        FundDAO fundDAO = model.getFundDAO();
        FundPriceHistoryDAO fundPriceHistoryDAO = model.getFundPriceHistoryDAO();
        PositionDAO positionDAO = model.getPositionDAO();

        String message = "";

        try {
            Customer customer = (Customer) request.getSession().getAttribute("customer");
            Employee employee = (Employee) request.getSession().getAttribute("employee");
            if (customer == null) {
                if (employee == null) {
                    message = "You must log in prior to making this request";
                    return Response.status(200).entity(gson.toJson(message)).build();
                } else {
                    message = "I'm sorry you are notauthorized to preform that action";
                    return Response.status(200).entity(gson.toJson(message)).build();
                }

            }

            Transaction.begin();
            Date lastTransitionDay = fundPriceHistoryDAO.getLastTransitionDay();
            Date newDay = new Date(lastTransitionDay.getTime() + (1000 * 60 * 60 * 24));
            Fund fund = fundDAO.getByTicker(fundSymbol);
            if (fund == null) {
                message = "The fund doesn't exist";
                return Response.status(200).entity(gson.toJson(message)).build();
            }
            long availableCash = customer.getCash();
            double amountDouble = Double.parseDouble(cashValue);
            long amount = (long) (amountDouble * 100);

            availableCash = customer.getCash();
            if (amount > availableCash) {
                message = "I'm sorry, you must first deposit sufficient funds in your account in order to make this purchase";
                return Response.status(200).entity(gson.toJson(message)).build();
            }
            // calculate the share
            long fundPrice = fund.getFundPrice();
            double shareDouble = (double) amount / (double) fundPrice;
            long shareLong = (long) shareDouble;
            long share = shareLong * 1000;
            // calculate the actual money need;
            long realAmount = shareLong * fundPrice;

            // update transaction
            MyTransaction transaction = new MyTransaction();
            transaction.setCustomerId(customer.getCustomerId());
            transaction.setFundId(fund.getFundId());
            transaction.setTransactionType(MyTransaction.BUY_FUND);
            transaction.setShares(share);
            transaction.setPrice(fundPrice);
            transaction.setAmount(realAmount);
            transaction.setExecuteDate(newDay);
            transactionDAO.addNoT(transaction);

            // update position
            positionDAO.updatePositionInTransactionDay(fund.getFundId(), customer.getCustomerId(), share, 2);

            // update customer
            customer.setCash(availableCash - realAmount);
            customerDAO.updateNoT(customer);

            Transaction.commit();

            message = "The purchase was successfully completed";
            return Response.status(200).entity(gson.toJson(message)).build();

        } catch (RollbackException e) {
            message = "Fail";
            return Response.status(200).entity(gson.toJson(message)).build();
        } finally {
            if (Transaction.isActive()) {
                Transaction.rollback();
            }
        }

    }

}
