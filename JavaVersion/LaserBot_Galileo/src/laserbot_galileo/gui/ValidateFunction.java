package laserbot_galileo.gui;

/**
 * Interface dùng để tạo hàm kiểm tra bổ sung cho Validate
 *
 * @author KimHao
 */
@FunctionalInterface
public interface ValidateFunction {

    /**
     * Hàm validate dữ liệu bổ sung
     *
     * @param control là control được validate, dùng cho trường hợp có nhiều
     * control dùng một ValidateFunction
     * @param name là tên control được validate, dùng cho trường hợp có nhiều
     * control dùng một ValidateFunction
     * @return - Rỗng nếu không có lỗi<br/>
     * - Chuỗi thông báo lỗi nếu có lỗi
     */
    public String valid(Object control, String name);
}
