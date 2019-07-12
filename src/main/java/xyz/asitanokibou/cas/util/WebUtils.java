package xyz.asitanokibou.cas.util;

import javax.servlet.http.HttpServletRequest;


/**
 * @author aimysaber@gmail.com
 */
public class WebUtils {

    public static String getBasePath(HttpServletRequest request) {
        return request.getScheme() + "://" +
                request.getServerName() +
                (request.getServerPort() == 80 ? "" : ":" + request.getServerPort()) +
                request.getContextPath();
    }
}
