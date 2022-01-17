package com.example.LabSystemBackend.jwt;

import com.google.common.base.Splitter;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebFilter(filterName="AuthorizationFilter", urlPatterns = {"/**"})
public class AuthorizationFilter implements Filter {

    @Value("${filter.config.excludeUrls}")
    private String excludeUrls; // 获取配置文件中不需要过滤的uri

    private List<String> excludes;

    @Override
    public void init(FilterConfig filterConfig) {
        // 初始化
        // 移除配置文件中不过滤url，字符串的前空白和尾部空白
        excludes = Splitter.on(",").trimResults().splitToList(this.excludeUrls);
    }



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {



        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final HttpServletResponse res = (HttpServletResponse) servletResponse;
        String uri = req.getRequestURI(); //获取请求uri
        res.setCharacterEncoding("UTF-8");

        String token = req.getHeader("Authorization");
        if (this.isExcludesUrl(uri)) { // 判断请求uri是否需要过滤（方法在下面）
            filterChain.doFilter(req, res); // 不需要，放行
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
    private boolean isExcludesUrl(String path) {
        for (String v : this.excludes) {
            if (path.startsWith(v)) {// 判断请求uri 是否满足配置文件uri要求
                return true;  // 满足、也就是请求uri 为 登录、注册，返回true
            }
        }
        return false; // 不满足、也就是请求uri 不是登录、注册，返回false
    }

}
