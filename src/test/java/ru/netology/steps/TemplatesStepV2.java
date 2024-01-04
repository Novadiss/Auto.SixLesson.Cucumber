package ru.netology.steps;

import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;
import ru.netology.page.VerificationPage;

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

    @Когда("когда пользователь переводит 5000 рублей с карты с номером 5559 0000 0000 0002 на свою 1 карту с главной страницы")
    public void transferFromFirstToSecond() {
        var amount = 5000;
        firstCardInfo = getFirstCardInfo();
        secondCardInfo = getSecondCardInfo();
        firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCardInfo);
    }

    @Тогда("тогда баланс его 1 карты из списка на главной странице должен стать 15000 рублей")
    public void actualBalanceFirstCard() {
        var expectedBalanceFirstCard = 15_000;
        var actualBalanceFirstCard = dashboardPage.getCardBalance(DataHelper.getFirstCardInfo());
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
    }
}
