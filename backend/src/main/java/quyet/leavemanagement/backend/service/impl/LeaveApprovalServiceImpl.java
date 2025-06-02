package quyet.leavemanagement.backend.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import quyet.leavemanagement.backend.dto.response.leave_request.LeaveRequestResponse;
import quyet.leavemanagement.backend.entity.Employee;
import quyet.leavemanagement.backend.entity.RequestLeave;
import quyet.leavemanagement.backend.entity.RequestStatus;
import quyet.leavemanagement.backend.entity.User;
import quyet.leavemanagement.backend.exception.AppException;
import quyet.leavemanagement.backend.exception.ErrorCode;
import quyet.leavemanagement.backend.repository.RequestLeaveRepository;
import quyet.leavemanagement.backend.repository.RequestStatusRepository;
import quyet.leavemanagement.backend.repository.UserRepository;
import quyet.leavemanagement.backend.service.EmployeeService;
import quyet.leavemanagement.backend.service.LeaveApprovalService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaveApprovalServiceImpl implements LeaveApprovalService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveApprovalServiceImpl.class);

    UserRepository userRepository;
    RequestLeaveRepository requestLeaveRepository;
    EmployeeService employeeService;
    RequestStatusRepository requestStatusRepository;
    JavaMailSender mailSender;

    @Override
    @PreAuthorize("hasAuthority('VIEW_SUB_REQUEST')")
    public Page<LeaveRequestResponse> filterAllLeaveRequests(
            String startCreatedAt, String endCreatedAt,
            String leaveDateStart, String leaveDateEnd,
            int leaveTypeId, int statusId,
            String employeeName, Pageable pageable) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(auth.getName());
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Long managerId = user.getEmployee().getEmpId();

        List<Long> subordinateIds = employeeService.getSubordinateEmployeeIds(managerId);
        if (subordinateIds.isEmpty()) {
            return Page.empty(pageable);
        }

        LocalDateTime startCreated = null;
        LocalDateTime endCreated = null;

        if (StringUtils.hasText(startCreatedAt)) {
            startCreated = LocalDateTime.parse(startCreatedAt, DateTimeFormatter.ISO_DATE_TIME);
        }
        if (StringUtils.hasText(endCreatedAt)) {
            endCreated = LocalDateTime.parse(endCreatedAt, DateTimeFormatter.ISO_DATE_TIME);
        }

        LocalDate leaveStart = null, leaveEnd = null;
        if (StringUtils.hasText(leaveDateStart)) {
            leaveStart = LocalDate.parse(leaveDateStart, DateTimeFormatter.ISO_DATE);
        }
        if (StringUtils.hasText(leaveDateEnd)) {
            leaveEnd = LocalDate.parse(leaveDateEnd, DateTimeFormatter.ISO_DATE);
        }

        String employeeNameFilter = StringUtils.hasText(employeeName) ? "%" + employeeName + "%" : null;

        Page<RequestLeave> leaveRequests = requestLeaveRepository.findAllLeaveRequestsForManager(
                startCreated, endCreated, leaveStart, leaveEnd, leaveTypeId, statusId,
                subordinateIds, employeeNameFilter, pageable
        );

        return leaveRequests.map(requestLeave -> LeaveRequestResponse.builder()
                .idRequest(requestLeave.getIdRequest())
                .title(requestLeave.getTitle())
                .reason(requestLeave.getReason())
                .fromDate(requestLeave.getFromDate())
                .toDate(requestLeave.getToDate())
                .noteProcess(requestLeave.getNoteProcess())
                .nameTypeLeave(requestLeave.getTypeLeave().getNameTypeLeave())
                .nameRequestStatus(requestLeave.getRequestStatus().getStatusName())
                .employeeCreated(requestLeave.getEmployeeCreated().getFullName())
                .employeeProcess((requestLeave.getEmployeeProcess() != null) ?
                        requestLeave.getEmployeeProcess().getFullName() : null)
                .createdAt(requestLeave.getCreatedAt())
                .build());
    }

    @Override
    @PreAuthorize("hasAuthority('APPOVAL_REQUEST')")
    public LeaveRequestResponse processLeaveRequest(Integer idRequest, String noteProcess, String action) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(auth.getName());
        User currentUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Employee currentEmployee = currentUser.getEmployee();
        Long managerId = currentEmployee.getEmpId();

        RequestLeave requestLeave = requestLeaveRepository.findById(idRequest)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        List<Long> subordinateIds = employeeService.getSubordinateEmployeeIds(managerId);
        if (!subordinateIds.contains(requestLeave.getEmployeeCreated().getEmpId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (requestLeave.getFromDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.DO_NOT_EDIT_LEAVE_IN_THE_PASS);
        }

        RequestStatus newStatus;
        switch (action.toUpperCase()) {
            case "APPROVE":
                newStatus = requestStatusRepository.findById(2L)
                        .orElseThrow(() -> new AppException(ErrorCode.REQUEST_STATUS_NOT_FOUND));
                break;
            case "REJECT":
                newStatus = requestStatusRepository.findById(3L)
                        .orElseThrow(() -> new AppException(ErrorCode.REQUEST_STATUS_NOT_FOUND));
                break;
            default:
                throw new AppException(ErrorCode.INVALID_ACTION);
        }

        requestLeave.setRequestStatus(newStatus);
        requestLeave.setNoteProcess(noteProcess);
        requestLeave.setEmployeeProcess(currentEmployee);

        requestLeave = requestLeaveRepository.save(requestLeave);
        sendLeaveRequestStatusEmail(requestLeave);

        return LeaveRequestResponse.builder()
                .idRequest(requestLeave.getIdRequest())
                .title(requestLeave.getTitle())
                .reason(requestLeave.getReason())
                .fromDate(requestLeave.getFromDate())
                .toDate(requestLeave.getToDate())
                .noteProcess(requestLeave.getNoteProcess())
                .nameTypeLeave(requestLeave.getTypeLeave().getNameTypeLeave())
                .nameRequestStatus(requestLeave.getRequestStatus().getStatusName())
                .employeeCreated(requestLeave.getEmployeeCreated().getFullName())
                .employeeProcess(requestLeave.getEmployeeProcess() != null ?
                        requestLeave.getEmployeeProcess().getFullName() : null)
                .createdAt(requestLeave.getCreatedAt())
                .build();
    }

    private void sendLeaveRequestStatusEmail(RequestLeave requestLeave) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            User user = userRepository.findByEmployee(requestLeave.getEmployeeCreated())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            String employeeEmail = user.getEmail();
            if (!StringUtils.hasText(employeeEmail)) {
                throw new AppException(ErrorCode.EMAIL_NOT_FOUND);
            }

            ClassPathResource resource = new ClassPathResource("templates/leaveRequestStatusEmail.html");
            String emailTemplate = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);

            String status = requestLeave.getRequestStatus().getStatusName();
            emailTemplate = emailTemplate
                    .replace("{employeeName}", requestLeave.getEmployeeCreated().getFullName())
                    .replace("{createdAt}", requestLeave.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                    .replace("{status}", status.toUpperCase())
                    .replace("{statusColor}", status.equalsIgnoreCase("Approved") ? "#4CAF50" : "#F44336")
                    .replace("{title}", requestLeave.getTitle())
                    .replace("{fromDate}", requestLeave.getFromDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                    .replace("{toDate}", requestLeave.getToDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                    .replace("{reason}", requestLeave.getReason())
                    .replace("{processedBy}", requestLeave.getEmployeeProcess().getFullName())
                    .replace("{noteProcess}", requestLeave.getNoteProcess() != null ? requestLeave.getNoteProcess() : "N/A");

            logger.info(emailTemplate);

            helper.setTo(employeeEmail);
            helper.setSubject("Leave Request Status Update - " + status.toUpperCase());
            helper.setText(emailTemplate, true);

            mailSender.send(mimeMessage);
            logger.info("Email sent successfully to {} for request ID: {}", employeeEmail, requestLeave.getIdRequest());
        } catch (MessagingException e) {
            logger.error("MessagingException while sending email for request {}: {}", requestLeave.getIdRequest(), e.getMessage(), e);
        } catch (IOException e) {
            logger.error("IOException while sending email for request {}: {}", requestLeave.getIdRequest(), e.getMessage(), e);
        }
    }
}
