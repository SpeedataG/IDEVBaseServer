package cepri.device.utils;

/**
 * @author :Reginer in  2017/10/24 9:47.
 *         联系方式:QQ:282921012
 *         功能描述:
 */
public class SecurityUnit {
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * 电源控制、打开端口
     *
     * @return 0    执行成功。
     * -1	执行失败。
     */
    public native int Init();

    /**
     * 电源控制、关闭端口
     *
     * @return 0    执行成功。
     * -1	执行失败。
     */
    public native int DeInit();

    /**
     * 清除发送缓存
     *
     * @return 0    执行成功。
     * -1	执行失败。
     */
    public native int ClearSendCache();

    /**
     * 清除接收缓存
     *
     * @return 0    执行成功。
     * -1	执行失败。
     */
    public native int ClearRecvCache();

    /**
     * 设置通讯参数
     *
     * @param baudrate  通讯波特率
     * @param databits  数据位
     * @param parity    校验位。0为无校验，1为奇校验，2为偶校验，3为Mark校验，4为Space校验。
     * @param stopbits  停止位。0为无停止位，1为1位停止位，2为2位停止位，3为1.5位停止位。
     * @param blockmode 阻塞模式。0为无阻塞，1为阻塞
     * @return 0    执行成功。
     * -1	执行失败。
     */
    public native int Config(int baudrate, int databits, int parity, int stopbits, int blockmode);

    /**
     * 发送数据
     *
     * @param buf    数组地址
     * @param offset 偏移量
     * @param count  数据数量
     * @return 0    成功。
     * 1	端口未打开。
     * 2	发送出错。
     */
    public native int SendData(byte[] buf, int offset, int count);

    /**
     * 接收数据
     *
     * @param buf    数组地址
     * @param offset 偏移量
     * @param count  数据数量
     * @return 为实际读取数据个数，数据类型为int
     */
    public native int RecvData(byte[] buf, int offset, int count);

    /**
     * 设置发送和接收数据的超时时间
     *
     * @param Direction 方向  0，	发送；1,接收
     * @param Timeout   超时时间 单位 ms(毫秒)
     * @return 0    成功。
     * -1	失败。
     */
    public native int SetTimeOut(int Direction, int Timeout);
}
