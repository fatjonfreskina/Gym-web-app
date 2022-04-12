package servlet.trainee.rest;

import com.google.gson.Gson;
import constants.ErrorCodes;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import resource.LectureTimeSlot;
import resource.Message;
import servlet.AbstractServlet;

import javax.naming.NamingException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;


public class TraineeDeleteReservationServelt extends AbstractServlet {
    private static final String JSON_MEDIA_TYPE = "application/json";
    private static final String JSON_UTF_8_MEDIA_TYPE = "application/json; charset=utf-8";
    private static final String ALL_MEDIA_TYPE = "*/*";


    //TODO Activate this method

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ErrorCodes code = ErrorCodes.METHOD_NOT_ALLOWED;
        setUpErrorMessage(resp, code);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ErrorCodes code = ErrorCodes.METHOD_NOT_ALLOWED;
        setUpErrorMessage(resp, code);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ErrorCodes code = ErrorCodes.METHOD_NOT_ALLOWED;
        setUpErrorMessage(resp, code);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ErrorCodes code = ErrorCodes.METHOD_NOT_ALLOWED;
        setUpErrorMessage(resp, code);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ErrorCodes code = ErrorCodes.METHOD_NOT_ALLOWED;
        setUpErrorMessage(resp, code);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ErrorCodes code = ErrorCodes.METHOD_NOT_ALLOWED;
        setUpErrorMessage(resp, code);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String path = req.getRequestURI();
        final String[] path_splitted = path.split("\\/");

        int i = 0;
        while (!path_splitted[i].equals("trainee") && i < path_splitted.length) {
            i++;
        }

        //Note : is the number of parameters required for a compatible URI request
        /* /trainee/rest/reservation/room/{room}/date/{date}/starttime/{time} */

        if (i != path_splitted.length - 9) {
            ErrorCodes code = ErrorCodes.BAD_REQUEST;
            setUpErrorMessage(resp, code);
            return;
        }

        if (    !path_splitted[i + 1].equals("rest") ||
                !path_splitted[i + 2].equals("reservation") ||
                !path_splitted[i + 3].equals("room") ||
                !path_splitted[i + 5].equals("date") ||
                !path_splitted[i + 7].equals("starttime")
        ) {
            ErrorCodes code = ErrorCodes.BAD_REQUEST;
            setUpErrorMessage(resp, code);
            return;
        }
        PrintWriter out = resp.getWriter();

        final String room = String.valueOf(path_splitted[i+4]);
        final Date date = Date.valueOf(path_splitted[i+6]);
        final Timestamp time = Timestamp.valueOf(path_splitted[i+8]);

        HttpSession session = req.getSession(false);
        final String email = session.getAttribute("email").toString();
        List<LectureTimeSlot> l_slots;

    }

        private void setUpErrorMessage (HttpServletResponse resp, ErrorCodes code) throws IOException {
            String messageJson = new Gson().toJson(new Message(code.getErrorMessage(), true));
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("utf-8");
            resp.setStatus(code.getHTTPCode());
            out.print(messageJson);

            out.flush();
            out.close();


        }
    }

