package com.cooksys.facebook.beans.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.cooksys.facebook.beans.AuthenticationBean;

@Component
public class AuthenticationFilter extends GenericFilterBean {
	private Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

	@Autowired
	private AuthenticationBean auth;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		log.info("AuthenticationFilter.doFilter(...)");

		if (auth.isLoggedIn()) {
			log.info("auth.isLoggedIn() = true");
			log.info("chain.doFilter(request, response)");
			chain.doFilter(request, response);
		} else {
			log.info("auth.isLoggedIn() = false");
			if (response instanceof HttpServletResponse) {
				log.info("response instanceof HttpServletResponse)");
				log.info("sendRedirect('/facebook/login.xhtml')");
				((HttpServletResponse) response).sendRedirect(String.format(
						"%s/login.xhtml",
						((HttpServletRequest) request).getContextPath()));
			}
		}
	}
}
