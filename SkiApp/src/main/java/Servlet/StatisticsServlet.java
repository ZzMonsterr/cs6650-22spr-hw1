package Servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "StatisticsServlet", value = "/statistics")
public class StatisticsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res) throws ServletException,
            IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String urlPath = req.getPathInfo();

        // get status
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_OK);
            this.mockGetAPIPerfomanceStats(req, res);
            return;
        } else {
            Helper.setErrorStatusAndMessage(res, HttpServletResponse.SC_NOT_FOUND,
                    "Invalid input supplied");
        }
    }

    public void mockGetAPIPerfomanceStats(HttpServletRequest req,
                                          HttpServletResponse res) {

    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res) throws ServletException,
            IOException {

    }
}
