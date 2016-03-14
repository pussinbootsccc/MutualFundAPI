/* @author yujie
 * 
 */
package ace.rest.customer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import databeans.Customer;
import databeans.CustomerFund;
import databeans.CustomerFundView;
import databeans.ViewPortfolioWrapper;
import model.CustomerDAO;
import model.Model;
import model.viewDAO.CustomerFundDAO;
import util.Common;

@Path("/viewPortfolio")
public class ViewPortfolio {
    @GET
    public Response viewPortfolioAction(@Context HttpServletRequest request) throws ServletException {
        Model model = new Model();
        Gson gson =  new GsonBuilder().disableHtmlEscaping().create();
        CustomerDAO customerDAO = model.getCustomerDAO();
        CustomerFundDAO customerFundDAO = model.getCustomerFundDAO();
        String err;

        ViewPortfolioWrapper wrapper = new ViewPortfolioWrapper();
        
        

        HttpSession session = request.getSession(false);
//        if (session == null) {
//            wrapper.setMessage("You must log in prior to making this request");
//            return Response.status(200).entity(gson.toJson(wrapper).replaceAll("\"", "")).build();
//        }
        
        if (request.getSession().getAttribute("employee") == null
                && request.getSession().getAttribute("customer") == null) {
        	wrapper.setMessage("You must log in prior to making this request");
            return Response.status(200).entity(gson.toJson(wrapper)).build();
        }

        if (session.getAttribute("customer") == null) {
            wrapper.setMessage("I'm sorry you are not authorized to preform that action");
            return Response.status(200).entity(gson.toJson(wrapper)).build();
        }

        try {
            Customer customer = (Customer) session.getAttribute("customer");
            customer = customerDAO.read(customer.getCustomerId());
            CustomerFund[] allFunds = customerFundDAO.getCustomerFund(customer.getCustomerId());

            if (allFunds == null || allFunds.length == 0) {
                wrapper.setMessage("You don't have any funds at this time");
            } else {
                wrapper.Funds = new CustomerFundView[allFunds.length];
                for (int i = 0; i < allFunds.length; i++) {
                    CustomerFundView view = new CustomerFundView();
                    view.setFundSymbol(allFunds[i].getFundTicker());
                    view.setFundPrice(Common.convertLongPriceToString(allFunds[i].getFundPrice()));
                    view.setShares(Common.convertLongShareToString(allFunds[i].getShares()));
                    wrapper.Funds[i] = view;
                }
            }

            // long depositPendingAmount = transactionDAO.getPendingDepositAmountByCustomerId(customer.getCustomerId());

            // Set amount with no decimal
            String cash = Double.toString((double) customer.getCash() / 100);

            wrapper.setCash(cash);
            // return Response.status(200).entity(gson.toJson(wrapper)).build();

            // Alternative output without quotes
            return Response.status(200).entity(gson.toJson(wrapper).replaceAll("\"", "")).build();

        } catch (Exception e) {
            err = e.getMessage();
        }
        return Response.status(200).entity(gson.toJson(err)).build();

    }
}
