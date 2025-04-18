package org.empik.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.util.Map;

@Service
public class GeoService {

    private static final String PUBLIC_IP_URL = "https://api.ipify.org?format=json";
    private static final String GEO_URL = "http://ip-api.com/json/{ip}?fields=country";

    private final RestTemplate rest = new RestTemplate();

    public String resolveCountry(String rawIp) {
        try {
            String ip = firstIp(rawIp);

            if (isLocalOrPrivate(ip)) {
                ip = fetchOwnPublicIp();
            }

            var geo = rest.getForObject(GEO_URL, Map.class, ip);
            return geo != null && geo.containsKey("country") ? geo.get("country").toString() : "Unknown";

        } catch (Exception e) {
            return "Unknown";
        }
    }

    private String fetchOwnPublicIp() {
        var resp = rest.getForObject(PUBLIC_IP_URL, Map.class);
        return resp != null ? resp.get("ip").toString() : "127.0.0.1";
    }

    private String firstIp(String ipList) {
        if (ipList == null) return "";
        int comma = ipList.indexOf(',');
        return (comma > 0 ? ipList.substring(0, comma) : ipList).trim();
    }

    private boolean isLocalOrPrivate(String ip) {
        try {
            var a = InetAddress.getByName(ip);
            return a.isAnyLocalAddress() || a.isLoopbackAddress() || a.isSiteLocalAddress();
        } catch (Exception e) {
            return true;
        }
    }
}