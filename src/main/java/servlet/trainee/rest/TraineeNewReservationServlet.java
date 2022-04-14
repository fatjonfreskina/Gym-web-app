package servlet.trainee.rest;

import com.google.gson.JsonParseException;
import constants.Constants;
import constants.Codes;
import dao.lecturetimeslot.GetLectureTimeSlotByRoomDateStartTimeDatabase;
import dao.reservation.GetAvailableSlotsReservation;
import dao.reservation.GetReservationByAllFields;
import dao.reservation.InsertReservationDatabase;
import dao.subscription.GetSubscriptionExpirationByLTSDatabase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import resource.LectureTimeSlot;
import resource.Reservation;
import servlet.AbstractRestServlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class TraineeNewReservationServlet extends AbstractRestServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Codes code = checkAcceptMediaType(req);
        if (code != Codes.OK)
        {
            sendErrorResponse(resp, code);
            return;
        }

        code = checkContentTypeMediaType(req);
        if (code != Codes.OK)
        {
            sendErrorResponse(resp, code);
            return;
        }

        processRequest(req,resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        Reader input = new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8);
        Reservation res;
        try{
            res = GSON.fromJson(input, Reservation.class);
        }catch(JsonParseException e){
            sendErrorResponse(resp, Codes.WRONG_JSON_RESERVATION);
            return;
        }
        // Retrieve trainee email by session.
        final HttpSession session = req.getSession(false);
        if (session == null)
        {
            resp.sendRedirect(req.getContextPath() + Constants.RELATIVE_URL_LOGIN);
            return;
        }
        final String email = session.getAttribute("email").toString();
        res = new Reservation(res, email);

        //Check 0: date and time for the reservations greater than current date and time
        if(!isDateAndTimeValid(res.getLectureDate(), res.getLectureStartTime())){
            sendErrorResponse(resp, Codes.INVALID_DATE);
            return;
        }
        try{
            Connection conn = getConnection();
            //Check 1: requested reservation is related to a real lecture time slot
            LectureTimeSlot lts = new LectureTimeSlot(res.getRoom(), res.getLectureDate(), res.getLectureStartTime(), 0, null, null);
            if(new GetLectureTimeSlotByRoomDateStartTimeDatabase(conn,lts).execute() == null) {
                sendErrorResponse(resp, Codes.LECTURETIMESLOT_NOT_FOUND);
                return;
            }
            //Check 2: available slots for the requested reservation
            if(new GetAvailableSlotsReservation(conn,res).execute() <=0 ){
                sendErrorResponse(resp, Codes.ROOM_ALREADY_FULL);
                return;
            }
            //Check 3: requested reservation is compatible with my subscriptions and is not over its expiration of the subscription
            Date expiration = new GetSubscriptionExpirationByLTSDatabase(conn,lts,email).execute();
            if(expiration == null){
                sendErrorResponse(resp, Codes.TRAINEE_NOT_ENROLLED_TO_THE_COURSE);
                return;
            }
            if(expiration.compareTo(res.getLectureDate()) <= 0){
                sendErrorResponse(resp, Codes.SUBSCRIPION_EXPIRED_BEFORE);
                return;
            }

            //Check 4: not already present a reservation made by the same user in the same slot
            if(new GetReservationByAllFields(conn,res).execute() != null) {
                sendErrorResponse(resp, Codes.RESERVATION_ALREADY_PRESENT);
                return;
            }
        }catch(Throwable e){
            sendErrorResponse(resp, Codes.INTERNAL_ERROR);
            return;
        }


        try {
            Connection conn = getConnection();
            new InsertReservationDatabase(conn, res).execute();
            sendDataResponse(resp, res);

        } catch (Throwable th) {
            sendErrorResponse(resp, Codes.INTERNAL_ERROR);
        }
    }

    private boolean isDateAndTimeValid(Date reservationDate, Time reservationTime)
    {
        long millis = System.currentTimeMillis();
        Date today = new Date(millis);
        Time now = new Time(millis);

        return today.compareTo(reservationDate) < 0 ||
                (today.compareTo(reservationDate) == 0 && now.compareTo(reservationTime) <= 0);
    }

}
