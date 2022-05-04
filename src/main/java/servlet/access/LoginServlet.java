package servlet.access;

import constants.Codes;
import constants.Constants;
import dao.emailconfirmation.GetEmailConfirmationIfExistsDatabase;
import dao.person.GetPersonByEmailDatabase;
import dao.person.GetPersonRolesDatabase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resource.EmailConfirmation;
import resource.Message;
import resource.Person;
import resource.TypeOfRoles;
import servlet.AbstractServlet;
import utils.EncryptionManager;
import utils.InputValidation;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet used to login
 *
 * @author Andrea Pasin
 * @author Francesco Caldivezzi
 * @author Harjot Singh
 */
public class LoginServlet extends AbstractServlet {


    private final Logger logger = LogManager.getLogger("andrea_pasin_logger");

    /**
     * Handles the get request by providing the correct page
     * @param req  the request
     * @param res  the response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("email") != null;
        if (loggedIn) {
            res.sendRedirect(req.getContextPath());
        } else {
            req.getRequestDispatcher(Constants.PATH_LOGIN).forward(req, res);
            // req.getRequestDispatcher("/jsp/access/roles.jsp").forward(req, res);
        }
    }

    /**
     * Handles the post request by confirming/refusing the user's request to login
     * @param req  the request
     * @param res  the response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email;
        String password;
        Person person = null;
        List<TypeOfRoles> userRoles = null;


        //Parse params to check if they are well-formatted, if not send back an error
        //Message message = new Message(Codes.OK.getErrorMessage(),false);
        Codes error = parseParams(req, res);

        if (error.getErrorCode() != Codes.OK.getErrorCode()) {
            sendBackError(error, req, res);
            return;
        }
        logger.info("Login: parsed");

        email = req.getParameter(Constants.EMAIL);
        password = req.getParameter(Constants.PASSWORD);


        //Encrypt password
        try {
            //logger.info("Password before:"+password);
            password = EncryptionManager.encrypt(password);
            //logger.info(email);
            //logger.info("Password after:"+password);
        } catch (Exception e) {
            error = Codes.INTERNAL_ERROR;
            sendBackError(error, req, res);
            return;
        }
        logger.info(email + " Login: encrypted");


        //Validate credentials
        try {
            person = (new GetPersonByEmailDatabase(getDataSource().getConnection(), email).execute());
        } catch (SQLException | NamingException e) {
            error = Codes.INTERNAL_ERROR;
            sendBackError(error, req, res);
            return;
        }
        if (person == null || !person.getPsw().equals(password)) {
            error = Codes.NOT_AUTHENTICATED;
            sendBackError(error, req, res);
            return;
        }
        logger.info(email + " Login: validated credentials");


        //Check if the person had confirmed the email
        EmailConfirmation emailStillToConfirm = null;
        try {
            emailStillToConfirm = (new GetEmailConfirmationIfExistsDatabase(getDataSource().getConnection(), new EmailConfirmation(person.getEmail())).execute());
        } catch (SQLException | NamingException e) {
            logger.info(e);
            error = Codes.INTERNAL_ERROR;
            sendBackError(error, req, res);
            return;
        }
        if (emailStillToConfirm != null) {
            error = Codes.NOT_AUTHENTICATED;
            sendBackError(error, req, res);
            /*
            res.setStatus(error.getHTTPCode());
            req.setAttribute(Constants.MESSAGE,message);
            //sendBackError(error,req,res);
            req.getRequestDispatcher(Constants.PATH_CONFIRM_REGISTRATION).forward(req, res);*/
            return;
        }
        logger.info(email + " Login: validated account");


        //Retrieve person roles
        try {
            userRoles = new GetPersonRolesDatabase(getDataSource().getConnection(), person).execute();

        } catch (SQLException | NamingException e) {
            error = Codes.INTERNAL_ERROR;
            sendBackError(error, req, res);
            return;
        }
        if (userRoles.isEmpty()) {
            sendBackError(Codes.USER_HAS_NO_ROLE_ASSIGNED, req, res);
            return;
        }
        logger.info(email + " Login: obtained roles" + userRoles);

        //Create session and set attributes
        HttpSession session = req.getSession();
        List<String> roles = new ArrayList<>();
        for (TypeOfRoles r : userRoles) {
            roles.add(r.getRole());
            logger.info(r.getRole());
        }
        session.setAttribute("email", person.getEmail());
        session.setAttribute("roles", roles);
        session.setAttribute("defaultRole", userRoles.get(0).getRole());
        session.setAttribute("avatarPath", person.getAvatarPath());


        //Everything is fine so far! Now act depending on user roles
        if (userRoles.size() == 1) {

            res.sendRedirect(req.getContextPath() + "/" + userRoles.get(0).getRole());
            return;
        }
        if (userRoles.size() > 1) {

            //req.setAttribute("roles", roles);

            //TODO in servlet for /access/roles where the user selects one role as default
            //session.setAttribute("defaultRole", userRoles.get(1).getRole());
            //req.getRequestDispatcher("/access/roles").forward(req, res);
            logger.info(req.getContextPath() + "/access/roles");
            res.sendRedirect(req.getContextPath() + "/access/roles");
        }
        logger.info(email + " Login: confirmed and returned");
    }

    /**
     * Sends the error to the client
     *
     * @param error The error to send
     * @param req   The request done by the client
     * @param res   The response to send to the client
     * @throws ServletException
     * @throws IOException
     */
    private void sendBackError(Codes error, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Message message = new Message(error.getErrorMessage(), true);
        res.setStatus(error.getHTTPCode());
        req.setAttribute(Constants.MESSAGE, message);
        req.getRequestDispatcher(Constants.PATH_LOGIN).forward(req, res);
        logger.info("Login: error =" + error.getErrorMessage());
    }

    /**
     * Checks if the different parameters are well formatted
     *
     * @param req  the request
     * @param res  the response
     * @return  a confirmation/error message
     * @throws ServletException
     * @throws IOException
     */
    private Codes parseParams(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = null;
        String password = null;
        Codes error = Codes.OK;
        try {
            email = req.getParameter("email");
            logger.info(email);
            password = req.getParameter("password");
            logger.info(password);

        } catch (IllegalArgumentException e) {
            error = Codes.INVALID_FIELDS;
        }
        if (error.getErrorCode() == Codes.OK.getErrorCode()) {
            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                error = Codes.EMPTY_INPUT_FIELDS;
            } else if (!InputValidation.isValidEmailAddress(email)) {
                error = Codes.NOT_A_MAIL;
            }
        }
        return error;
    }
}
