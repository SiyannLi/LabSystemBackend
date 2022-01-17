package com.example.LabSystemBackend.jwt;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName="AuthorizationFilter",urlPatterns= {"user/login/*"})
public class AuthorizationFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {



        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final HttpServletResponse res = (HttpServletResponse) servletResponse;
        res.setCharacterEncoding("UTF-8");

        String token = req.getHeader("Authorization");
        if ("OPTIONS".equals(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
        } else {

            if (token == null || "".equals(token)) {

                req.setAttribute("verification result", "not logged in");

                filterChain.doFilter(req, res);
                return;
            }

            HttpSession session = req.getSession();
            String tokenServer = (String) session.getAttribute("token");

            if (JwtUtil.verify(token) || !token.equals(tokenServer)) {
                req.setAttribute("verification result", "wrong token");

                filterChain.doFilter(req, res);
                return;
            }


            servletRequest.setAttribute("verification result", "logged in");
        }
        filterChain.doFilter(req, res);
    }
}
