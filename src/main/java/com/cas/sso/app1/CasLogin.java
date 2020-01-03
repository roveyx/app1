package com.cas.sso.app1;

import org.jasig.cas.client.authentication.AttributePrincipal;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Servlet implementation class casLogin
 *
 * @author rovey
 */
public class CasLogin extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CasLogin() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // cas认证成功，获取用户信息
        AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();

        String userId = principal.getName();
        String url = request.getRequestURL().toString();
        Cookie[] cookies = request.getCookies();
        String ticket = null;
        for (Cookie cookie : cookies) {
            if ("JSESSIONID".equals(cookie.getName())) {
                ticket = cookie.getValue();
            }
        }


        System.out.println("11111111111");
        System.out.println(ticket);
        System.out.println(url);
        System.out.println(userId);
        String urll = "http://localhost/sso/integrationConfig/loginCas?url="
                + url + "&userId=" + userId + "&token=" + ticket;

        response.sendRedirect(url);

    }
}
