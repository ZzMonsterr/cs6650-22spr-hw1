package Servlet;

import Model.*;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ResortServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String urlPath = req.getPathInfo();

        // get all resorts
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_OK);
            this.mockGetAllResorts(res);
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)

        if (!Helper.isUrlValidForResort(urlParts)) {
            Helper.setErrorStatusAndMessage(res, HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid input supplied");
        } else {
            res.setStatus(HttpServletResponse.SC_OK);
            if (urlParts.length == 3) {
                this.mockGetSeasons(res, urlParts);
            } else {
                this.mockGetSkiers(res, urlParts);
            }
        }

    }

    /**
     * get the total vertical for the skier for the specified ski day
     * @throws IOException
     */
    // http://localhost:8080/SkiApp_war_exploded/resorts
    private void mockGetAllResorts(HttpServletResponse res)
            throws IOException {
        // mock
        ResortsList resortsList = new ResortsList();
        ResortsListResort resortA = new ResortsListResort("resortA", 1);
        ResortsListResort resortB = new ResortsListResort("resortB", 2);
        resortsList.addResort(resortA);
        resortsList.addResort(resortB);

        Gson gson = new Gson();
        String totalJsonString = gson.toJson(resortsList);
        PrintWriter out = res.getWriter();
        out.print(totalJsonString);
        out.flush();
    }

    /**
     * get number of unique skiers at resort/season/day
     * @param res
     * @param urlParts
     * @throws IOException
     */
    // http://localhost:8080/SkiApp_war_exploded/resorts/:resortID/seasons/:seasonID/day/:dayID/skiers
    private void mockGetSkiers(HttpServletResponse res,
                               String[] urlParts)
            throws IOException {
        // mock
        ResortSkiers resortSkiers = new ResortSkiers("2022-01-01", 10086);

        Gson gson = new Gson();
        String totalJsonString = gson.toJson(resortSkiers);
        PrintWriter out = res.getWriter();
        out.print(totalJsonString);
        out.flush();
    }

    /**
     * get a list of seasons for the specified resort
     * @param res
     * @throws IOException
     */
    // http://localhost:8080/SkiApp_war_exploded/resorts/1/seasons
    private void mockGetSeasons(HttpServletResponse res,
                               String[] urlParts)
            throws IOException {
        // mock
        List<String> seasons = new ArrayList<>();
        seasons.add("Winter");
        seasons.add("Spring");
        SeasonsList seasonsList = new SeasonsList(seasons);

        Gson gson = new Gson();
        String totalJsonString = gson.toJson(seasonsList);
        PrintWriter out = res.getWriter();
        out.print(totalJsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();

        String urlPath = req.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty() || urlPath.equals("/")) {
            Helper.setErrorStatusAndMessage(res, HttpServletResponse.SC_NOT_FOUND,
                    "Data not found");
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)

        if (!Helper.isUrlValidForResort(urlParts)) {
            Helper.setErrorStatusAndMessage(res, HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid input supplied");
        } else {
            res.setStatus(HttpServletResponse.SC_CREATED);
            mockPostANewSeason(res, urlParts);
        }
    }

    private void mockPostANewSeason(HttpServletResponse res,
                                        String[] urlParts)
            throws IOException {
        // TODO: mock Post request

    }

}
