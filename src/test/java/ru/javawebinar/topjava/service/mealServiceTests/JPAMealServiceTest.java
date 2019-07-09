package ru.javawebinar.topjava.service.mealServiceTests;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import static ru.javawebinar.topjava.Profiles.JPA;

@ActiveProfiles(JPA)
public class JPAMealServiceTest extends AbstractMealServiceTest {
}
