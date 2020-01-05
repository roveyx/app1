package com.cas.sso.app1.configuration;

import org.jasig.cas.client.authentication.DefaultGatewayResolverImpl;
import org.jasig.cas.client.authentication.GatewayResolver;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author guoyx
 * @since 2020-01-05 17:06
 */
public class ReAuthenticationFilter extends AbstractCasFilter {
    private String casServerLoginUrl;
    private boolean renew = false;

    private boolean gateway = false;

    private GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();

    // 不拦截的路径
    private String[] excludePaths;

    @Override
    protected void initInternal(FilterConfig filterConfig)throws ServletException {
        if (!isIgnoreInitConfiguration()) {
            super.initInternal(filterConfig);
            setCasServerLoginUrl(getPropertyFromInitParams(filterConfig,
                    "casServerLoginUrl", null));
            this.log.trace("Loaded CasServerLoginUrl parameter: "
                    + this.casServerLoginUrl);
            setRenew(parseBoolean(getPropertyFromInitParams(filterConfig,
                    "renew", "false")));
            this.log.trace("Loaded renew parameter: " + this.renew);
            setGateway(parseBoolean(getPropertyFromInitParams(filterConfig,
                    "gateway", "false")));
            this.log.trace("Loaded gateway parameter: " + this.gateway);

            String gatewayStorageClass = getPropertyFromInitParams(
                    filterConfig, "gatewayStorageClass", null);

            if (gatewayStorageClass != null) {
                try {
                    this.gatewayStorage = ((GatewayResolver) Class.forName(
                            gatewayStorageClass).newInstance());
                } catch (Exception e) {
                    this.log.error(e, e);
                    throw new ServletException(e);
                }
            }
        }
        //拦截器过滤修改************begin*************************
        String _excludePaths = getPropertyFromInitParams(filterConfig, "exceptPaths", null);
        if(CommonUtils.isNotBlank(_excludePaths)){
            excludePaths = _excludePaths.trim().split(",");
        }
        //拦截器过滤修改************end************************
    }

    @Override
    public void init() {
        super.init();
        CommonUtils.assertNotNull(this.casServerLoginUrl,"casServerLoginUrl cannot be null.");
    }

    @Override
    public final void doFilter(ServletRequest servletRequest,
                               ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        Assertion assertion = session != null ? (Assertion) session.getAttribute("_const_cas_assertion_") : null;

        // 拦截器过滤修改************begin********************
        String uri = request.getRequestURI();
        if (excludePaths != null && excludePaths.length > 0 && uri != null) {
            for (String path : excludePaths) {
                if (CommonUtils.isNotBlank(path)) {
                    if (uri.contains(path)) {
                        filterChain.doFilter(request, response);
                        return;
                    }
                }
            }
        }
        // 拦截器过滤修改************end********************************

        if (assertion != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String serviceUrl = constructServiceUrl(request, response);
        String ticket = CommonUtils.safeGetParameter(request,
                getArtifactParameterName());
        boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(request,
                serviceUrl);

        if ((CommonUtils.isNotBlank(ticket)) || (wasGatewayed)) {
            filterChain.doFilter(request, response);
            return;
        }

        this.log.debug("no ticket and no assertion found");
        String modifiedServiceUrl;
        if (this.gateway) {
            this.log.debug("setting gateway attribute in session");
            modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
        } else {
            modifiedServiceUrl = serviceUrl;
        }

        if (this.log.isDebugEnabled()) {
            this.log.debug("Constructed service url: " + modifiedServiceUrl);
        }

        String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl, getServiceParameterName(),
                modifiedServiceUrl, this.renew, this.gateway);

        if (this.log.isDebugEnabled()) {
            this.log.debug("redirecting to \"" + urlToRedirectTo + "\"");
        }

        response.sendRedirect(urlToRedirectTo);
    }

    public final void setRenew(boolean renew) {
        this.renew = renew;
    }

    public final void setGateway(boolean gateway) {
        this.gateway = gateway;
    }

    public final void setCasServerLoginUrl(String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }

    public final void setGatewayStorage(GatewayResolver gatewayStorage) {
        this.gatewayStorage = gatewayStorage;
    }

    public String[] getExcludePaths() {
        return excludePaths;
    }

    public void setExcludePaths(String[] excludePaths) {
        this.excludePaths = excludePaths;
    }
}
