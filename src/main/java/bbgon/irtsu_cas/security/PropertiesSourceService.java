package bbgon.irtsu_cas.security;

import bbgon.irtsu_cas.dto.response.PropertiesResponse;
import org.springframework.stereotype.Service;

@Service
public class PropertiesSourceService {

    public PropertiesResponse getProperties() {
        PropertiesResponse response = new PropertiesResponse();
        String[] data = { "Apple", "Banana", "Orange" };
        response.setGroup(data);
        response.setStatus(data);
        response.setOrderStatus(data);
        return response;
    }
}
