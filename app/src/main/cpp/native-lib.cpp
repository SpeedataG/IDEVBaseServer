#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jint JNICALL
Java_cepri_device_utils_SecurityUnit_Init(JNIEnv *env, jobject instance) {

    // TODO

}extern "C"
JNIEXPORT jint JNICALL
Java_cepri_device_utils_SecurityUnit_DeInit(JNIEnv *env, jobject instance) {

    // TODO

}extern "C"
JNIEXPORT jint JNICALL
Java_cepri_device_utils_SecurityUnit_ClearSendCache(JNIEnv *env, jobject instance) {

    // TODO

}extern "C"
JNIEXPORT jint JNICALL
Java_cepri_device_utils_SecurityUnit_ClearRecvCache(JNIEnv *env, jobject instance) {

    // TODO

}extern "C"
JNIEXPORT jint JNICALL
Java_cepri_device_utils_SecurityUnit_Config(JNIEnv *env, jobject instance, jint baudrate, jint databits, jint parity, jint stopbits, jint blockmode) {

    // TODO

}extern "C"
JNIEXPORT jint JNICALL
Java_cepri_device_utils_SecurityUnit_SendData(JNIEnv *env, jobject instance, jbyteArray buf_, jint offset, jint count) {
    jbyte *buf = env->GetByteArrayElements(buf_, NULL);

    // TODO

    env->ReleaseByteArrayElements(buf_, buf, 0);
}extern "C"
JNIEXPORT jint JNICALL
Java_cepri_device_utils_SecurityUnit_RecvData(JNIEnv *env, jobject instance, jbyteArray buf_, jint offset, jint count) {
    jbyte *buf = env->GetByteArrayElements(buf_, NULL);

    // TODO

    env->ReleaseByteArrayElements(buf_, buf, 0);
}extern "C"
JNIEXPORT jint JNICALL
Java_cepri_device_utils_SecurityUnit_SetTimeOut(JNIEnv *env, jobject instance, jint Direction, jint Timeout) {

    // TODO

}