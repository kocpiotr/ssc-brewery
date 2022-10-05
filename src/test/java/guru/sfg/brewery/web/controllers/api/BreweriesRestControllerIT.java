package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by jt on 6/13/20.
 */
@SpringBootTest
public class BreweriesRestControllerIT extends BaseIT {

    @Test
    void listBeerHttpBasicWithCustomerRole() throws Exception{
        mockMvc.perform(get("/api/v1/breweries")
                        .with(httpBasic("scott", "tiger")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void listBeerHttpBasicWithAdminRole() throws Exception{
        mockMvc.perform(get("/api/v1/breweries")
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void listBeerHttpBasicWithUserRole() throws Exception{
        mockMvc.perform(get("/api/v1/breweries")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void listBeerHttpBasicByNotAuthenticated() throws Exception{
        mockMvc.perform(get("/api/v1/breweries"))
                .andExpect(status().isUnauthorized());
    }

}
