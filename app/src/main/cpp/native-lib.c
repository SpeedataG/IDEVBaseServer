#include   <jni.h>
#include   <stdio.h>    
#include   <stdlib.h>  
#include   <unistd.h>    
#include   <sys/types.h>
#include   <sys/stat.h> 
#include   <fcntl.h>   
#include   <termios.h> 
#include   <errno.h>   
#include   <string.h>
#include   "android/log.h"

static const char *JNI_TAG = "SecurityUnit";
static int global_fd = -1;
static int gpio_fd = -1;
static int global_timeout[2] = {0};
static int isblock = 0;

#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  JNI_TAG, fmt, ##args)
//#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, JNI_TAG, fmt, ##args)
#define LOGD(fmt, args...)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, JNI_TAG, fmt, ##args)
#define LOGW(fmt, args...) __android_log_print(ANDROID_LOG_WARN,  JNI_TAG, fmt, ##args)
#define LOGF(fmt, args...) __android_log_print(ANDROID_LOG_FATAL, JNI_TAG, fmt, ##args)

#define SPORT 		"/dev/ttyMT3"
#define GPIO_NUM	2
#define GPIO_CTL_PATH	"/sys/class/misc/mtgpio/pin"
#define GPIO_MODE_STR	"-wmode%d %d"
#define GPIO_DIR_STR 	"-wdir%d %d"
#define GPIO_OUT_STR 	"-wdout%d %d"
#define	SEND		0
#define	RECV		1
#define NON_BLOCK	0
#define BLOCK		1

JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_Init(JNIEnv *env, jobject obj);
JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_DeInit(JNIEnv *env, jobject obj);
JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_SendData(JNIEnv *env, jobject obj, jbyteArray buf, jint offset, jint count);
JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_RecvData(JNIEnv *env, jobject obj, jbyteArray buf, jint offset, jint count);
JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_Config(JNIEnv *env, jobject obj, jint baudrate, jint databits, jint parity, jint stopbits, jint blockmode);
JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_ClearSendCache(JNIEnv *env, jobject obj);
JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_ClearRecvCache(JNIEnv *env, jobject obj);
JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_SetTimeOut(JNIEnv *env, jobject obj, jint direction, jint timeout);

static int init_gpio(void)
{
	int rv;
	char bf[128] = {0};
	gpio_fd = open(GPIO_CTL_PATH, O_RDWR);
	if(gpio_fd < 0)
	{
		LOGE("%s open gpio path %s failed", __FUNCTION__, GPIO_CTL_PATH);
		return -1;
	}

	snprintf(bf, sizeof(bf), GPIO_MODE_STR, GPIO_NUM, 0);
	rv = write(gpio_fd, bf, strlen(bf));
	if(rv < 0)
	{
		LOGE("%s write gpio mode %s failed", __FUNCTION__, bf);
		return -1;
	}
	
	memset(bf, 0, sizeof(bf));
	snprintf(bf, sizeof(bf), GPIO_DIR_STR, GPIO_NUM, 1);
	rv = write(gpio_fd, bf, strlen(bf));
	if(rv < 0)
	{
		LOGE("%s write gpio dir %s failed", __FUNCTION__, bf);
		return -1;
	}

	return 0;
}

static int set_gpio_output(int level)
{
	int rv;
	char bf[128] = {0};

	if(gpio_fd < 0)
	{
		LOGE("%s gpio_fd is not ready", __FUNCTION__);
		return -1;
	}

	snprintf(bf, sizeof(bf), GPIO_OUT_STR, GPIO_NUM, !!level);
	rv = write(gpio_fd, bf, strlen(bf));
	if(rv < 0)
	{
		LOGE("%s write gpio out %s failed", __FUNCTION__, bf);
		return -1;
	}
	
	return 0;
}

static speed_t getBaudrate(jint baudrate)
{
	switch(baudrate) {
		case 0: return B0;
		case 50: return B50;
		case 75: return B75;
		case 110: return B110;
		case 134: return B134;
		case 150: return B150;
		case 200: return B200;
		case 300: return B300;
		case 600: return B600;
		case 1200: return B1200;
		case 1800: return B1800;
		case 2400: return B2400;
		case 4800: return B4800;
		case 9600: return B9600;
		case 19200: return B19200;
		case 38400: return B38400;
		case 57600: return B57600;
		case 115200: return B115200;
		case 230400: return B230400;
		case 460800: return B460800;
		case 500000: return B500000;
		case 576000: return B576000;
		case 921600: return B921600;
		case 1000000: return B1000000;
		case 1152000: return B1152000;
		case 1500000: return B1500000;
		case 2000000: return B2000000;
		case 2500000: return B2500000;
		case 3000000: return B3000000;
		case 3500000: return B3500000;
		case 4000000: return B4000000;
		default: return -1;
	}
}

static void setTermios(struct termios * pNewtio, int uBaudRate, int cSize, int cStop, int crc)
{
	bzero(pNewtio, sizeof(struct termios)); /* clear struct for new port settings */
	pNewtio->c_cflag = uBaudRate | cSize | cStop | CREAD | CLOCAL;
	if(crc == 0)
	{
//		pNewtio->c_iflag |= IGNPAR;
	}
	else if(crc == 1)
	{
		pNewtio->c_cflag |= PARENB;
		pNewtio->c_iflag |= INPCK;
	}
	else if(crc == 2)
	{
		pNewtio->c_cflag |= (PARENB | PARODD);
		pNewtio->c_iflag |= INPCK;
	}
	pNewtio->c_oflag = 0;
	pNewtio->c_lflag = 0; //non ICANON

	pNewtio->c_cc[VINTR] = 0; /* Ctrl-c */
	pNewtio->c_cc[VQUIT] = 0; /* Ctrl-\ */
	pNewtio->c_cc[VERASE] = 0; /* del */
	pNewtio->c_cc[VKILL] = 0; /* @ */
	pNewtio->c_cc[VEOF] = 4; /* Ctrl-d */
	pNewtio->c_cc[VTIME] = 0; /* inter-character timer, timeout VTIME*0.1 */
	pNewtio->c_cc[VMIN] = 0; /* blocking read until VMIN character arrives */
	pNewtio->c_cc[VSWTC] = 0; /* '\0' */
	pNewtio->c_cc[VSTART] = 0; /* Ctrl-q */
	pNewtio->c_cc[VSTOP] = 0; /* Ctrl-s */
	pNewtio->c_cc[VSUSP] = 0; /* Ctrl-z */
	pNewtio->c_cc[VEOL] = 0; /* '\0' */
	pNewtio->c_cc[VREPRINT] = 0; /* Ctrl-r */
	pNewtio->c_cc[VDISCARD] = 0; /* Ctrl-u */
	pNewtio->c_cc[VWERASE] = 0; /* Ctrl-w */
	pNewtio->c_cc[VLNEXT] = 0; /* Ctrl-v */
	pNewtio->c_cc[VEOL2] = 0; /* '\0' */
}

JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_Init(JNIEnv *env, jobject obj)
{
	if(init_gpio() < 0)
	{
		return -1;
	}

	if(set_gpio_output(1) < 0)
	{
		return -1;
	}

	global_fd = open(SPORT, O_RDWR | O_NOCTTY);
	LOGD("now open %s\n", SPORT);
	if(global_fd < 0)
	{
		LOGE("open serial port failed\n");
		LOGE("reason is %s\n", strerror(errno));
		return -1;
	}

	return 0;
}

JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_DeInit(JNIEnv *env, jobject obj)
{
	if(global_fd >= 0)
	{
		close(global_fd);
	}

	if(gpio_fd >= 0)
	{
		set_gpio_output(0);
		close(gpio_fd);
	}

	return 0;
}

JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_SendData(JNIEnv *env, jobject obj, jbyteArray buf, jint offset, jint count)
{
	int reval = 0, writed = 0;
	char *bf = (*env)->GetByteArrayElements(env, buf, 0);

	if(global_fd < 0)
	{
		LOGE("%s global_fd is not ready", __FUNCTION__);
		return 1;
	}

	if(isblock)
	{
		fd_set wfds;
		struct timeval tv = { 
			.tv_sec = global_timeout[SEND] / 1000, 
			.tv_usec = (global_timeout[SEND] - tv.tv_sec * 1000) * 1000,
		};

		do
		{
			FD_ZERO(&wfds);
			FD_SET(global_fd, &wfds);

			if((reval = select(1 + global_fd, NULL, &wfds, NULL, &tv)) > 0)
			{
				if(FD_ISSET(global_fd, &wfds))
				{
					reval = write(global_fd, bf + offset + writed, count - writed);
					if(reval < 0)
					{
						LOGE("write serial port error\n");
					}

					writed += reval;
				}
			}
			else if(reval == 0 && writed > 0)			//timeout means 
			{
				LOGE("%s timeout exit, writed %d", __FUNCTION__, writed);
				reval = 2;
				goto ENDED;
			}
			else					
			{
				LOGE("%s other erro %d exit, writed %d", __FUNCTION__, reval, writed);
				reval = 2;
				goto ENDED;
			}
		}
		while(writed < count);
		reval = 0;
	}
	else
	{
		reval = write(global_fd, bf + offset, count);
		if(reval < 0)
		{
			LOGE("%s write serial port error %d\n", __FUNCTION__, reval);
			reval = 2;
			goto ENDED;
		}
		else if(reval < count)
		{
			LOGE("%s write serial port ok but only %d of %d writed\n", __FUNCTION__, reval, count);
			reval = 2;
			goto ENDED;
		}
		reval = 0;
	}
	
ENDED:	(*env)->ReleaseByteArrayElements(env, buf, bf, 0);
	return reval;
}

JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_RecvData(JNIEnv *env, jobject obj, jbyteArray buf, jint offset, jint count)
{
	int reval = 0, readed = 0;
	char *bf = malloc(count);

	if(global_fd < 0)
	{
		LOGE("%s global_fd is not ready", __FUNCTION__);
		return -1;
	}

	if(isblock)
	{
		fd_set rfds;
		struct timeval tv = { 
			.tv_sec = global_timeout[RECV] / 1000, 
			.tv_usec = (global_timeout[RECV] - tv.tv_sec * 1000) * 1000,
		};

		do
		{
			FD_ZERO(&rfds);
			FD_SET(global_fd, &rfds);

			if((reval = select(1 + global_fd, &rfds, NULL, NULL, &tv)) > 0)
			{
				if(FD_ISSET(global_fd, &rfds))
				{
					int temp = read(global_fd, bf + readed, count - readed);
					if(temp < 0)
					{
						LOGE("%s read serial error %d \n", __FUNCTION__, temp);
						if(readed == 0)
						{
							readed = -1;
							goto ENDED;
						}
					}

					readed += temp;
				}
			}
			else if(reval == 0 && readed > 0)			//timeout means 
			{
				//LOGD("%s timeout exit, readed %d", __FUNCTION__, readed);
				break;
			}
			else					
			{
				LOGE("%s other erro %d exit, readed %d", __FUNCTION__, reval, readed);
				break;
			}
		}
		while(readed < count);
	}
	else
	{
		int temp = read(global_fd, bf, count);
		if(temp < 0)
		{
			LOGE("%s read serial error %d \n", __FUNCTION__, temp);
			readed = -1;
			goto ENDED;
		}
		else
		{
			readed = temp;
		}
	}

	if(readed > 0)
	{
		(*env)->SetByteArrayRegion(env, buf, offset, readed, bf);
	}

ENDED:	free(bf);
	return readed;
}


JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_Config(JNIEnv *env, jobject obj, jint baudrate, jint databits, jint parity, jint stopbits, jint blockmode)
{
	struct termios newtio;

	if(global_fd < 0)
	{
		LOGE("%s global_fd is not ready", __FUNCTION__);
		return -1;
	}

	if(getBaudrate(baudrate) < 0)
	{
		LOGE("speed is not support");
		return -1;
	}

	if((databits != 8) && (databits != 7))
	{
		LOGE("size can only have value 7 or 8");
		return -1;
	}
	
	if((stopbits != 1) && (stopbits != 2))
	{
		LOGE("stop bit can only have value 1 or 2");
		return -1;
	}

	if((parity != 0) && (parity != 1) && (parity != 2))
	{
		LOGE("crc can only be no crc, even crc, odd crc");
		return -1;
	}

	setTermios(&newtio, getBaudrate(baudrate), (databits == 8 ? CS8 : CS7), (stopbits == 1 ? 0 : CSTOPB), parity);
	tcflush(global_fd, TCIFLUSH);
	tcsetattr(global_fd, TCSANOW, &newtio);

	isblock = !!blockmode;

	return 0;
}

JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_ClearSendCache(JNIEnv *env, jobject obj)
{
	if(global_fd < 0)
	{
		LOGE("%s global_fd is not ready", __FUNCTION__);
		return -1;
	}

	tcflush(global_fd, TCOFLUSH);
	return 0;
}

JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_ClearRecvCache(JNIEnv *env, jobject obj)
{
	if(global_fd < 0)
	{
		LOGE("%s global_fd is not ready", __FUNCTION__);
		return -1;
	}

	tcflush(global_fd, TCIFLUSH);
	return 0;
}

JNIEXPORT jint JNICALL Java_cepri_device_utils_SecurityUnit_SetTimeOut(JNIEnv *env, jobject obj, jint direction, jint timeout)
{
	if((direction < 0) || (direction > 1))
	{
		LOGE("%s error direction", __FUNCTION__);
		return -1;
	}

	LOGD("%s set %s timeout to %d ms", __FUNCTION__, (direction ? "recv" : "send"), timeout);
	global_timeout[direction] = timeout;
	return 0;
}
