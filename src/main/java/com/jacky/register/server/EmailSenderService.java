package com.jacky.register.server;

import com.jacky.register.dataHandle.LoggerHandle;
import com.jacky.register.err.HighRepeatSendingEmailException;
import com.jacky.register.models.database.Term.Exam;
import com.jacky.register.models.database.Term.ExamCycle;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.register.Student;
import com.jacky.register.server.dbServers.register.RegisterCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailSenderService {
    static final String from = "imagestorage@126.com";

    static final ConcurrentHashMap<String, LocalDateTime> lastSendTime = new ConcurrentHashMap<>();
    static final int maxWaitTime = 60;

    @Autowired
    JavaMailSender mailSender;
    @Autowired
    RegisterCollectionService collectionService;

    @Value("${server.port}")
    private int serverPort;

    final String address;

    LoggerHandle logger = LoggerHandle.newLogger(EmailSenderService.class);

    public EmailSenderService() {
        String address1;
        InetAddress address2;
        try {
            address2 = InetAddress.getLocalHost();
            address1 = address2.getHostAddress();
        } catch (UnknownHostException e) {
            address1 = "127.0.0.1";
        }
        address = address1;
    }

    public void sendExamEnterConfirmMail(Student student, Exam exam, ExamCycle cycle, GroupDepartment department) {
        RegisterCollectionService.ConfirmToken token = new RegisterCollectionService.ConfirmToken();
        token.examId = exam.id;
        token.studentId = student.id;

        var message = String.format(
                "%s 你好，你参与的`%s`部门开展的`%s`可以确认参与考核了\n" +
                        "请访问以下链接以进行确认：\n" +
                        "`http://%s:%s/api/examCycle/collection/exam/confirm/%s`",
                student.name, department.name, cycle.name, address, serverPort,
                collectionService.generateConfirmToken(token)
        );
        sendMail("参与考核确认邮件", message, false, student.email);
    }

    public void sendMessage(Student student, String message, String title) {
        sendMail(title, message, false, student.email);
    }

    void sendMail(String title, String sendMessage, boolean htmlType, String to) {
        //check time out
        if (lastSendTime.containsKey(to)) {
            var time = lastSendTime.get(to);
            if (time.plusSeconds(maxWaitTime).isBefore(LocalDateTime.now()))
                throw new HighRepeatSendingEmailException();
            else
                lastSendTime.put(to, LocalDateTime.now());
        } else {
            lastSendTime.put(to, LocalDateTime.now());
        }

        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, "UTF-8");
        try {
            helper.setSubject(title);
            helper.setText(sendMessage, htmlType);

            helper.setFrom(from);
            helper.setTo(to);

            logger.info(String.format("send email to <%s> success [%s]", to, title));
            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error(String.format("send email to <%s> failure [%s]", to, title), e);
        }

    }
}
