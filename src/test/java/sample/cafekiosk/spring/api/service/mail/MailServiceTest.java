package sample.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)//@Mock 으로 객체를 만드는 경우 꼭 달아줘야한다.
class MailServiceTest {


//    @Spy //사용 시 실제 객체를 기반한다. stubbing 한 객체의 기능만 지정한 대로 동작하고 나머지는 실제 객체대로 동작한다.
    @Mock
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService; //생성자에 있는 애들을 @Mock 달려 있는 애들 가져온다.

    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail(){
        //given


//        MailSendClient mailSendClient = Mockito.mock(MailSendClient.class);
//        MailSendHistoryRepository mailSendHistoryRepository = Mockito.mock(MailSendHistoryRepository.class);

        //spy 사용시 이렇게 사용하면 안된다.
        when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

//        doReturn(true) //spy 사용 시에는 위 처럼 사용이 안되어서 이렇게 사용해야한다.
//                .when(mailSendClient)
//                .sendEmail(anyString(),anyString(),anyString(),anyString());

//        when(mailSendHistoryRepository.save(any()))
//                .thenReturn(true);


//        MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

        //when
        boolean result = mailService.sendMail("", "", "", "");
        //then
        assertThat(result).isTrue();
        verify(mailSendHistoryRepository,times(1)).save(any(MailSendHistory.class));
    }
}