package ru.javawebinar.topjava.service.userServiceTest;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.JPA;

@ActiveProfiles(JPA)
public class JPAUserServiceTest extends AbstractUserServiceTest {
}
