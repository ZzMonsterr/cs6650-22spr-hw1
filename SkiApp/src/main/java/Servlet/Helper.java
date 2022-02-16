package Servlet;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import Model.ResponseMsg;

public class Helper {

    /**
     * Check the url validation for Skier servlet
	 * valid url example:
	 * http://localhost:8080/SkiApp_war_exploded/skiers/12/seasons/2019/days/1/skiers/123
	 * urlPath  = "/1/seasons/2019/days/1/skiers/123"
	 * urlParts = [, 1, seasons, 2019, days, 1, skiers, 123]
     * @param urlPath
     * @return true / false
     */
    public static boolean isUrlValidForSkier(String[] urlPath) {
        if (urlPath.length == 3) {
            // /skiers/{skierID}/vertical
            if (isInteger(urlPath[1]) && urlPath[2].equals("vertical")) {
                return true;
            }
        } else if (urlPath.length == 8) {
            // /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
            if (isInteger(urlPath[1]) && urlPath[2].equals("seasons")
                    && isInteger(urlPath[3]) && urlPath[4].equals("days")
                    && isInteger(urlPath[5]) && urlPath[6].equals("skiers")
                    && isInteger(urlPath[7])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check the url validation for Resort servlet according to the API spec
     *  valid url example:
     *  http://localhost:8080/SkiApp_war_exploded/resorts/:resortID/seasons
     * @param urlPath
     * @return true / false
     */
    public static boolean isUrlValidForResort(String[] urlPath) {
        if (urlPath.length == 3 && isInteger(urlPath[1])
                && urlPath[2].equals("seasons")) {
            // /resorts/:resortID/seasons
            return true;
        } else if (urlPath.length == 7 && isInteger(urlPath[1])
                && urlPath[2].equals("seasons")
                && isInteger(urlPath[3]) && urlPath[4].equals("day")
                && isInteger(urlPath[5]) && urlPath[6].equals("skiers")) {
            // /resorts/:resortID/seasons/:seasonID/day/:dayID/skiers
            return true;
        }
        return false;
    }

    // helper function of isUrlValid for both Skier and Resort
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * Set ErrorCode as 404 (not found) or 400 (bad request) and
     * print some related error message to help the user to enter a valid url.
     * @param res
     * @param errorCode
     * @param message
     * @throws IOException
     */
    public static void setErrorStatusAndMessage(HttpServletResponse res,
                                                int errorCode,
                                                String message) throws IOException {
        res.setStatus(errorCode);

        ResponseMsg responseMsg = new ResponseMsg(message);
        String responseMsgJsonString = new Gson().toJson(responseMsg);

        PrintWriter out = res.getWriter();
        out.print(responseMsgJsonString);
        out.flush();
    }
}
