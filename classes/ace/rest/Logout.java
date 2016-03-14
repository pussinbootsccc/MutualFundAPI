package ace.rest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import databeans.MessageWrapper;

@Path("/logout")
public class Logout {
    @POST
    public Response logout(@Context HttpServletRequest request)
            throws ServletException {
        request.getSession().setAttribute("employee", null);
        request.getSession().setAttribute("customer", null);
        String output = "You've been logged out";
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return Response.status(200).entity(gson.toJson(new MessageWrapper(output))).build();
    }
}
