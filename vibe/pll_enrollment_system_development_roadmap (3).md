# Detailed Java Technical Coding Roadmap
## Computerized Enrollment System for Precious Little Lights Academy

This roadmap focuses on **what Java files to create, where to place them, what each file extends, and what each file should be able to do**.

Assumption: the system uses **Java Swing** for GUI, **PostgreSQL**, **JDBC**, **BCrypt**, and **iText 7**.

---

# Java File Responsibility Legend

## GUI Window Files
GUI window files should usually extend:

```java
JFrame
```

Use `JFrame` when the screen opens as its own full window.

Examples:

```java
LoginView extends JFrame
AdminDashboardView extends JFrame
NewStudentEnrollmentView extends JFrame
```

## GUI Panel Files
Reusable inner sections should usually extend:

```java
JPanel
```

Use `JPanel` when the file is only a section inside another window.

Examples:

```java
SidebarPanel extends JPanel
HeaderPanel extends JPanel
TablePanel extends JPanel
```

## GUI Dialog Files
Popup forms should usually extend:

```java
JDialog
```

Use `JDialog` when the form should open as a modal popup.

Examples:

```java
PaymentFormView extends JDialog
ForgotPasswordView extends JDialog
OtpVerificationView extends JDialog
```

## Non-GUI Files
These should not extend Swing classes:

```java
DAO files        → no extends
Service files    → no extends
Controller files → no extends
Model files      → no extends
Utility files    → final class, usually no extends
```

---

# 0. Recommended Project Structure

Create this folder/package structure first:

```text
src/
└── com/
    └── plla/
        └── enrollment/
            ├── Main.java
            │
            ├── config/
            │   └── AppConfig.java
            │
            ├── database/
            │   └── DatabaseConnection.java
            │
            ├── models/
            │   ├── User.java
            │   ├── Role.java
            │   ├── Student.java
            │   ├── Guardian.java
            │   ├── Enrollment.java
            │   ├── Requirement.java
            │   ├── DiagnosticTest.java
            │   ├── Appointment.java
            │   ├── GradeLevel.java
            │   ├── Section.java
            │   ├── Subject.java
            │   ├── Teacher.java
            │   ├── Classroom.java
            │   ├── ClassSchedule.java
            │   ├── FeeSchedule.java
            │   ├── Payment.java
            │   ├── BillingRecord.java
            │   ├── AuditLog.java
            │   └── BackupLog.java
            │
            ├── dao/
            │   ├── UserDAO.java
            │   ├── RoleDAO.java
            │   ├── StudentDAO.java
            │   ├── GuardianDAO.java
            │   ├── EnrollmentDAO.java
            │   ├── RequirementDAO.java
            │   ├── DiagnosticTestDAO.java
            │   ├── GradeLevelDAO.java
            │   ├── SectionDAO.java
            │   ├── SubjectDAO.java
            │   ├── TeacherDAO.java
            │   ├── ClassroomDAO.java
            │   ├── ClassScheduleDAO.java
            │   ├── FeeScheduleDAO.java
            │   ├── PaymentDAO.java
            │   ├── BillingDAO.java
            │   ├── AuditLogDAO.java
            │   └── BackupLogDAO.java
            │
            ├── services/
            │   ├── AuthService.java
            │   ├── UserService.java
            │   ├── EnrollmentService.java
            │   ├── PaymentService.java
            │   ├── BillingService.java
            │   ├── ScheduleGeneratorService.java
            │   ├── ReportService.java
            │   ├── BackupService.java
            │   └── AuditLogService.java
            │
            ├── controllers/
            │   ├── LoginController.java
            │   ├── AccountRegistrationController.java
            │   ├── SearchStudentController.java
            │   ├── EnrollmentController.java
            │   ├── RequirementController.java
            │   ├── DiagnosticTestController.java
            │   ├── PaymentController.java
            │   ├── ReportsController.java
            │   ├── ScheduleController.java
            │   ├── StudentQualificationController.java
            │   ├── MaintenanceController.java
            │   └── NavigationController.java
            │
            ├── views/                         ← GUI FILES ARE HERE
            │   ├── auth/
            │   │   ├── LoginView.java         ← GUI
            │   │   ├── ForgotPasswordView.java ← GUI
            │   │   ├── OtpVerificationView.java ← GUI
            │   │   └── ResetPasswordView.java ← GUI
            │   │
            │   ├── dashboards/
            │   │   ├── AdminDashboardView.java ← GUI
            │   │   ├── RegistrarDashboardView.java ← GUI
            │   │   └── TeacherDashboardView.java ← GUI
            │   │
            │   ├── users/
            │   │   ├── AccountRegistrationView.java ← GUI
            │   │   └── UserManagementView.java ← GUI
            │   │
            │   ├── students/
            │   │   ├── SearchStudentView.java ← GUI
            │   │   ├── StudentDetailsView.java ← GUI
            │   │   └── EditStudentInformationView.java ← GUI
            │   │
            │   ├── enrollment/
            │   │   ├── EnrollmentMenuView.java ← GUI
            │   │   ├── NewStudentEnrollmentView.java ← GUI
            │   │   ├── OldStudentEnrollmentView.java ← GUI
            │   │   ├── RequirementsView.java ← GUI
            │   │   └── DiagnosticTestView.java ← GUI
            │   │
            │   ├── payments/
            │   │   ├── PaymentBillingView.java ← GUI
            │   │   ├── PaymentFormView.java ← GUI
            │   │   ├── StudentBalanceView.java ← GUI
            │   │   └── FeeScheduleView.java ← GUI
            │   │
            │   ├── schedules/
            │   │   ├── ClassScheduleManagementView.java ← GUI
            │   │   ├── AddSectionView.java ← GUI
            │   │   ├── ViewScheduleView.java ← GUI
            │   │   ├── EditScheduleView.java ← GUI
            │   │   ├── AutoGenerateScheduleView.java ← GUI
            │   │   └── TeacherScheduleView.java ← GUI
            │   │
            │   ├── reports/
            │   │   └── ReportsView.java ← GUI
            │   │
            │   ├── maintenance/
            │   │   ├── MaintenanceView.java ← GUI
            │   │   ├── BackupView.java ← GUI
            │   │   ├── RestoreBackupView.java ← GUI
            │   │   └── ArchiveRecordsView.java ← GUI
            │   │
            │   ├── help/
            │   │   ├── HelpView.java ← GUI
            │   │   └── AboutView.java ← GUI
            │   │
            │   └── components/
            │       ├── SidebarPanel.java ← reusable GUI component
            │       ├── HeaderPanel.java ← reusable GUI component
            │       ├── FormPanel.java ← reusable GUI component
            │       ├── TablePanel.java ← reusable GUI component
            │       └── RoundedButton.java ← reusable GUI component
            │
            ├── reports/
            │   ├── PdfGeneratorUtil.java
            │   ├── RegistrationFormReport.java
            │   ├── StatementOfAccountReport.java
            │   └── CertificateReport.java
            │
            └── utils/
                ├── PasswordUtil.java
                ├── ValidationUtil.java
                ├── SessionManager.java
                ├── DateUtil.java
                ├── CurrencyUtil.java
                ├── DialogUtil.java
                ├── FileUtil.java
                └── Constants.java
```

---

# 1. File Type Guide

## GUI files
All GUI files should be placed under:

```text
src/com/plla/enrollment/views/
```

GUI files usually extend one of these:

```java
JFrame
JPanel
JDialog
```

Example:

```java
public class LoginView extends JFrame {
}
```

or

```java
public class AccountRegistrationView extends JPanel {
}
```

## Controller files
Controllers should be placed under:

```text
src/com/plla/enrollment/controllers/
```

Controllers connect GUI events to services.

Example:

```java
public class LoginController {
    private LoginView loginView;
    private AuthService authService;
}
```

## Service files
Services should be placed under:

```text
src/com/plla/enrollment/services/
```

Services contain business logic.

Example:

```java
public class PaymentService {
    public BigDecimal calculateBalance(int enrollmentId) {
        return null;
    }
}
```

## DAO files
DAO files should be placed under:

```text
src/com/plla/enrollment/dao/
```

DAO files contain SQL queries only.

Example:

```java
public class StudentDAO {
    public Student findByLRN(String lrn) {
        return null;
    }
}
```

## Model files
Model files should be placed under:

```text
src/com/plla/enrollment/models/
```

Models represent database records.

Example:

```java
public class Student {
    private int studentId;
    private String lrn;
    private String firstName;
    private String lastName;
}
```

---

# 2. Coding Flow Rule

For every module, code in this order:

```text
1. Database table
2. Model class
3. DAO class
4. Service class
5. GUI View class
6. Controller class
7. Connect controller to dashboard
8. Test the module
```

Example for Account Registration:

```text
1. users table
2. User.java
3. UserDAO.java
4. UserService.java
5. AccountRegistrationView.java
6. AccountRegistrationController.java
7. Add button in AdminDashboardView.java
8. Test creating an account
```

---

# 3. Phase 1 — Base Project Files

## 3.1 Main Application Entry Point

Create:

```text
src/com/plla/enrollment/Main.java
```

Purpose:
- Starts the application
- Sets Swing look and feel
- Opens `LoginView`

Checklist:
- [ ] Create `Main.java`
- [ ] Set look and feel
- [ ] Open `LoginView`
- [ ] Create `LoginController`

---

## 3.2 Configuration File

Create:

```text
src/com/plla/enrollment/config/AppConfig.java
```

Purpose:
- Stores database URL
- Stores database username
- Stores database password
- Stores report output folder
- Stores backup folder

Suggested constants:

```java
public static final String DB_URL = "jdbc:postgresql://localhost:5432/plla_enrollment";
public static final String DB_USERNAME = "postgres";
public static final String DB_PASSWORD = "your_password";
public static final String REPORT_FOLDER = "C:/PLL_Reports/";
public static final String BACKUP_FOLDER = "C:/PLL_Backups/";
```

Checklist:
- [ ] Add database URL
- [ ] Add database username
- [ ] Add database password
- [ ] Add report folder path
- [ ] Add backup folder path

---

## 3.3 Database Connection

Create:

```text
src/com/plla/enrollment/database/DatabaseConnection.java
```

Purpose:
- Creates PostgreSQL connection
- Used by every DAO class

Required method:

```java
public static Connection getConnection() throws SQLException
```

Checklist:
- [ ] Import `java.sql.Connection`
- [ ] Import `java.sql.DriverManager`
- [ ] Import `java.sql.SQLException`
- [ ] Use values from `AppConfig`
- [ ] Test connection

---

# 4. Phase 2 — Utility Classes

## 4.1 Password Utility

Create:

```text
src/com/plla/enrollment/utils/PasswordUtil.java
```

Purpose:
- Hash passwords
- Verify passwords

Required methods:

```java
public static String hashPassword(String plainPassword)
public static boolean verifyPassword(String plainPassword, String hashedPassword)
```

Used by:
- `AuthService.java`
- `UserService.java`
- `AccountRegistrationController.java`

Checklist:
- [ ] Add BCrypt dependency
- [ ] Create hash method
- [ ] Create verify method
- [ ] Test with sample password

---

## 4.2 Session Manager

Create:

```text
src/com/plla/enrollment/utils/SessionManager.java
```

Purpose:
- Stores currently logged-in user
- Stores current role
- Clears user session on logout

Required fields:

```java
private static User currentUser;
```

Required methods:

```java
public static void login(User user)
public static void logout()
public static User getCurrentUser()
public static boolean isAdmin()
public static boolean isRegistrar()
public static boolean isTeacher()
```

Used by:
- Dashboards
- Controllers
- Role restrictions

Checklist:
- [ ] Store current user
- [ ] Check role methods
- [ ] Clear session method

---

## 4.3 Validation Utility

Create:

```text
src/com/plla/enrollment/utils/ValidationUtil.java
```

Purpose:
- Centralize all input validation

Required methods:

```java
public static boolean isEmpty(String value)
public static boolean isValidEmail(String email)
public static boolean isValidPhone(String phone)
public static boolean isValidMoney(String value)
public static boolean isValidLRN(String lrn)
public static boolean isPasswordStrong(String password)
```

Used by:
- Registration
- Enrollment
- Payment
- Search

Checklist:
- [ ] Required field validator
- [ ] Email validator
- [ ] Phone validator
- [ ] LRN validator
- [ ] Money validator
- [ ] Password validator

---

## 4.4 Dialog Utility

Create:

```text
src/com/plla/enrollment/utils/DialogUtil.java
```

Purpose:
- Avoid repeated `JOptionPane` code

Required methods:

```java
public static void showSuccess(Component parent, String message)
public static void showError(Component parent, String message)
public static void showWarning(Component parent, String message)
public static boolean showConfirm(Component parent, String message)
```

Used by:
- All GUI controllers

---

# 5. Phase 3 — Authentication Module

## 5.1 User Model

Create:

```text
src/com/plla/enrollment/models/User.java
```

Suggested fields:

```java
private int userId;
private String fullName;
private String username;
private String email;
private String passwordHash;
private String role;
private boolean active;
```

Checklist:
- [ ] Add private fields
- [ ] Add empty constructor
- [ ] Add full constructor
- [ ] Add getters and setters

---

## 5.2 User DAO

Create:

```text
src/com/plla/enrollment/dao/UserDAO.java
```

Purpose:
- SQL operations for users

Required methods:

```java
public User findByUsername(String username)
public User findByEmail(String email)
public boolean createUser(User user)
public boolean updatePassword(int userId, String newPasswordHash)
public boolean usernameExists(String username)
public boolean emailExists(String email)
public List<User> findAllUsers()
```

Checklist:
- [ ] Write SELECT by username
- [ ] Write SELECT by email
- [ ] Write INSERT user
- [ ] Write UPDATE password
- [ ] Write duplicate username check
- [ ] Write duplicate email check

---

## 5.3 Auth Service

Create:

```text
src/com/plla/enrollment/services/AuthService.java
```

Purpose:
- Login logic
- Password checking
- Session creation

Required method:

```java
public User login(String username, String password)
```

Logic:

```text
1. Check if username/password is empty
2. Find user by username
3. If user does not exist, return null
4. Verify password using PasswordUtil
5. If valid, save session
6. Return user
```

Checklist:
- [ ] Validate empty input
- [ ] Call `UserDAO.findByUsername()`
- [ ] Call `PasswordUtil.verifyPassword()`
- [ ] Call `SessionManager.login(user)`

---

## 5.4 Login GUI

Create:

```text
src/com/plla/enrollment/views/auth/LoginView.java
```

This is a GUI file.

Recommended class type:

```java
public class LoginView extends JFrame {
}
```

Components:

```text
JTextField usernameField
JPasswordField passwordField
JButton loginButton
JButton forgotPasswordButton
JLabel titleLabel
```

Required getter methods:

```java
public String getUsername()
public String getPassword()
public JButton getLoginButton()
public JButton getForgotPasswordButton()
```

Checklist:
- [ ] Create JFrame
- [ ] Add username field
- [ ] Add password field
- [ ] Add login button
- [ ] Add forgot password button
- [ ] Add getters for controller

---

## 5.5 Login Controller

Create:

```text
src/com/plla/enrollment/controllers/LoginController.java
```

Purpose:
- Handles login button click
- Calls `AuthService`
- Opens correct dashboard

Constructor:

```java
public LoginController(LoginView view)
```

Required methods:

```java
private void attachEvents()
private void handleLogin()
private void openDashboard(User user)
```

Logic:

```text
If role = Admin → open AdminDashboardView
If role = Registrar → open RegistrarDashboardView
If role = Teacher → open TeacherDashboardView
```

Checklist:
- [ ] Listen to login button
- [ ] Get input from `LoginView`
- [ ] Call `AuthService.login()`
- [ ] Show error if invalid
- [ ] Open dashboard if valid

---

# 6. Phase 4 — Dashboard GUI Files

## 6.1 Admin Dashboard GUI

Create:

```text
src/com/plla/enrollment/views/dashboards/AdminDashboardView.java
```

This is a GUI file.

Recommended class type:

```java
public class AdminDashboardView extends JFrame {
}
```

Admin buttons:

```text
Account Registration
Enrollment
Search Student
Class Schedule
Payment and Billing
Reports
Maintenance
Help
About
Logout
```

Required getters:

```java
public JButton getAccountRegistrationButton()
public JButton getEnrollmentButton()
public JButton getSearchButton()
public JButton getScheduleButton()
public JButton getPaymentButton()
public JButton getReportsButton()
public JButton getMaintenanceButton()
public JButton getLogoutButton()
```

---

## 6.2 Registrar Dashboard GUI

Create:

```text
src/com/plla/enrollment/views/dashboards/RegistrarDashboardView.java
```

This is a GUI file.

Registrar buttons:

```text
Enrollment
Search Student
Payment and Billing
Reports
Help
About
Logout
```

---

## 6.3 Teacher Dashboard GUI

Create:

```text
src/com/plla/enrollment/views/dashboards/TeacherDashboardView.java
```

This is a GUI file.

Teacher buttons:

```text
Student Qualification
View Schedule
Help
About
Logout
```

---

## 6.4 Navigation Controller

Create:

```text
src/com/plla/enrollment/controllers/NavigationController.java
```

Purpose:
- Centralizes dashboard button navigation
- Opens module windows/panels
- Handles logout

Required methods:

```java
public void openAccountRegistration()
public void openEnrollmentMenu()
public void openSearchStudent()
public void openPaymentBilling()
public void openReports()
public void openMaintenance()
public void logout(JFrame currentFrame)
```

---

# 7. Phase 5 — Account Registration Module

## 7.1 Account Registration GUI

Create:

```text
src/com/plla/enrollment/views/users/AccountRegistrationView.java
```

This is a GUI file.

Recommended class type:

```java
public class AccountRegistrationView extends JFrame {
}
```

Components:

```text
JTextField fullNameField
JTextField usernameField
JTextField emailField
JPasswordField passwordField
JPasswordField confirmPasswordField
JComboBox<String> roleComboBox
JButton registerButton
JButton clearButton
JButton backButton
```

Required getters:

```java
public String getFullName()
public String getUsername()
public String getEmail()
public String getPassword()
public String getConfirmPassword()
public String getSelectedRole()
public JButton getRegisterButton()
public JButton getClearButton()
public JButton getBackButton()
```

---

## 7.2 User Service

Create:

```text
src/com/plla/enrollment/services/UserService.java
```

Purpose:
- Validates user registration
- Hashes password
- Calls `UserDAO`

Required method:

```java
public boolean registerUser(String fullName, String username, String email, String password, String confirmPassword, String role)
```

Logic:

```text
1. Validate required fields
2. Validate email format
3. Validate password strength
4. Check password confirmation
5. Check duplicate username
6. Check duplicate email
7. Hash password
8. Save user
```

---

## 7.3 Account Registration Controller

Create:

```text
src/com/plla/enrollment/controllers/AccountRegistrationController.java
```

Purpose:
- Handles register button
- Sends form data to `UserService`

Required methods:

```java
private void attachEvents()
private void handleRegister()
private void clearForm()
```

---

# 8. Phase 6 — Student Search Module

## 8.1 Student Model

Create:

```text
src/com/plla/enrollment/models/Student.java
```

Suggested fields:

```java
private int studentId;
private String lrn;
private String firstName;
private String middleName;
private String lastName;
private LocalDate birthDate;
private String gender;
private String address;
private String status;
```

---

## 8.2 Student DAO

Create:

```text
src/com/plla/enrollment/dao/StudentDAO.java
```

Required methods:

```java
public boolean createStudent(Student student)
public boolean updateStudent(Student student)
public Student findById(int studentId)
public Student findByLRN(String lrn)
public List<Student> searchByName(String keyword)
public List<Student> findAll()
```

---

## 8.3 Search Student GUI

Create:

```text
src/com/plla/enrollment/views/students/SearchStudentView.java
```

This is a GUI file.

Components:

```text
JTextField searchField
JComboBox<String> searchTypeComboBox
JButton searchButton
JButton viewDetailsButton
JTable resultsTable
DefaultTableModel tableModel
```

Search type values:

```text
Name
LRN
```

Required getters:

```java
public String getSearchKeyword()
public String getSearchType()
public JButton getSearchButton()
public JTable getResultsTable()
public void setTableData(List<Student> students)
```

---

## 8.4 Search Controller

Create:

```text
src/com/plla/enrollment/controllers/SearchStudentController.java
```

Purpose:
- Reads search input
- Calls `StudentDAO`
- Updates JTable

Required methods:

```java
private void attachEvents()
private void handleSearch()
private void handleViewDetails()
```

---

# 9. Phase 7 — Enrollment Module

## 9.1 Guardian Model

Create:

```text
src/com/plla/enrollment/models/Guardian.java
```

Fields:

```java
private int guardianId;
private int studentId;
private String fullName;
private String relationship;
private String phoneNumber;
private String email;
private String address;
```

---

## 9.2 Enrollment Model

Create:

```text
src/com/plla/enrollment/models/Enrollment.java
```

Fields:

```java
private int enrollmentId;
private int studentId;
private int gradeLevelId;
private int sectionId;
private String schoolYear;
private String enrollmentType;
private String paymentScheme;
private LocalDate enrollmentDate;
private String status;
```

---

## 9.3 Enrollment Menu GUI

Create:

```text
src/com/plla/enrollment/views/enrollment/EnrollmentMenuView.java
```

This is a GUI file.

Buttons:

```text
New Student Enrollment
Old Student Enrollment
Edit Student Information
Requirements
Diagnostic Test
Back
```

---

## 9.4 New Student Enrollment GUI

Create:

```text
src/com/plla/enrollment/views/enrollment/NewStudentEnrollmentView.java
```

This is a GUI file.

Recommended sections:

```text
Student Information Panel
Guardian Information Panel
Enrollment Information Panel
Action Buttons Panel
```

Student fields:

```text
LRN
First Name
Middle Name
Last Name
Birth Date
Gender
Address
Previous School
```

Guardian fields:

```text
Guardian Full Name
Relationship
Phone Number
Email
Address
```

Enrollment fields:

```text
School Year
Grade Level
Section
Payment Scheme
Enrollment Date
```

Buttons:

```text
Save Enrollment
Clear
Back
```

Required getters:

```java
public Student getStudentFormData()
public Guardian getGuardianFormData()
public Enrollment getEnrollmentFormData()
public JButton getSaveButton()
public JButton getClearButton()
public JButton getBackButton()
```

---

## 9.5 Old Student Enrollment GUI

Create:

```text
src/com/plla/enrollment/views/enrollment/OldStudentEnrollmentView.java
```

This is a GUI file.

Components:

```text
Search student field
Search button
Student info display panel
School year field
Grade level dropdown
Section dropdown
Payment scheme dropdown
Enroll button
```

---

## 9.6 Enrollment DAO

Create:

```text
src/com/plla/enrollment/dao/EnrollmentDAO.java
```

Required methods:

```java
public boolean createEnrollment(Enrollment enrollment)
public boolean updateEnrollment(Enrollment enrollment)
public Enrollment findCurrentEnrollmentByStudentId(int studentId)
public List<Enrollment> findEnrollmentHistory(int studentId)
public boolean updateStatus(int enrollmentId, String status)
```

---

## 9.7 Guardian DAO

Create:

```text
src/com/plla/enrollment/dao/GuardianDAO.java
```

Required methods:

```java
public boolean createGuardian(Guardian guardian)
public boolean updateGuardian(Guardian guardian)
public Guardian findByStudentId(int studentId)
```

---

## 9.8 Enrollment Service

Create:

```text
src/com/plla/enrollment/services/EnrollmentService.java
```

Purpose:
- Controls full enrollment transaction
- Saves student, guardian, and enrollment together

Required methods:

```java
public boolean enrollNewStudent(Student student, Guardian guardian, Enrollment enrollment)
public boolean enrollOldStudent(int studentId, Enrollment enrollment)
public boolean updateStudentInformation(Student student, Guardian guardian)
```

Important:
Use a database transaction here.

Logic for new student:

```text
1. Start transaction
2. Insert student
3. Get generated student ID
4. Insert guardian using student ID
5. Insert enrollment using student ID
6. Commit transaction
7. Rollback if any step fails
```

---

## 9.9 Enrollment Controller

Create:

```text
src/com/plla/enrollment/controllers/EnrollmentController.java
```

Purpose:
- Handles new and old enrollment GUI actions

Required methods:

```java
private void handleNewStudentEnrollment()
private void handleOldStudentEnrollment()
private void validateEnrollmentForm()
private void clearForm()
```

---

# 10. Phase 8 — Requirements and Diagnostic Test

## 10.1 Requirement Model

Create:

```text
src/com/plla/enrollment/models/Requirement.java
```

Fields:

```java
private int requirementId;
private int enrollmentId;
private boolean birthCertificateSubmitted;
private boolean goodMoralSubmitted;
private boolean sf10Submitted;
private String remarks;
```

---

## 10.2 Requirements GUI

Create:

```text
src/com/plla/enrollment/views/enrollment/RequirementsView.java
```

This is a GUI file.

Components:

```text
Student search field
Birth Certificate checkbox
Good Moral checkbox
SF10 checkbox
Remarks text area
Save button
```

---

## 10.3 Diagnostic Test Model

Create:

```text
src/com/plla/enrollment/models/DiagnosticTest.java
```

Fields:

```java
private int diagnosticTestId;
private int enrollmentId;
private LocalDate testDate;
private LocalTime testTime;
private String status;
private String remarks;
```

---

## 10.4 Diagnostic Test GUI

Create:

```text
src/com/plla/enrollment/views/enrollment/DiagnosticTestView.java
```

This is a GUI file.

Components:

```text
Student search field
Date picker/text field
Time dropdown
Status dropdown
Remarks text area
Save button
```

Status values:

```text
Scheduled
Completed
Passed
Failed
```

---

# 11. Phase 9 — Payment and Billing Module

## 11.1 Fee Schedule Model

Create:

```text
src/com/plla/enrollment/models/FeeSchedule.java
```

Fields:

```java
private int feeScheduleId;
private int gradeLevelId;
private String paymentScheme;
private BigDecimal tuitionFee;
private BigDecimal miscellaneousFee;
private BigDecimal totalAmount;
```

---

## 11.2 Payment Model

Create:

```text
src/com/plla/enrollment/models/Payment.java
```

Fields:

```java
private int paymentId;
private int enrollmentId;
private BigDecimal amount;
private String paymentMethod;
private String referenceNumber;
private LocalDate paymentDate;
private String remarks;
```

---

## 11.3 Payment and Billing GUI

Create:

```text
src/com/plla/enrollment/views/payments/PaymentBillingView.java
```

This is a GUI file.

Components:

```text
Student search field
Search button
Student info panel
Total fee label
Total paid label
Balance label
Payment history table
Record payment button
Generate SOA button
```

---

## 11.4 Payment Form GUI

Create:

```text
src/com/plla/enrollment/views/payments/PaymentFormView.java
```

This is a GUI file.

Components:

```text
Amount field
Payment method dropdown
Reference number field
Payment date field
Remarks field
Save payment button
Cancel button
```

---

## 11.5 Payment DAO

Create:

```text
src/com/plla/enrollment/dao/PaymentDAO.java
```

Required methods:

```java
public boolean recordPayment(Payment payment)
public List<Payment> findPaymentsByEnrollmentId(int enrollmentId)
public BigDecimal getTotalPaid(int enrollmentId)
```

---

## 11.6 Billing Service

Create:

```text
src/com/plla/enrollment/services/BillingService.java
```

Required methods:

```java
public BigDecimal getTotalFee(int enrollmentId)
public BigDecimal getTotalPaid(int enrollmentId)
public BigDecimal calculateBalance(int enrollmentId)
public boolean canAcceptPayment(int enrollmentId, BigDecimal amount)
```

Logic:

```text
balance = totalFee - totalPaid
Reject payment if amount <= 0
Reject payment if amount > balance
Require reference number for GCash or Bank Transfer
```

---

## 11.7 Payment Controller

Create:

```text
src/com/plla/enrollment/controllers/PaymentController.java
```

Required methods:

```java
private void handleSearchStudentBilling()
private void handleRecordPayment()
private void refreshPaymentTable()
private void updateBalanceLabels()
```

---

# 12. Phase 10 — Reports Module

## 12.1 Reports GUI

Create:

```text
src/com/plla/enrollment/views/reports/ReportsView.java
```

This is a GUI file.

Components:

```text
Student search field
Report type dropdown
Generate button
Open folder button
```

Report types:

```text
Registration Form
Statement of Account
Certificate of Enrollment
```

---

## 12.2 PDF Utility

Create:

```text
src/com/plla/enrollment/reports/PdfGeneratorUtil.java
```

Purpose:
- Common PDF formatting
- Headers
- Footers
- File naming

Required methods:

```java
public static String createReportFilePath(String reportName, String studentName)
public static void addSchoolHeader(Document document)
```

---

## 12.3 Registration Form Report

Create:

```text
src/com/plla/enrollment/reports/RegistrationFormReport.java
```

Required method:

```java
public String generate(int enrollmentId)
```

Data needed:

```text
Student data
Guardian data
Enrollment data
Grade level
Section
School year
```

---

## 12.4 Statement of Account Report

Create:

```text
src/com/plla/enrollment/reports/StatementOfAccountReport.java
```

Required method:

```java
public String generate(int enrollmentId)
```

Data needed:

```text
Student data
Fee schedule
Payment history
Total paid
Remaining balance
```

---

## 12.5 Reports Controller

Create:

```text
src/com/plla/enrollment/controllers/ReportsController.java
```

Required methods:

```java
private void handleGenerateReport()
private void generateRegistrationForm()
private void generateStatementOfAccount()
private void generateCertificate()
```

---

# 13. Phase 11 — Class Schedule Management Module

## 13.1 Class Schedule Model

Create:

```text
src/com/plla/enrollment/models/ClassSchedule.java
```

Fields:

```java
private int scheduleId;
private int sectionId;
private int subjectId;
private int teacherId;
private int classroomId;
private String dayOfWeek;
private LocalTime startTime;
private LocalTime endTime;
```

---

## 13.2 Schedule Management GUI

Create:

```text
src/com/plla/enrollment/views/schedules/ClassScheduleManagementView.java
```

This is a GUI file.

Buttons:

```text
Add New Section
View Schedule
Edit Schedule
Auto Generate Schedule
Back
```

---

## 13.3 Add Section GUI

Create:

```text
src/com/plla/enrollment/views/schedules/AddSectionView.java
```

This is a GUI file.

Components:

```text
Grade level dropdown
Section name field
Room dropdown
Adviser dropdown
Save button
```

---

## 13.4 View Schedule GUI

Create:

```text
src/com/plla/enrollment/views/schedules/ViewScheduleView.java
```

This is a GUI file.

Components:

```text
Grade level dropdown
Section dropdown
Weekly schedule table
Print button
```

---

## 13.5 Edit Schedule GUI

Create:

```text
src/com/plla/enrollment/views/schedules/EditScheduleView.java
```

This is a GUI file.

Components:

```text
Section dropdown
Subject dropdown
Teacher dropdown
Classroom dropdown
Day dropdown
Start time dropdown
End time dropdown
Save button
Delete button
```

---

## 13.6 Auto Generate Schedule GUI

Create:

```text
src/com/plla/enrollment/views/schedules/AutoGenerateScheduleView.java
```

This is a GUI file.

Components:

```text
Grade level dropdown
Section dropdown
Generate button
Generated schedule preview table
Save generated schedule button
```

---

## 13.7 Class Schedule DAO

Create:

```text
src/com/plla/enrollment/dao/ClassScheduleDAO.java
```

Required methods:

```java
public boolean createSchedule(ClassSchedule schedule)
public boolean updateSchedule(ClassSchedule schedule)
public boolean deleteSchedule(int scheduleId)
public List<ClassSchedule> findBySection(int sectionId)
public List<ClassSchedule> findByTeacher(int teacherId)
public boolean hasTeacherConflict(int teacherId, String day, LocalTime start, LocalTime end)
public boolean hasClassroomConflict(int classroomId, String day, LocalTime start, LocalTime end)
public boolean hasSectionConflict(int sectionId, String day, LocalTime start, LocalTime end)
```

---

## 13.8 Greedy Schedule Generator Service

Create:

```text
src/com/plla/enrollment/services/ScheduleGeneratorService.java
```

Required method:

```java
public List<ClassSchedule> generateSchedule(int gradeLevelId, int sectionId)
```

Basic algorithm:

```text
1. Load required subjects for grade level
2. Load available teachers
3. Load available classrooms
4. Load available time slots
5. For each subject:
   a. Find a teacher who can teach subject
   b. Find first available time slot
   c. Find first available classroom
   d. Check teacher conflict
   e. Check classroom conflict
   f. Check section conflict
   g. If no conflict, create schedule item
6. Return generated schedule list
```

---

## 13.9 Schedule Controller

Create:

```text
src/com/plla/enrollment/controllers/ScheduleController.java
```

Required methods:

```java
private void handleAddSection()
private void handleViewSchedule()
private void handleEditSchedule()
private void handleAutoGenerateSchedule()
private void handleSaveGeneratedSchedule()
```

---

# 14. Phase 12 — Teacher Qualification Module

## 14.1 Student Qualification GUI

Create:

```text
src/com/plla/enrollment/views/schedules/StudentQualificationView.java
```

This is a GUI file.

Components:

```text
Grade level dropdown
Section dropdown
Student table
Qualification status dropdown
Remarks field
Save button
```

Status values:

```text
Qualified
Not Qualified
For Review
```

---

## 14.2 Student Qualification Controller

Create:

```text
src/com/plla/enrollment/controllers/StudentQualificationController.java
```

Required methods:

```java
private void loadStudentsBySection()
private void handleSaveQualification()
```

---

# 15. Phase 13 — Maintenance Module

## 15.1 Maintenance Main GUI

Create:

```text
src/com/plla/enrollment/views/maintenance/MaintenanceView.java
```

This is a GUI file.

Buttons:

```text
Create Backup
Restore Backup
Archive Records
Back
```

---

## 15.2 Backup GUI

Create:

```text
src/com/plla/enrollment/views/maintenance/BackupView.java
```

This is a GUI file.

Components:

```text
Backup folder label
Create backup button
Backup history table
```

---

## 15.3 Restore Backup GUI

Create:

```text
src/com/plla/enrollment/views/maintenance/RestoreBackupView.java
```

This is a GUI file.

Components:

```text
Choose backup file button
Selected file label
Restore button
```

---

## 15.4 Archive Records GUI

Create:

```text
src/com/plla/enrollment/views/maintenance/ArchiveRecordsView.java
```

This is a GUI file.

Components:

```text
Search student field
Student table
Archive button
Restore archived record button
```

---

## 15.5 Backup Service

Create:

```text
src/com/plla/enrollment/services/BackupService.java
```

Required methods:

```java
public boolean createBackup()
public boolean restoreBackup(File backupFile)
```

Technical note:
Use Java `ProcessBuilder` to run PostgreSQL commands:

```text
pg_dump
psql
pg_restore
```

---

## 15.6 Maintenance Controller

Create:

```text
src/com/plla/enrollment/controllers/MaintenanceController.java
```

Required methods:

```java
private void handleCreateBackup()
private void handleRestoreBackup()
private void handleArchiveRecord()
```

---

# 16. Phase 14 — Help and About Module

## 16.1 Help GUI

Create:

```text
src/com/plla/enrollment/views/help/HelpView.java
```

This is a GUI file.

Components:

```text
Role-based instructions panel
FAQ section
Troubleshooting section
Back button
```

---

## 16.2 About GUI

Create:

```text
src/com/plla/enrollment/views/help/AboutView.java
```

This is a GUI file.

Components:

```text
School name
School address
School contact info
System name
Developers
Version number
Back button
```

---

# 17. Detailed File-by-File Java Roadmap

Use this section as your main coding checklist.

---

## 17.1 Core Startup and Configuration Files

### `Main.java`
Location:

```text
src/com/plla/enrollment/Main.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Start the whole application
- Set the Swing look and feel
- Create `LoginView`
- Create `LoginController`
- Display the login window

---

### `AppConfig.java`
Location:

```text
src/com/plla/enrollment/config/AppConfig.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Store database URL
- Store database username and password
- Store report folder path
- Store backup folder path
- Store application constants

Recommended declaration:

```java
public final class AppConfig {
    private AppConfig() {}
}
```

---

### `DatabaseConnection.java`
Location:

```text
src/com/plla/enrollment/database/DatabaseConnection.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Connect Java to PostgreSQL
- Return a reusable `Connection`
- Throw or handle SQL connection errors
- Be used by every DAO class

Important method:

```java
public static Connection getConnection() throws SQLException
```

---

## 17.2 Utility Files

### `PasswordUtil.java`
Location:

```text
src/com/plla/enrollment/utils/PasswordUtil.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Hash plain passwords using BCrypt
- Verify plain password against hashed password
- Prevent storing raw passwords

Recommended declaration:

```java
public final class PasswordUtil {
    private PasswordUtil() {}
}
```

---

### `SessionManager.java`
Location:

```text
src/com/plla/enrollment/utils/SessionManager.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Store the currently logged-in user
- Return the current user
- Check if current user is Admin, Registrar, or Teacher
- Clear session during logout

---

### `ValidationUtil.java`
Location:

```text
src/com/plla/enrollment/utils/ValidationUtil.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Validate empty fields
- Validate email format
- Validate phone number format
- Validate LRN format
- Validate money input
- Validate password strength
- Validate selected combo box values

---

### `DialogUtil.java`
Location:

```text
src/com/plla/enrollment/utils/DialogUtil.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Show success messages
- Show error messages
- Show warning messages
- Show confirmation dialogs
- Reduce repeated `JOptionPane` code

---

## 17.3 Model Files

Model files represent database records. They should only contain fields, constructors, getters, and setters.

### `User.java`
Location:

```text
src/com/plla/enrollment/models/User.java
```

Extends:

```java
Does not extend anything
```

Should be able to store:
- User ID
- Full name
- Username
- Email
- Password hash
- Role
- Account status

---

### `Student.java`
Location:

```text
src/com/plla/enrollment/models/Student.java
```

Extends:

```java
Does not extend anything
```

Should be able to store:
- Student ID
- LRN
- First name
- Middle name
- Last name
- Birth date
- Gender
- Address
- Student status

---

### `Guardian.java`
Location:

```text
src/com/plla/enrollment/models/Guardian.java
```

Extends:

```java
Does not extend anything
```

Should be able to store:
- Guardian ID
- Student ID
- Guardian full name
- Relationship
- Phone number
- Email
- Address

---

### `Enrollment.java`
Location:

```text
src/com/plla/enrollment/models/Enrollment.java
```

Extends:

```java
Does not extend anything
```

Should be able to store:
- Enrollment ID
- Student ID
- Grade level ID
- Section ID
- School year
- Enrollment type
- Payment scheme
- Enrollment date
- Enrollment status

---

### `Payment.java`
Location:

```text
src/com/plla/enrollment/models/Payment.java
```

Extends:

```java
Does not extend anything
```

Should be able to store:
- Payment ID
- Enrollment ID
- Payment amount
- Payment method
- Reference number
- Payment date
- Remarks

---

### `ClassSchedule.java`
Location:

```text
src/com/plla/enrollment/models/ClassSchedule.java
```

Extends:

```java
Does not extend anything
```

Should be able to store:
- Schedule ID
- Section ID
- Subject ID
- Teacher ID
- Classroom ID
- Day of week
- Start time
- End time

---

## 17.4 DAO Files

DAO files should contain SQL only. They should not show GUI dialogs and should not contain business rules.

### `UserDAO.java`
Location:

```text
src/com/plla/enrollment/dao/UserDAO.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Find user by username
- Find user by email
- Create new user
- Update password
- Check duplicate username
- Check duplicate email
- Load all users

---

### `StudentDAO.java`
Location:

```text
src/com/plla/enrollment/dao/StudentDAO.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Insert new student
- Update student information
- Find student by ID
- Find student by LRN
- Search students by name
- Load all students

---

### `EnrollmentDAO.java`
Location:

```text
src/com/plla/enrollment/dao/EnrollmentDAO.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Create enrollment record
- Update enrollment record
- Find current enrollment by student ID
- Find enrollment history
- Update enrollment status

---

### `PaymentDAO.java`
Location:

```text
src/com/plla/enrollment/dao/PaymentDAO.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Record a payment
- Load payment history by enrollment ID
- Compute total paid
- Find payment by ID
- Delete or void payment if allowed by admin

---

### `ClassScheduleDAO.java`
Location:

```text
src/com/plla/enrollment/dao/ClassScheduleDAO.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Create schedule
- Update schedule
- Delete schedule
- Load schedule by section
- Load schedule by teacher
- Check teacher conflict
- Check classroom conflict
- Check section conflict

---

## 17.5 Service Files

Service files contain business logic. They connect controllers to DAOs.

### `AuthService.java`
Location:

```text
src/com/plla/enrollment/services/AuthService.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Validate login input
- Find user account
- Verify BCrypt password
- Check if account is active
- Start session after successful login
- Return the logged-in user

---

### `UserService.java`
Location:

```text
src/com/plla/enrollment/services/UserService.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Validate account registration fields
- Check duplicate username and email
- Hash password
- Create user account
- Update user account
- Deactivate user account

---

### `EnrollmentService.java`
Location:

```text
src/com/plla/enrollment/services/EnrollmentService.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Enroll a new student
- Enroll an old student
- Save student, guardian, and enrollment together
- Use database transaction
- Roll back if one part fails
- Update student information

---

### `BillingService.java`
Location:

```text
src/com/plla/enrollment/services/BillingService.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Get total school fees
- Get total paid amount
- Calculate remaining balance
- Prevent invalid payment amount
- Require reference number for GCash or bank transfer

---

### `ScheduleGeneratorService.java`
Location:

```text
src/com/plla/enrollment/services/ScheduleGeneratorService.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Load subjects, teachers, classrooms, and time slots
- Generate schedule using Greedy Algorithm
- Avoid teacher conflicts
- Avoid classroom conflicts
- Avoid section conflicts
- Return generated schedule list before saving

---

### `BackupService.java`
Location:

```text
src/com/plla/enrollment/services/BackupService.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Run `pg_dump` using `ProcessBuilder`
- Create database backup file
- Restore backup file using PostgreSQL command
- Save backup logs
- Return success or failure status

---

## 17.6 Controller Files

Controller files handle button clicks and connect GUI to services.

### `LoginController.java`
Location:

```text
src/com/plla/enrollment/controllers/LoginController.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Listen to Login button
- Get username/password from `LoginView`
- Call `AuthService`
- Open correct dashboard based on user role
- Show error message for invalid login

---

### `AccountRegistrationController.java`
Location:

```text
src/com/plla/enrollment/controllers/AccountRegistrationController.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Listen to Register button
- Get form data from `AccountRegistrationView`
- Call `UserService`
- Clear form after successful registration
- Show validation errors

---

### `EnrollmentController.java`
Location:

```text
src/com/plla/enrollment/controllers/EnrollmentController.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Listen to Save Enrollment button
- Get student form data
- Get guardian form data
- Get enrollment form data
- Call `EnrollmentService`
- Open requirements or payment screen after enrollment

---

### `PaymentController.java`
Location:

```text
src/com/plla/enrollment/controllers/PaymentController.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Search student billing record
- Load payment history
- Open payment form dialog
- Record payment
- Refresh balance labels
- Generate Statement of Account

---

### `ScheduleController.java`
Location:

```text
src/com/plla/enrollment/controllers/ScheduleController.java
```

Extends:

```java
Does not extend anything
```

Should be able to:
- Add section
- View schedule
- Edit schedule
- Delete schedule
- Call auto-schedule generator
- Save generated schedule
- Prevent conflict before saving

---

## 17.7 GUI Files

GUI files should only build screens, expose getter methods, and display data. Do not put SQL inside GUI files.

### `LoginView.java`
Location:

```text
src/com/plla/enrollment/views/auth/LoginView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Display username field
- Display password field
- Display login button
- Display forgot password button
- Return username input
- Return password input
- Expose login button to controller

---

### `ForgotPasswordView.java`
Location:

```text
src/com/plla/enrollment/views/auth/ForgotPasswordView.java
```

Extends:

```java
JDialog
```

Should be able to:
- Ask for registered email
- Send email input to controller
- Display Send OTP button
- Display Cancel button

---

### `OtpVerificationView.java`
Location:

```text
src/com/plla/enrollment/views/auth/OtpVerificationView.java
```

Extends:

```java
JDialog
```

Should be able to:
- Ask for OTP code
- Verify OTP button
- Resend OTP button
- Cancel button

---

### `ResetPasswordView.java`
Location:

```text
src/com/plla/enrollment/views/auth/ResetPasswordView.java
```

Extends:

```java
JDialog
```

Should be able to:
- Ask for new password
- Ask for confirm password
- Submit password reset
- Cancel password reset

---

### `AdminDashboardView.java`
Location:

```text
src/com/plla/enrollment/views/dashboards/AdminDashboardView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Display Admin dashboard
- Show buttons for all modules
- Show logged-in user name
- Open account registration
- Open enrollment
- Open search
- Open schedule management
- Open payment and billing
- Open reports
- Open maintenance
- Logout

---

### `RegistrarDashboardView.java`
Location:

```text
src/com/plla/enrollment/views/dashboards/RegistrarDashboardView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Display Registrar dashboard
- Show only Registrar modules
- Open enrollment
- Open search
- Open payment and billing
- Open reports
- Logout

---

### `TeacherDashboardView.java`
Location:

```text
src/com/plla/enrollment/views/dashboards/TeacherDashboardView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Display Teacher dashboard
- Open student qualification
- Open teacher schedule view
- Open help/about
- Logout

---

### `AccountRegistrationView.java`
Location:

```text
src/com/plla/enrollment/views/users/AccountRegistrationView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Display account creation form
- Collect full name
- Collect username
- Collect email
- Collect password
- Collect confirm password
- Select role
- Expose Register button
- Clear form fields

---

### `SearchStudentView.java`
Location:

```text
src/com/plla/enrollment/views/students/SearchStudentView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Display search bar
- Select search type: Name or LRN
- Display results in `JTable`
- Return selected student ID
- Open student details

---

### `StudentDetailsView.java`
Location:

```text
src/com/plla/enrollment/views/students/StudentDetailsView.java
```

Extends:

```java
JDialog
```

Should be able to:
- Display full student information
- Display guardian information
- Display enrollment information
- Display payment summary
- Display requirement status

---

### `NewStudentEnrollmentView.java`
Location:

```text
src/com/plla/enrollment/views/enrollment/NewStudentEnrollmentView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Display student information form
- Display guardian information form
- Display enrollment information form
- Select grade level
- Select section
- Select payment scheme
- Return `Student` object from form data
- Return `Guardian` object from form data
- Return `Enrollment` object from form data
- Expose Save button
- Clear form

---

### `OldStudentEnrollmentView.java`
Location:

```text
src/com/plla/enrollment/views/enrollment/OldStudentEnrollmentView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Search existing student
- Display existing student information
- Select new school year
- Select grade level
- Select section
- Select payment scheme
- Create new enrollment for existing student

---

### `EditStudentInformationView.java`
Location:

```text
src/com/plla/enrollment/views/students/EditStudentInformationView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Load selected student information
- Edit student information
- Edit guardian information
- Save updated data
- Cancel changes

---

### `RequirementsView.java`
Location:

```text
src/com/plla/enrollment/views/enrollment/RequirementsView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Search or receive selected enrollment
- Display Birth Certificate checkbox
- Display Good Moral checkbox
- Display SF10 checkbox
- Save requirement status
- Display remarks field

---

### `DiagnosticTestView.java`
Location:

```text
src/com/plla/enrollment/views/enrollment/DiagnosticTestView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Schedule diagnostic test date
- Schedule diagnostic test time
- Set status: Scheduled, Completed, Passed, Failed
- Add remarks
- Save diagnostic test result

---

### `PaymentBillingView.java`
Location:

```text
src/com/plla/enrollment/views/payments/PaymentBillingView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Search student billing account
- Display total fee
- Display total paid
- Display remaining balance
- Display payment history table
- Open `PaymentFormView`
- Generate Statement of Account

---

### `PaymentFormView.java`
Location:

```text
src/com/plla/enrollment/views/payments/PaymentFormView.java
```

Extends:

```java
JDialog
```

Should be able to:
- Enter payment amount
- Select payment method
- Enter reference number
- Enter payment date
- Add remarks
- Save payment
- Cancel payment entry

---

### `ReportsView.java`
Location:

```text
src/com/plla/enrollment/views/reports/ReportsView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Search student or enrollment
- Select report type
- Generate Registration Form
- Generate Statement of Account
- Generate Certificate of Enrollment
- Open report folder

---

### `ClassScheduleManagementView.java`
Location:

```text
src/com/plla/enrollment/views/schedules/ClassScheduleManagementView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Open Add Section screen
- Open View Schedule screen
- Open Edit Schedule screen
- Open Auto Generate Schedule screen
- Return to dashboard

---

### `AddSectionView.java`
Location:

```text
src/com/plla/enrollment/views/schedules/AddSectionView.java
```

Extends:

```java
JDialog
```

Should be able to:
- Select grade level
- Enter section name
- Select classroom
- Select adviser
- Save new section

---

### `ViewScheduleView.java`
Location:

```text
src/com/plla/enrollment/views/schedules/ViewScheduleView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Select grade level
- Select section
- Display weekly timetable using `JTable`
- Print or export schedule if needed

---

### `EditScheduleView.java`
Location:

```text
src/com/plla/enrollment/views/schedules/EditScheduleView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Select schedule entry
- Edit subject
- Edit teacher
- Edit classroom
- Edit day and time
- Save changes
- Delete schedule entry

---

### `AutoGenerateScheduleView.java`
Location:

```text
src/com/plla/enrollment/views/schedules/AutoGenerateScheduleView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Select grade level
- Select section
- Trigger Greedy Algorithm generation
- Preview generated schedule
- Save generated schedule

---

### `TeacherScheduleView.java`
Location:

```text
src/com/plla/enrollment/views/schedules/TeacherScheduleView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Display logged-in teacher schedule
- Display assigned subjects
- Display assigned sections
- Display weekly timetable

---

### `StudentQualificationView.java`
Location:

```text
src/com/plla/enrollment/views/schedules/StudentQualificationView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Select grade level and section
- Display students in table
- Select qualification status
- Add teacher remarks
- Save qualification result

---

### `MaintenanceView.java`
Location:

```text
src/com/plla/enrollment/views/maintenance/MaintenanceView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Open backup screen
- Open restore screen
- Open archive records screen
- Return to admin dashboard

---

### `BackupView.java`
Location:

```text
src/com/plla/enrollment/views/maintenance/BackupView.java
```

Extends:

```java
JDialog
```

Should be able to:
- Display backup folder
- Trigger backup creation
- Display backup history

---

### `RestoreBackupView.java`
Location:

```text
src/com/plla/enrollment/views/maintenance/RestoreBackupView.java
```

Extends:

```java
JDialog
```

Should be able to:
- Choose backup file
- Confirm restore action
- Trigger restore process

---

### `ArchiveRecordsView.java`
Location:

```text
src/com/plla/enrollment/views/maintenance/ArchiveRecordsView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Search inactive student records
- Display records in table
- Archive selected record
- Restore archived record if needed

---

### `HelpView.java`
Location:

```text
src/com/plla/enrollment/views/help/HelpView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Display user manual
- Display FAQ
- Display role-based instructions
- Display troubleshooting guide

---

### `AboutView.java`
Location:

```text
src/com/plla/enrollment/views/help/AboutView.java
```

Extends:

```java
JFrame
```

Should be able to:
- Display school information
- Display system information
- Display developer names
- Display version number

---

## 17.8 Reusable GUI Components

### `SidebarPanel.java`
Location:

```text
src/com/plla/enrollment/views/components/SidebarPanel.java
```

Extends:

```java
JPanel
```

Should be able to:
- Display navigation buttons
- Highlight active module
- Be reused in dashboard windows

---

### `HeaderPanel.java`
Location:

```text
src/com/plla/enrollment/views/components/HeaderPanel.java
```

Extends:

```java
JPanel
```

Should be able to:
- Display screen title
- Display logged-in user
- Display date/time if needed

---

### `TablePanel.java`
Location:

```text
src/com/plla/enrollment/views/components/TablePanel.java
```

Extends:

```java
JPanel
```

Should be able to:
- Display reusable `JTable`
- Set table model
- Return selected row ID
- Refresh table data

---

# 18. Recommended Coding Order

Follow this exact order:

```text
1. AppConfig.java
2. DatabaseConnection.java
3. PasswordUtil.java
4. SessionManager.java
5. ValidationUtil.java
6. DialogUtil.java
7. User.java
8. UserDAO.java
9. AuthService.java
10. LoginView.java              ← GUI
11. LoginController.java
12. AdminDashboardView.java     ← GUI
13. RegistrarDashboardView.java ← GUI
14. TeacherDashboardView.java   ← GUI
15. NavigationController.java
16. AccountRegistrationView.java ← GUI
17. UserService.java
18. AccountRegistrationController.java
19. Student.java
20. Guardian.java
21. Enrollment.java
22. StudentDAO.java
23. GuardianDAO.java
24. EnrollmentDAO.java
25. SearchStudentView.java      ← GUI
26. SearchStudentController.java
27. EnrollmentMenuView.java     ← GUI
28. NewStudentEnrollmentView.java ← GUI
29. OldStudentEnrollmentView.java ← GUI
30. EnrollmentService.java
31. EnrollmentController.java
32. Requirement.java
33. DiagnosticTest.java
34. RequirementsView.java       ← GUI
35. DiagnosticTestView.java     ← GUI
36. RequirementController.java
37. DiagnosticTestController.java
38. FeeSchedule.java
39. Payment.java
40. FeeScheduleDAO.java
41. PaymentDAO.java
42. BillingService.java
43. PaymentBillingView.java     ← GUI
44. PaymentFormView.java        ← GUI
45. PaymentController.java
46. PdfGeneratorUtil.java
47. RegistrationFormReport.java
48. StatementOfAccountReport.java
49. ReportsView.java            ← GUI
50. ReportsController.java
51. GradeLevel.java
52. Section.java
53. Subject.java
54. Teacher.java
55. Classroom.java
56. ClassSchedule.java
57. ClassScheduleDAO.java
58. ClassScheduleManagementView.java ← GUI
59. AddSectionView.java         ← GUI
60. ViewScheduleView.java       ← GUI
61. EditScheduleView.java       ← GUI
62. AutoGenerateScheduleView.java ← GUI
63. ScheduleGeneratorService.java
64. ScheduleController.java
65. StudentQualificationView.java ← GUI
66. StudentQualificationController.java
67. MaintenanceView.java        ← GUI
68. BackupView.java             ← GUI
69. RestoreBackupView.java      ← GUI
70. ArchiveRecordsView.java     ← GUI
71. BackupService.java
72. MaintenanceController.java
73. HelpView.java               ← GUI
74. AboutView.java              ← GUI
75. Full integration testing
```

---

# 18. Minimum Working Version First

Build this first before all other features:

```text
1. Database connection works
2. Admin login works
3. Admin dashboard opens
4. Admin can create users
5. Registrar can log in
6. Registrar can encode new student
7. Registrar can search student
8. Registrar can record payment
9. System can generate Statement of Account PDF
```

MVP GUI files:

```text
LoginView.java
AdminDashboardView.java
RegistrarDashboardView.java
AccountRegistrationView.java
SearchStudentView.java
NewStudentEnrollmentView.java
PaymentBillingView.java
PaymentFormView.java
ReportsView.java
```

MVP non-GUI files:

```text
DatabaseConnection.java
AppConfig.java
PasswordUtil.java
SessionManager.java
ValidationUtil.java
DialogUtil.java
User.java
Student.java
Guardian.java
Enrollment.java
Payment.java
FeeSchedule.java
UserDAO.java
StudentDAO.java
GuardianDAO.java
EnrollmentDAO.java
PaymentDAO.java
FeeScheduleDAO.java
AuthService.java
UserService.java
EnrollmentService.java
BillingService.java
LoginController.java
AccountRegistrationController.java
SearchStudentController.java
EnrollmentController.java
PaymentController.java
ReportsController.java
StatementOfAccountReport.java
```

---

# 19. Naming Rules

Use these naming rules consistently:

```text
GUI files:        SomethingView.java
Controller files: SomethingController.java
Service files:    SomethingService.java
DAO files:        SomethingDAO.java
Model files:      SingularNoun.java
Utility files:    SomethingUtil.java
```

Examples:

```text
LoginView.java
LoginController.java
AuthService.java
UserDAO.java
User.java
PasswordUtil.java
```

---

# 20. Important Technical Rules

- [ ] Never put SQL inside GUI files
- [ ] Never put password hashing inside GUI files
- [ ] Never put business logic inside GUI files
- [ ] GUI files should only display components and collect input
- [ ] Controllers should handle button clicks
- [ ] Services should contain business rules
- [ ] DAO files should contain SQL queries
- [ ] Models should only contain fields, constructors, getters, and setters
- [ ] Use `PreparedStatement` for every SQL query
- [ ] Use transactions for enrollment and payment operations
- [ ] Use BCrypt for passwords
- [ ] Validate all user input before saving
- [ ] Keep report generation inside the `reports/` package
- [ ] Keep reusable UI components inside `views/components/`

