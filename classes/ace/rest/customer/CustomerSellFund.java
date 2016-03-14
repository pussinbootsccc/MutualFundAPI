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
import databeans.Fund;
import databeans.MessageWrapper;
import databeans.MyTransaction;
import databeans.Position;
import model.CustomerDAO;
import model.FundDAO;
import model.FundPriceHistoryDAO;
import model.Model;
import model.PositionDAO;
import model.TransactionDAO;
import util.Common;

@Path("/sellFund")
public class CustomerSellFund {

    @POST
    public Response sellFund(@Context HttpServletRequest request, @FormParam("fundSymbol") String fundSymbol,
            @FormParam("numShares") String numShares) throws ServletException {

        Model model = new Model();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        CustomerDAO customerDAO = model.getCustomerDAO();
        FundDAO fundDAO = model.getFundDAO();
        PositionDAO positionDAO = model.getPositionDAO();
        TransactionDAO transactionDAO = model.getTransactionDAO();
        FundPriceHistoryDAO fundPriceHistoryDAO = model.getFundPriceHistoryDAO();
        MessageWrapper message = new MessageWrapper();

        HttpSession session = request.getSession(false);
        if (session == null) {
            message.setMessage("You must log in prior to making this request");
            return Response.status(200).entity(gson.toJson(message)).build();
        }

        if (session.getAttribute("customer") == null) {
            if (session.getAttribute("employee") == null) {
                message.setMessage("You must log in prior to making this request");
                return Response.status(200).entity(gson.toJson(message)).build();
            } else {
                message.setMessage("I'm sorry you are not authorized to preform that action");
                return Response.status(200).entity(gson.toJson(message)).build();
            }
        }

        try {
            Customer customer = (Customer) session.getAttribute("customer");
            long customerId = customer.getCustomerId();

            Fund fund = fundDAO.getByTicker(fundSymbol);
            if (fund == null) {
                message.setMessage("No such fund");
                return Response.status(200).entity(gson.toJson(message)).build();
            }
            long fundId = fund.getFundId();

            Position position = positionDAO.getByCustomerIdAndFundId(customerId, fundId);
            if (position == null) {
                message.setMessage("I'm sorry, you don't have enough shares of that fund in your portfolio");
                return Response.status(200).entity(gson.toJson(message)).build();
            }

            long availableShares = position.getShares();
            double sharesDouble = Double.parseDouble(numShares);
            long shares = (long) (sharesDouble * 1000);

            Transaction.begin();
            {
                Date lastTransitionDay = fundPriceHistoryDAO.getLastTransitionDay();
                position = positionDAO.getByCustomerIdAndFundId(customerId, fundId);

                availableShares = position.getShares();
                if (shares > availableShares) {
                    message.setMessage("I'm sorry, you don't have enough shares of that fund in your portfolio");
                    return Response.status(200).entity(gson.toJson(message)).build();
                }

                double currentPrice = (double) fundPriceHistoryDAO.getCurrentPrice(fundId).getPrice() / 100;
                String amountString = Common.convertDoublePriceToString(sharesDouble * currentPrice);
                long amount = (long) (Double.valueOf(amountString) * 100);

                // Create new Transaction
                MyTransaction transaction = new MyTransaction();
                transaction.setCustomerId(customerId);
                transaction.setFundId(fundId);
                transaction.setTransactionType(MyTransaction.SELL_FUND);
                transaction.setShares(shares);
                transaction.setAmount(amount);
                transaction.setPrice(fundPriceHistoryDAO.getCurrentPrice(fundId).getPrice());
                transaction.setExecuteDate(lastTransitionDay);
                transactionDAO.addNoT(transaction);

                // Update Position
                assert availableShares >= shares;
                if (availableShares == shares) {
                    positionDAO.delete(position.getPositionId());
                } else {
                    position.setShares(availableShares - shares);
                    positionDAO.updateNoT(position);
                }

                // Update Customer
                customer = customerDAO.read(customerId);
                customer.setCash(customer.getCash() + amount);
                customerDAO.updateNoT(customer);
            }

            Transaction.commit();

        } catch (RollbackException e) {
            e.printStackTrace();
            message.setMessage("Transaction Rollbacked.");
            return Response.status(200).entity(gson.toJson(message)).build();
        }
        message.setMessage("The purchase was successfully completed");
        return Response.status(200).entity(gson.toJson(message)).build();
    }
}
