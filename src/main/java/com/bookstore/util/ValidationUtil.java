package com.bookstore.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validation Utility - Kiểm tra dữ liệu đầu vào nâng cao
 */
public class ValidationUtil {

    // Regex patterns
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9,10}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern ISBN_PATTERN = Pattern.compile("^\\d{10,13}$");

    // ==================== ADVANCED VALIDATION METHODS ====================

    /**
     * Validate Book
     */
    public static ValidationResult validateBook(String tenSach, String tacGia, String nhaXuatBan,
                                              int namXuatBan, double giaBia, int soLuongTon) {
        ValidationResult result = new ValidationResult();

        if (tenSach == null || tenSach.trim().isEmpty()) {
            result.addError("Tên sách không được để trống");
        } else if (tenSach.length() > 200) {
            result.addError("Tên sách không được quá 200 ký tự");
        }

        if (giaBia < 0) {
            result.addError("Giá bìa phải lớn hơn hoặc bằng 0");
        }

        if (soLuongTon < 0) {
            result.addError("Số lượng tồn phải lớn hơn hoặc bằng 0");
        }

        int currentYear = LocalDate.now().getYear();
        if (namXuatBan < 1900 || namXuatBan > currentYear + 1) {
            result.addError("Năm xuất bản không hợp lệ");
        }

        return result;
    }

    /**
     * Validate Customer
     */
    public static ValidationResult validateCustomer(String hoTen, String soDienThoai, String email) {
        ValidationResult result = new ValidationResult();

        if (hoTen == null || hoTen.trim().isEmpty()) {
            result.addError("Họ tên không được để trống");
        } else if (hoTen.length() > 100) {
            result.addError("Họ tên không được quá 100 ký tự");
        }

        if (soDienThoai != null && !soDienThoai.isEmpty()) {
            if (!isValidPhone(soDienThoai)) {
                result.addError("Số điện thoại không hợp lệ");
            }
        }

        if (email != null && !email.isEmpty()) {
            if (!isValidEmail(email)) {
                result.addError("Email không hợp lệ");
            }
        }

        return result;
    }

    /**
     * Validate Employee
     */
    public static ValidationResult validateEmployee(String hoTen, String soDienThoai, String email, double luong) {
        ValidationResult result = new ValidationResult();

        if (hoTen == null || hoTen.trim().isEmpty()) {
            result.addError("Họ tên không được để trống");
        }

        if (soDienThoai != null && !soDienThoai.isEmpty()) {
            if (!isValidPhone(soDienThoai)) {
                result.addError("Số điện thoại không hợp lệ");
            }
        }

        if (email != null && !email.isEmpty()) {
            if (!isValidEmail(email)) {
                result.addError("Email không hợp lệ");
            }
        }

        if (luong < 0) {
            result.addError("Lương phải lớn hơn hoặc bằng 0");
        }

        return result;
    }

    /**
     * Validate Supplier
     */
    public static ValidationResult validateSupplier(String tenNCC, String soDienThoai, String email) {
        ValidationResult result = new ValidationResult();

        if (tenNCC == null || tenNCC.trim().isEmpty()) {
            result.addError("Tên nhà cung cấp không được để trống");
        }

        if (soDienThoai != null && !soDienThoai.isEmpty()) {
            if (!isValidPhone(soDienThoai)) {
                result.addError("Số điện thoại không hợp lệ");
            }
        }

        if (email != null && !email.isEmpty()) {
            if (!isValidEmail(email)) {
                result.addError("Email không hợp lệ");
            }
        }

        return result;
    }

    /**
     * Validate User
     */
    public static ValidationResult validateUser(String tenDangNhap, String matKhau) {
        ValidationResult result = new ValidationResult();

        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
            result.addError("Tên đăng nhập không được để trống");
        } else if (tenDangNhap.length() < 3 || tenDangNhap.length() > 50) {
            result.addError("Tên đăng nhập phải từ 3-50 ký tự");
        }

        if (matKhau == null || matKhau.length() < 6) {
            result.addError("Mật khẩu phải có ít nhất 6 ký tự");
        }

        return result;
    }

    // ==================== BASIC VALIDATION METHODS ====================

    /**
     * Kiểm tra tên không được để trống
     */
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Kiểm tra số điện thoại Việt Nam
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Kiểm tra email
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Kiểm tra giá tiền hợp lệ
     */
    public static boolean isValidPrice(double price) {
        return price >= 0;
    }

    /**
     * Kiểm tra số lượng hợp lệ
     */
    public static boolean isValidQuantity(int quantity) {
        return quantity >= 0;
    }

    /**
     * Kiểm tra năm xuất bản hợp lệ
     */
    public static boolean isValidYear(int year) {
        int currentYear = LocalDate.now().getYear();
        return year > 1900 && year <= currentYear + 1;
    }

    /**
     * Kiểm tra ngày sinh hợp lệ (không được lớn hơn ngày hiện tại)
     */
    public static boolean isValidBirthDate(Date birthDate) {
        if (birthDate == null) {
            return true; // Optional field
        }
        return birthDate.before(new Date());
    }

    /**
     * Kiểm tra lương hợp lệ
     */
    public static boolean isValidSalary(double salary) {
        return salary >= 0;
    }

    /**
     * Kiểm tra tên đăng nhập
     */
    public static boolean isValidUsername(String username) {
        return username != null && username.trim().length() >= 3 && username.trim().length() <= 50;
    }

    /**
     * Kiểm tra mật khẩu
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Lấy thông báo lỗi cho số điện thoại
     */
    public static String getPhoneErrorMessage() {
        return "Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại 10-11 số bắt đầu bằng 0";
    }

    /**
     * Lấy thông báo lỗi cho email
     */
    public static String getEmailErrorMessage() {
        return "Email không hợp lệ. Vui lòng nhập đúng định dạng email";
    }

    /**
     * Chuyển đổi Date sang LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Chuyển đổi LocalDate sang Date
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // ==================== VALIDATION RESULT CLASS ====================

    /**
     * Validation Result - Lưu trữ kết quả kiểm tra
     */
    public static class ValidationResult {
        private final List<String> errors = new ArrayList<>();

        public void addError(String error) {
            errors.add(error);
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public List<String> getErrors() {
            return errors;
        }

        public String getErrorMessage() {
            return String.join("\n", errors);
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }
    }
}
