package com.canoo.dolphin.server.context;

import com.canoo.dolphin.impl.PlatformConstants;
import com.canoo.dolphin.server.DolphinSessionListener;
import com.canoo.dolphin.server.config.DolphinPlatformConfiguration;
import com.canoo.dolphin.server.container.ContainerManager;
import com.canoo.dolphin.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DolphinContextFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(DolphinContextFilter.class);

    private final DolphinPlatformConfiguration configuration;

    private final ContainerManager containerManager;

    private final DolphinContextFactory dolphinContextFactory;

    private final DolphinSessionListenerProvider dolphinSessionListenerProvider;

    public DolphinContextFilter(final DolphinPlatformConfiguration configuration, final ContainerManager containerManager, final DolphinContextFactory dolphinContextFactory, final DolphinSessionListenerProvider dolphinSessionListenerProvider) {
        this.configuration = Assert.requireNonNull(configuration, "configuration");
        this.containerManager = Assert.requireNonNull(containerManager, "containerManager");
        this.dolphinContextFactory = Assert.requireNonNull(dolphinContextFactory, "dolphinContextFactory");
        this.dolphinSessionListenerProvider = Assert.requireNonNull(dolphinSessionListenerProvider, "dolphinSessionListenerProvider");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        DolphinContextUtils.setContextForCurrentThread(null);
        try {
            final HttpServletRequest servletRequest = (HttpServletRequest) request;
            final HttpServletResponse servletResponse = (HttpServletResponse) response;
            final HttpSession httpSession = Assert.requireNonNull(servletRequest.getSession(), "request.getSession()");

            DolphinContext dolphinContext = null;
            final String clientId = servletRequest.getHeader(PlatformConstants.CLIENT_ID_HTTP_HEADER_NAME);
            if (clientId == null || clientId.trim().isEmpty()) {
                if(DolphinContextUtils.getOrCreateContextMapInSession(httpSession).size() >= configuration.getMaxClientsPerSession()) {
                    servletResponse.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Maximum size for clients in session is reached");
                    LOG.info("Maximum size for clients in session " + servletRequest.getSession().getId() +" is reached");
                    return;
                }
                dolphinContext = createNewContext(httpSession);
                DolphinContextUtils.storeInSession(servletRequest.getSession(), dolphinContext);
                for(DolphinSessionListener listener : dolphinSessionListenerProvider.getAllListeners()) {
                    listener.sessionCreated(dolphinContext.getCurrentDolphinSession());
                }
                LOG.trace("Created new DolphinContext {} in http session {}", dolphinContext.getId(), httpSession.getId());
            } else {
                dolphinContext = DolphinContextUtils.getClientInSession(servletRequest.getSession(), clientId);
                if(dolphinContext == null) {
                    LOG.warn("Can not find requested client for id " + clientId);
                    servletResponse.sendError(HttpServletResponse.SC_REQUEST_TIMEOUT, "Can not find requested client!");
                    return;
                }
            }
            DolphinContextUtils.setContextForCurrentThread(dolphinContext);
            chain.doFilter(request, response);
            servletResponse.setHeader(PlatformConstants.CLIENT_ID_HTTP_HEADER_NAME, dolphinContext.getId());
        } finally {
            DolphinContextUtils.setContextForCurrentThread(null);
        }
    }

    private DolphinContext createNewContext(final HttpSession httpSession) {
        Assert.requireNonNull(httpSession, "httpSession");
        return dolphinContextFactory.create(httpSession, dolphinSessionListenerProvider);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //Nothing to do here
    }

    @Override
    public void destroy() {
    }
}