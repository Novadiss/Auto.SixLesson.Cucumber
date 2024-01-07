package ru.netology.steps;

import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import ru.netology.data.DataHelper;
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

    CardInfo firstCardInfo;
    CardInfo secondCardInfo;
    int firstCardBalance;
    int secondCardBalance;

    @Пусть("пользователь залогинен с именем {string} и паролем {string}")
    public void openTransferPage(String login, String password) {
        loginPage = open("http://localhost:9999", LoginPage.class);
        verificationPage = loginPage.validLogin(login, password);
        dashboardPage = verificationPage.validVerify("12345");
    }

    @Когда("когда пользователь переводит {string} рублей с карты с номером {string} на свою 1 карту с главной страницы")
    public void transferFromFirstToSecond(String amount, String cardNum) {
        //var amount = 5000;
        firstCardInfo = getFirstCardInfo();
        secondCardInfo = getSecondCardInfo();
        firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        if (Objects.equals(cardNum, "5559 0000 0000 0002")) {
            var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
            dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCardInfo);
        } else {
            var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
            dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        }
    }

    @Тогда("тогда баланс его 1 карты из списка на главной странице должен стать {string} рублей")
    public void actualBalanceFirstCard(String balance) {
        int expectedBalanceFirstCard = Integer.parseInt(balance);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(getFirstCardInfo());
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
    }
}
