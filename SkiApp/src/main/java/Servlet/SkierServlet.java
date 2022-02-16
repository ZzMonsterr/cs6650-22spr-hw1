package Servlet;

import Model.*;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SkierServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String urlPath = req.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty() || urlPath == "/") {
            Helper.setErrorStatusAndMessage(res, HttpServletResponse.SC_NOT_FOUND,
                    "Data not found");
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)

        if (!Helper.isUrlValidForSkier(urlParts)) {
            Helper.setErrorStatusAndMessage(res, HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid input supplied");
        } else {
            res.setStatus(HttpServletResponse.SC_OK);
            // do any sophisticated processing with urlParts which contains all the url params
            // DONE: process url params in `urlParts`
            if (urlParts.length == 8) {
                this.mockGetSkiersDetails(res, urlParts);
            } else {
                this.mockGetSkiersVertical(res, urlParts);
            }
        }
    }

    /**
     * get the total vertical for the skier for the specified ski day
     * @param urlParts
     * @throws IOException
     */
    // http://localhost:8080/SkiApp_war_exploded/skiers/12/seasons/2019/days/1/skiers/123
    private void mockGetSkiersDetails(HttpServletResponse res,
                                      String[] urlParts)
            throws IOException {
        // return some dummy data
        // TODO: Data not found (so far url is valid) should return 404 error
        //  code and message
        int total = 12345;
        String totalJsonString = new Gson().toJson(total);

        PrintWriter out = res.getWriter();
        out.print(totalJsonString);
        out.flush();
    }

    /**
     * get the total vertical for the skier the specified resort.
     * If no season is specified, return all seasons
     * @param res
     * @throws IOException
     */
    // http://localhost:8080/SkiApp_war_exploded/skiers/123/vertical
    private void mockGetSkiersVertical(HttpServletResponse res,
                                       String[] urlParts)
            throws IOException {
        // TODO: Data not found (so far url is valid) should return 404 error
        //  code and message; return some dummy data now
        SkierVerticalResort skierVerticalResort = new SkierVerticalResort("Winter", 1000);
        SkierVertical skierVertical = new SkierVertical();
        skierVertical.add(skierVerticalResort);

        String verticalsListJsonString = new Gson().toJson(skierVertical);

        PrintWriter out = res.getWriter();
        out.print(verticalsListJsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        // check whether url is valid (parameters are valid), just as doGet
        String urlPath = req.getPathInfo();

        if (urlPath == null || urlPath.isEmpty() || urlPath.equals("/")) {
            Helper.setErrorStatusAndMessage(res, HttpServletResponse.SC_NOT_FOUND,
                    "Data not found");
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)

        if (!Helper.isUrlValidForSkier(urlParts)) {
            Helper.setErrorStatusAndMessage(res, HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid input supplied");
        } else {
            res.setStatus(HttpServletResponse.SC_CREATED);
            // analyze JSON Object from req, and create a Java Instance for it
            try {
                mockPostSkiersVertical(req, res, urlParts);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void mockPostSkiersVertical(
            HttpServletRequest req, HttpServletResponse res,
                                       String[] urlParts)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = req.getReader().readLine()) != null) {
            sb.append(s);
        }

        Gson gson = new Gson();
        LiftRide liftRide = (LiftRide) gson.fromJson(sb.toString(),
                LiftRide.class);
        // TODO: save this liftRide into a Database,
        //  Temporarily print out here
        String objectJsonString = new Gson().toJson(liftRide);
        PrintWriter out = res.getWriter();
        out.print(objectJsonString);
        out.flush();
    }
}
