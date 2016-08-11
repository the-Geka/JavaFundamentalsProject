package testers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

@WebServlet("/t/locale")
public class LocaleLang extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(), "UTF8"), true);

        out.println(httpHeadersToString(req));

        List<Locale> headerNames = Collections.list(req.getLocales());
        int i = 0;

        for (Locale lcl : headerNames) {
            String cnt = lcl.getCountry();
            String scr = lcl.getScript();
            String lng = lcl.getLanguage();
            String var = lcl.getVariant();

            out.print(++i + ". ");

            if (!cnt.isEmpty()) out.print(" CN: " + cnt);
            if (!scr.isEmpty()) out.print(" SC: " + scr);
            if (!lng.isEmpty()) out.print(" LN: " + lng);
            if (!var.isEmpty()) out.print(" VR: " + var);

            out.println("<br/>");
        }

        out.println("<br/>");

        out.println(
                ofNullable(req.getCookies())
                        .map(cookies -> stream(cookies)
                                .map(cookie -> cookie.getName() + " = " + cookie.getValue())
                                .collect(joining("<br/>")))
                        .orElse("No cookies")
        );



/*
        Enumeration<Locale> localeEnumeration = req.getLocales();
        while (localeEnumeration.hasMoreElements()) {


            Locale lcl = localeEnumeration.nextElement();


            //String cnt = localeEnumeration.nextElement().getCountry();
            //String lng = localeEnumeration.nextElement().getLanguage();
            if (!cnt.get().isEmpty()) out.print("CN: " + cnt.get() + " ");
            if (lng.isPresent()) out.println("LN: " + lng.get());
            out.println("<br>");
        }*/
        out.close();
    }

    private static String httpHeadersToString(HttpServletRequest request) {
        List<String> headerNames = Collections.list(request.getHeaderNames());
        StringBuilder sb = new StringBuilder();

        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>Hello World!</title>");
        sb.append("</head>");
        sb.append("<body>");
        //sb.append("<h1>Hello World!</h1>");
        sb.append("</body>");
        sb.append("</html>");
        sb.append(java.time.LocalDateTime.now().toString());
        sb.append("<br>");

        for (String headerName : headerNames) {
            String headerValue = request.getHeader(headerName);
            sb.append(headerName).append("='").append(headerValue).append("'\n");
            sb.append("<br>");
        }
        return sb.toString();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
