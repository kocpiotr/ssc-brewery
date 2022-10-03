/*
 *  Copyright 2020 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.*;
import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.*;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;


/**
 * Created by jt on 2019-01-26.
 */
@RequiredArgsConstructor
@Component
public class DefaultBreweryLoader implements CommandLineRunner {

    public static final String TASTING_ROOM = "Tasting Room";
    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    private final BreweryRepository breweryRepository;
    private final BeerRepository beerRepository;
    private final BeerInventoryRepository beerInventoryRepository;
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) {
        loadBreweryData();
        loadCustomerData();
        loadSecurityData();
    }

    private void loadSecurityData() {
       if(!userRepository.findAll().isEmpty()) {
           return;
       }

        final Authority adminAuthority = Authority.builder().role("ADMIN").build();
        final Authority userAuthority = Authority.builder().role("USER").build();
        final Authority customerAuthority = Authority.builder().role("CUSTOMER").build();

        authorityRepository.saveAll(Arrays.asList(adminAuthority, userAuthority, customerAuthority));

        final User springUser = User.builder()
                .password("{bcrypt}$2a$10$7tYAvVL2/KwcQTcQywHIleKueg4ZK7y7d44hKyngjTwHCDlesxdla")
                .username("spring")
                .authority(adminAuthority).build();
        final User scottUser = User.builder()
                .password("{bcrypt10}$2a$10$jv7rEbL65k4Q3d/mqG5MLuLDLTlg5oKoq2QOOojfB3e2awo.nlmgu")
                .username("scott")
                .authority(customerAuthority).build();
        final User userUser = User.builder()
                .password("{sha256}1296cefceb47413d3fb91ac7586a4625c33937b4d3109f5a4dd96c79c46193a029db713b96006ded")
                .username("user")
                .authority(userAuthority).build();

        userRepository.saveAll(Arrays.asList(springUser, scottUser, userUser));
    }

    private void loadCustomerData() {
        Customer tastingRoom = Customer.builder().customerName(TASTING_ROOM).apiKey(UUID.randomUUID()).build();

        customerRepository.save(tastingRoom);

        beerRepository.findAll().forEach(beer -> {
            beerOrderRepository.save(BeerOrder.builder().customer(tastingRoom).orderStatus(OrderStatusEnum.NEW).beerOrderLines(Set.of(BeerOrderLine.builder().beer(beer).orderQuantity(2).build())).build());
        });
    }

    private void loadBreweryData() {
        if (breweryRepository.count() == 0) {
            breweryRepository.save(Brewery.builder().breweryName("Cage Brewing").build());

            Beer mangoBobs = Beer.builder().beerName("Mango Bobs").beerStyle(BeerStyleEnum.IPA).minOnHand(12).quantityToBrew(200).upc(BEER_1_UPC).build();

            beerRepository.save(mangoBobs);
            beerInventoryRepository.save(BeerInventory.builder().beer(mangoBobs).quantityOnHand(500).build());

            Beer galaxyCat = Beer.builder().beerName("Galaxy Cat").beerStyle(BeerStyleEnum.PALE_ALE).minOnHand(12).quantityToBrew(200).upc(BEER_2_UPC).build();

            beerRepository.save(galaxyCat);
            beerInventoryRepository.save(BeerInventory.builder().beer(galaxyCat).quantityOnHand(500).build());

            Beer pinball = Beer.builder().beerName("Pinball Porter").beerStyle(BeerStyleEnum.PORTER).minOnHand(12).quantityToBrew(200).upc(BEER_3_UPC).build();

            beerRepository.save(pinball);
            beerInventoryRepository.save(BeerInventory.builder().beer(pinball).quantityOnHand(500).build());

        }
    }
}
