package quyet.leavemanagement.backend.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.response.agenda.SimpleEmployeeStatusResponse;
import quyet.leavemanagement.backend.entity.Employee;
import quyet.leavemanagement.backend.entity.RequestLeave;
import quyet.leavemanagement.backend.entity.User;
import quyet.leavemanagement.backend.exception.AppException;
import quyet.leavemanagement.backend.exception.ErrorCode;
import quyet.leavemanagement.backend.repository.RequestLeaveRepository;
import quyet.leavemanagement.backend.repository.UserRepository;
import quyet.leavemanagement.backend.service.AgendaService;
import quyet.leavemanagement.backend.service.EmployeeService;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class AgendaServiceImpl implements AgendaService {
    EmployeeService employeeService;
    RequestLeaveRepository requestLeaveRepository;
    UserRepository userRepository;
    private static final String MONTH_VIEW = "month";
    private static final String EMPLOYEE_HEADER = "Employee";
    private static final String DEPARTMENT_HEADER = "Department";
    private static final String NO_LEAVE_STATUS = "No Leave";

    @Override
    @PreAuthorize("hasAuthority('VIEW_AGENDA')")
    public List<SimpleEmployeeStatusResponse> getSimpleEmployeeStatus(LocalDate startDate, LocalDate endDate, String viewMode) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(auth.getName());
        User currentUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));


        LocalDate adjustedStartDate;
        if (MONTH_VIEW.equals(viewMode)) {
            adjustedStartDate = startDate.withDayOfMonth(1);
        } else if ("year".equals(viewMode)) {
            adjustedStartDate = startDate.withDayOfYear(1);
        } else {
            adjustedStartDate = startDate;
        }

        final LocalDate adjustedEndDate;
        if (MONTH_VIEW.equals(viewMode)) {
            adjustedEndDate = endDate.withDayOfMonth(endDate.lengthOfMonth());
        } else {
            if ("year".equals(viewMode)) adjustedEndDate = endDate.withDayOfYear(endDate.lengthOfYear());
            else adjustedEndDate = endDate;
        }

        List<Employee> subordinates = employeeService.getAllSubordinateEmployees(currentUser.getEmployee().getEmpId());
        if (subordinates.isEmpty()) {
            return new ArrayList<>();
        }

        List<SimpleEmployeeStatusResponse> result = new ArrayList<>();
        for (Employee emp : subordinates) {
            List<RequestLeave> leaveRequests = requestLeaveRepository.findByEmployeeCreatedAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                    emp, adjustedEndDate, adjustedStartDate);

            List<String> leaveDays = leaveRequests.stream()
                    .filter(req -> req.getRequestStatus().getStatusName().equals("Approved"))
                    .flatMap(req -> req.getFromDate().datesUntil(req.getToDate().plusDays(1)))
                    .filter(date -> !date.isBefore(adjustedStartDate) && !date.isAfter(adjustedEndDate))
                    .map(LocalDate::toString)
                    .distinct()
                    .collect(Collectors.toList());

            SimpleEmployeeStatusResponse response = SimpleEmployeeStatusResponse.builder()
                    .id(emp.getEmpId())
                    .name(emp.getFullName())
                    .departmentName(emp.getDepartment() != null ? emp.getDepartment().getDepName() : "N/A") // Lấy tên phòng ban
                    .leaveDays(leaveDays)
                    .build();

            result.add(response);
        }
        return result;
    }


    @Override
    @PreAuthorize("hasAuthority('VIEW_AGENDA')")
    public ByteArrayInputStream exportToExcel(LocalDate startDate, LocalDate endDate) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(auth.getName());

        // Tạo workbook
        Workbook workbook = new XSSFWorkbook();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter fileDateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String currentDateTime = LocalDateTime.now().format(fileDateTimeFormatter);
        String fileName = "export_" + currentDateTime + "_" + userId + "_report.xlsx";

        // Tạo style
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setColor(IndexedColors.WHITE.getIndex());

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setBorderBottom(BorderStyle.MEDIUM);
        titleStyle.setBorderTop(BorderStyle.MEDIUM);
        titleStyle.setBorderLeft(BorderStyle.MEDIUM);
        titleStyle.setBorderRight(BorderStyle.MEDIUM);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        CellStyle offStyle = workbook.createCellStyle();
        offStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        offStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        offStyle.setAlignment(HorizontalAlignment.CENTER);
        offStyle.setBorderBottom(BorderStyle.THIN);
        offStyle.setBorderTop(BorderStyle.THIN);
        offStyle.setBorderLeft(BorderStyle.THIN);
        offStyle.setBorderRight(BorderStyle.THIN);

        CellStyle noLeaveStyle = workbook.createCellStyle();
        noLeaveStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        noLeaveStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        noLeaveStyle.setAlignment(HorizontalAlignment.CENTER);
        noLeaveStyle.setBorderBottom(BorderStyle.THIN);
        noLeaveStyle.setBorderTop(BorderStyle.THIN);
        noLeaveStyle.setBorderLeft(BorderStyle.THIN);
        noLeaveStyle.setBorderRight(BorderStyle.THIN);

        // Sheet 1: Daily Status
        List<SimpleEmployeeStatusResponse> dayEmployees = getSimpleEmployeeStatus(startDate, endDate, "day");
        Sheet daySheet = workbook.createSheet("Daily Status");

        // Thêm tiêu đề chính
        Row dayTitleRow = daySheet.createRow(0);
        dayTitleRow.setHeight((short) (20 * 20)); // Tăng chiều cao hàng
        Cell titleCell = dayTitleRow.createCell(0);
        titleCell.setCellValue("Employee Leave Status Report (Daily) - " + LocalDate.now().format(dateFormatter));
        titleCell.setCellStyle(titleStyle);

        // Tính số cột thực tế để gộp
        int totalColumnsDay = 2; // Bắt đầu với 2 cột "Employee" và "Department"
        LocalDate tempDateIterator = startDate;
        while (!tempDateIterator.isAfter(endDate)) {
            totalColumnsDay++;
            tempDateIterator = tempDateIterator.plusDays(1);
        }
        daySheet.addMergedRegion(new CellRangeAddress(0, 0, 0, totalColumnsDay - 1));

        Row dayHeaderRow = daySheet.createRow(1);
        dayHeaderRow.setHeight((short) (15 * 20)); // Tăng chiều cao hàng header
        dayHeaderRow.createCell(0).setCellValue(EMPLOYEE_HEADER);
        dayHeaderRow.getCell(0).setCellStyle(headerStyle);
        dayHeaderRow.createCell(1).setCellValue(DEPARTMENT_HEADER);
        dayHeaderRow.getCell(1).setCellStyle(headerStyle);

        LocalDate currentDateIterator = startDate;
        int columnIndex = 2;
        while (!currentDateIterator.isAfter(endDate)) {
            Cell cell = dayHeaderRow.createCell(columnIndex++);
            cell.setCellValue(currentDateIterator.format(dateFormatter));
            cell.setCellStyle(headerStyle);
            currentDateIterator = currentDateIterator.plusDays(1);
        }

        // Thêm filter cho header
        daySheet.setAutoFilter(new CellRangeAddress(1, 1, 0, totalColumnsDay - 1));

        int dayRowIdx = 2;
        for (SimpleEmployeeStatusResponse emp : dayEmployees) {
            Row row = daySheet.createRow(dayRowIdx++);
            row.setHeight((short) (15 * 20)); // Tăng chiều cao hàng dữ liệu
            row.createCell(0).setCellValue(emp.getName());
            row.getCell(0).setCellStyle(dataStyle);
            row.createCell(1).setCellValue(emp.getDepartmentName());
            row.getCell(1).setCellStyle(dataStyle);

            currentDateIterator = startDate;
            columnIndex = 2;
            while (!currentDateIterator.isAfter(endDate)) {
                final LocalDate finalCurrentDate = currentDateIterator;
                Cell statusCell = row.createCell(columnIndex++);
                boolean isLeave = emp.getLeaveDays().stream()
                        .map(LocalDate::parse)
                        .anyMatch(date -> date.equals(finalCurrentDate));
                if (isLeave) {
                    statusCell.setCellValue("Off");
                    statusCell.setCellStyle(offStyle);
                } else {
                    statusCell.setCellValue(NO_LEAVE_STATUS);
                    statusCell.setCellStyle(noLeaveStyle);
                }
                currentDateIterator = currentDateIterator.plusDays(1);
            }
        }

        // Sheet 2: Monthly Status
        LocalDate monthStart = startDate.withDayOfMonth(1);
        LocalDate monthEnd = endDate.withDayOfMonth(endDate.lengthOfMonth());
        List<SimpleEmployeeStatusResponse> monthEmployees = getSimpleEmployeeStatus(monthStart, monthEnd, MONTH_VIEW);
        Sheet monthSheet = workbook.createSheet("Monthly Status");

        Row monthTitleRow = monthSheet.createRow(0);
        monthTitleRow.setHeight((short) (20 * 20));
        Cell monthTitleCell = monthTitleRow.createCell(0);
        monthTitleCell.setCellValue("Employee Leave Status Report (Monthly) - " + LocalDate.now().format(dateFormatter));
        monthTitleCell.setCellStyle(titleStyle);

        int totalColumnsMonth = 2;
        LocalDate tempMonthIterator = monthStart;
        while (!tempMonthIterator.isAfter(monthEnd)) {
            totalColumnsMonth++;
            tempMonthIterator = tempMonthIterator.plusMonths(1);
        }
        monthSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, totalColumnsMonth - 1));

        Row monthHeaderRow = monthSheet.createRow(1);
        monthHeaderRow.setHeight((short) (15 * 20));
        monthHeaderRow.createCell(0).setCellValue(EMPLOYEE_HEADER);
        monthHeaderRow.getCell(0).setCellStyle(headerStyle);
        monthHeaderRow.createCell(1).setCellValue(DEPARTMENT_HEADER);
        monthHeaderRow.getCell(1).setCellStyle(headerStyle);

        LocalDate currentMonth = monthStart;
        columnIndex = 2;
        while (!currentMonth.isAfter(monthEnd)) {
            Cell cell = monthHeaderRow.createCell(columnIndex++);
            cell.setCellValue(currentMonth.format(DateTimeFormatter.ofPattern("MM-yyyy")));
            cell.setCellStyle(headerStyle);
            currentMonth = currentMonth.plusMonths(1);
        }

        monthSheet.setAutoFilter(new CellRangeAddress(1, 1, 0, totalColumnsMonth - 1));

        int monthRowIdx = 2;
        for (SimpleEmployeeStatusResponse emp : monthEmployees) {
            Row row = monthSheet.createRow(monthRowIdx++);
            row.setHeight((short) (15 * 20));
            row.createCell(0).setCellValue(emp.getName());
            row.getCell(0).setCellStyle(dataStyle);
            row.createCell(1).setCellValue(emp.getDepartmentName());
            row.getCell(1).setCellStyle(dataStyle);

            currentMonth = monthStart;
            columnIndex = 2;
            while (!currentMonth.isAfter(monthEnd)) {
                final LocalDate finalCurrentMonth = currentMonth;
                Cell statusCell = row.createCell(columnIndex++);
                long leaveCount = emp.getLeaveDays().stream()
                        .map(LocalDate::parse)
                        .filter(date -> date.getMonth() == finalCurrentMonth.getMonth() && date.getYear() == finalCurrentMonth.getYear())
                        .count();
                if (leaveCount > 0) {
                    statusCell.setCellValue(leaveCount + " days");
                    statusCell.setCellStyle(offStyle);
                } else {
                    statusCell.setCellValue(NO_LEAVE_STATUS);
                    statusCell.setCellStyle(noLeaveStyle);
                }
                currentMonth = currentMonth.plusMonths(1);
            }
        }

        // Sheet 3: Yearly Status
        LocalDate yearStart = startDate.withDayOfYear(1);
        LocalDate yearEnd = endDate.withDayOfYear(endDate.lengthOfYear());
        List<SimpleEmployeeStatusResponse> yearEmployees = getSimpleEmployeeStatus(yearStart, yearEnd, "year");
        Sheet yearSheet = workbook.createSheet("Yearly Status");

        Row yearTitleRow = yearSheet.createRow(0);
        yearTitleRow.setHeight((short) (20 * 20));
        Cell yearTitleCell = yearTitleRow.createCell(0);
        yearTitleCell.setCellValue("Employee Leave Status Report (Yearly) - " + LocalDate.now().format(dateFormatter));
        yearTitleCell.setCellStyle(titleStyle);

        int totalColumnsYear = 2;
        LocalDate tempYearIterator = yearStart;
        while (!tempYearIterator.isAfter(yearEnd)) {
            totalColumnsYear++;
            tempYearIterator = tempYearIterator.plusYears(1);
        }
        yearSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, totalColumnsYear - 1));

        Row yearHeaderRow = yearSheet.createRow(1);
        yearHeaderRow.setHeight((short) (15 * 20));
        yearHeaderRow.createCell(0).setCellValue(EMPLOYEE_HEADER);
        yearHeaderRow.getCell(0).setCellStyle(headerStyle);
        yearHeaderRow.createCell(1).setCellValue(DEPARTMENT_HEADER);
        yearHeaderRow.getCell(1).setCellStyle(headerStyle);

        LocalDate currentYear = yearStart;
        columnIndex = 2;
        while (!currentYear.isAfter(yearEnd)) {
            Cell cell = yearHeaderRow.createCell(columnIndex++);
            cell.setCellValue(String.valueOf(currentYear.getYear()));
            cell.setCellStyle(headerStyle);
            currentYear = currentYear.plusYears(1);
        }

        yearSheet.setAutoFilter(new CellRangeAddress(1, 1, 0, totalColumnsYear - 1));

        int yearRowIdx = 2;
        for (SimpleEmployeeStatusResponse emp : yearEmployees) {
            Row row = yearSheet.createRow(yearRowIdx++);
            row.setHeight((short) (15 * 20));
            row.createCell(0).setCellValue(emp.getName());
            row.getCell(0).setCellStyle(dataStyle);
            row.createCell(1).setCellValue(emp.getDepartmentName());
            row.getCell(1).setCellStyle(dataStyle);

            currentYear = yearStart;
            columnIndex = 2;
            while (!currentYear.isAfter(yearEnd)) {
                final LocalDate finalCurrentYear = currentYear;
                Cell statusCell = row.createCell(columnIndex++);
                long leaveCount = emp.getLeaveDays().stream()
                        .map(LocalDate::parse)
                        .filter(date -> date.getYear() == finalCurrentYear.getYear())
                        .count();
                if (leaveCount > 0) {
                    statusCell.setCellValue(leaveCount + " days");
                    statusCell.setCellStyle(offStyle);
                } else {
                    statusCell.setCellValue(NO_LEAVE_STATUS);
                    statusCell.setCellStyle(noLeaveStyle);
                }
                currentYear = currentYear.plusYears(1);
            }
        }

        // Tự động điều chỉnh độ rộng cột cho tất cả sheet
        for (Sheet sheet : new Sheet[]{daySheet, monthSheet, yearSheet}) {
            int totalColumns;
            if (sheet == daySheet) {
                totalColumns = totalColumnsDay;
            } else if (sheet == monthSheet) {
                totalColumns = totalColumnsMonth;
            } else {
                totalColumns = totalColumnsYear;
            }
            for (int i = 0; i < totalColumns; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        // Lưu file vào backend với xử lý lỗi
        String exportDir = "exports/";
        File directory = new File(exportDir);
        if (!directory.exists() && !directory.mkdirs()) {
            log.error("Failed to create directory: {}", exportDir);
        }

        String filePath = exportDir + fileName;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
            log.info("File saved successfully at: {}", filePath);
        } catch (IOException e) {
            log.error("Error saving file to {}: {}", filePath, e.getMessage());
        }

        // Trả về file để tải về
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

}
