package ace.rest.employee;

import java.util.Date;
import java.util.Random;

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

import databeans.Fund;
import databeans.FundPriceHistory;
import databeans.MessageWrapper;
import model.FundDAO;
import model.FundPriceHistoryDAO;
import model.Model;

@Path("/createFund")
public class CreateFund {
    @POST
    public Response createFund(@Context HttpServletRequest request,
            @FormParam("name") String name, @FormParam("symbol") String symbol)
            throws ServletException {
        Model model = new Model();
        FundDAO fundDAO = model.getFundDAO();
        FundPriceHistoryDAO priceDAO = model.getFundPriceHistoryDAO();

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String output;

        if (request.getSession(false) == null) {
            output = "You must log in prior to making this request";
            return Response.status(200).entity(gson.toJson(new MessageWrapper(output))).build();
        }

        if (request.getSession().getAttribute("employee") == null) {
            output = "I'm sorry you are not authorized to preform that action";
            return Response.status(200).entity(gson.toJson(new MessageWrapper(output))).build();
        }

        try {
            if (name == null || symbol == null) {
                output = "I'm sorry, there was a problem creating the fund";
                return Response.status(200).entity(gson.toJson(new MessageWrapper(output))).build();
            }

            if (fundDAO.getByName(name) != null || fundDAO.getByTicker(symbol) != null) {
                output = "I'm sorry, there was a problem creating the fund";
                return Response.status(200).entity(gson.toJson(new MessageWrapper(output))).build();
            }

            Fund fund = new Fund();
            fund.setFundName(name);
            fund.setFundTicker(symbol);
            fund.setFundPrice(new Random().nextInt(49000) + 1000);

            fundDAO.createFund(fund);

            FundPriceHistory fundPriceHistory = new FundPriceHistory();
            fundPriceHistory.setFundId(fund.getFundId());
            fundPriceHistory.setPrice(fund.getFundPrice());
            fundPriceHistory.setPriceDate(new Date());
            priceDAO.create(fundPriceHistory);

            output = "The fund has been successfully created";
        } catch (RollbackException e) {
            output = "I'm sorry, there was a problem creating the fund";
            return Response.status(200).entity(gson.toJson(new MessageWrapper(output))).build();
        } finally {
            if (Transaction.isActive())
                Transaction.rollback();
        }
        return Response.status(200).entity(gson.toJson(new MessageWrapper(output))).build();
    }
}
