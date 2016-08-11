package core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * HttpServletResponseEnhanced
 * The javax.servlet.http.HttpServletResponse not support the "expires" parameter, only "MaxAge".
 * Unfortunately Internet explorer does not support MaxAge, but does support expires.
 *
 * This extends javax.servlet.http.HttpServletResponse by over-riding
 * the addCookie() method with one that writes the expires parameter if MaxAge>0
 *
 */
public class HTTPSltRespE extends HttpServletResponseWrapper {

    // date format for expired=date on Set-Cookie: parameter
    private static final DateFormat dateFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy hh:mm:ss", Locale.ENGLISH);

    /**
     * Constructor - same as super
     * @param response - создается на основе полученного респонза
     */
    public HTTPSltRespE(HttpServletResponse response) {
        super(response);
    }

    /*
     * over-ride with a cookie writer that includes an 'Expires=' parameter
     * if the cookie's maxAge is >0.
     *
     * Otherwise do the super's addCookie()
     *
     * The "expires=" is supported by Internet Explorer, whereas MaxAge is not. So
     * "expires=" is not supported by the standard javax.servlet.http.HttpServletResponse
     *
     * expires date format is "day, DD-MMM-YYYY HH:MM:SS timeZone",
     *    e.g.  Sun, 16-Nov-2008 13:14:35 GMT
     *
     * @param cookie
     */
    public void addCookie(Cookie cookie) {
        if (cookie.getMaxAge() <=0) {
            super.addCookie(cookie);
        } else {
            // write the Set-Cookie parameter with the Expires
            addHeader("Set-Cookie",
                    cookie.getName() + "=" + cookie.getValue() + ";"
                            //+ " Version=1;"
                            + " Expires=" + timeGMT(cookie.getMaxAge()) + ";"
                            //+ " Domain=" + cookie.getDomain() + ";"
                            + " Path=/;"
                            //+ " Path=" + cookie.getPath() + ";"
                            + " Max-Age=" + Integer.toString(cookie.getMaxAge()) + ";");
        }
    }

    /**
     * Return a string with the current time + timeFromNow (in milliseconds) as
     * Format is "EEE, dd-MMM-yyyy HH:MM:SS z" and timeZone is UTC (GMT)
     *
     * @param timeFromNow сдвиг от текущего времени
     * @return стринг в варианте с гмт
     */
    private String timeGMT(long timeFromNow) {
        Date date = new Date();                         // current system time as UTC (GMT)
        date.setTime(date.getTime() + (timeFromNow * 1000));     // add the time from now
        return dateFormat.format(date) + " GMT";
    }
}