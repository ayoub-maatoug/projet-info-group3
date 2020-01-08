package com.example.repository;

import com.example.model.Status;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class LightDaoCustomImplTest {

    @Autowired
    private LightDao lightDao;

    @Test
    public void shouldFindOnLights() {
        Assertions.assertThat(lightDao.findOnLights())
                .hasSize(2)
                .extracting("id", "status")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(3, Status.ON),
                        Tuple.tuple(1, Status.ON)
                );
    }
}