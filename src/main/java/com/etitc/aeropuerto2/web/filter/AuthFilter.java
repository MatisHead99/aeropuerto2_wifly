package com.etitc.aeropuerto2.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Protege todas las rutas /app/*. Así cada servlet nuevo queda protegido
 * sin repetir la validación de sesión.
 */
@WebFilter("/app/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, jakarta.servlet.ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession sesion = req.getSession(false);

        if (sesion == null || sesion.getAttribute("usuarioLogueado") == null) {
            res.sendRedirect(req.getContextPath() + "/login?error=2");
            return;
        }
        chain.doFilter(request, response);
    }
}
