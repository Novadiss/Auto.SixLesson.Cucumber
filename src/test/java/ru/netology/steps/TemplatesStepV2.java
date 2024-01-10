package ru.netology.steps;

import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;

import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;

public class TemplatesStepV2 {

    private static LoginPage loginPage;
    private static DashboardPage dashboardPage;
    private static VerificationPage verificationPage;

    CardInfo transferFrom;

    @Пусть("пользователь залогинен с именем {string} и паролем {string}")
    public void openTransferPage(String login, String password) {
        loginPage = open("http://localhost:9999", LoginPage.class);
        verificationPage = loginPage.validLogin(login, password);
        dashboardPage = verificationPage.validVerify("12345");
    }

    @Когда("когда пользователь переводит {string} рублей с карты с номером {string} на свою {string} карту с главной страницы")
    public void transferFromFirstToSecond(String amount, String cardFrom, String cardTo) {
        if (Objects.equals(cardFrom, "5559 0000 0000 0002")) {
            transferFrom = getSecondCardInfo();
        }
        if (Objects.equals(cardFrom, "5559 0000 0000 0001")) {
            transferFrom = getFirstCardInfo();
        }
        var transferPage = dashboardPage.selectCardToTransfer(Integer.parseInt(cardTo) - 1);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), transferFrom);

    }

    @Тогда("тогда баланс его {string} карты из списка на главной странице должен стать {string} рублей")
    public void actualBalanceFirstCard(String actualCard, String balance) {
        int expectedBalanceFirstCard = Integer.parseInt(balance);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(Integer.parseInt(actualCard) - 1);
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
    }
}
