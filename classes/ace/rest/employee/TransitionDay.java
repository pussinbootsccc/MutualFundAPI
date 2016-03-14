package ace.rest.employee;

import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import databeans.Fund;
import databeans.MessageWrapper;
import model.FundDAO;
import model.FundPriceHistoryDAO;
import model.Model;

@Path("/transitionDay")
public class TransitionDay {
    @POST
    public Response deposit(@Context HttpServletRequest request) throws ServletException {
        Model model = new Model();
        FundPriceHistoryDAO fundPriceHistoryDao = model.getFundPriceHistoryDAO();
        FundDAO fundDao = model.getFundDAO();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String message = null;

        if (request.getSession().getAttribute("employee") == null
                && request.getSession().getAttribute("customer") == null) {
            message = "You must log in prior to making this request";
            return Response.status(200).entity(gson.toJson(new MessageWrapper(message))).build();
        }

        if (request.getSession().getAttribute("employee") == null) {
            message = "I'm sorry you are not authorized to preform that action";
            return Response.status(200).entity(gson.toJson(new MessageWrapper(message))).build();
        }
        try {
            Transaction.begin();
            Date lastTransitionDay = fundPriceHistoryDao.getLastTransitionDay();
            Date newDay = new Date(lastTransitionDay.getTime() + (1000 * 60 * 60 * 24));

            if (fundDao.getFundNumbers() != 0) {
                Fund[] fundList = fundDao.getAllFunds();
                long[] fundIds = new long[fundList.length];
                long[] fundPrices = new long[fundList.length];
                for (int i = 0; i < fundList.length; i++) {
                    fundIds[i] = fundList[i].getFundId();
                    fundPrices[i] = fundList[i].getFundPrice();
                }
                fluctuateFunds(fundPrices);
                fundPriceHistoryDao.UpdateAllFundsPrice(fundIds, fundPrices, newDay);
                fundDao.updateAllFundPrices(fundIds, fundPrices);
            }
            Transaction.commit();
            return Response.status(200).entity("The fund prices have been recalculated").build();
        } catch (RollbackException e) {
            return Response.status(200).entity("I'm sorry, there seems some problem, please try again").build();
        } finally {
            if (Transaction.isActive())
                Transaction.rollback();
        }

    }

    private long[] fluctuateFunds(long[] fundPrices) {
        Random r = new Random();
        int low = -10;
        int high = 10;
        double result = 0;// r.nextInt(High - Low) + Low;
        for (int i = 0; i < fundPrices.length; i++) {
            result = (r.nextInt(high - low) + low) * 1.00 / 100;
            double tmp = fundPrices[i] * (result + 1);
            fundPrices[i] = (long) tmp;
        }
        return fundPrices;
    }

}
