#include <jni.h>
#include <string>
#include "BBP.h"

extern "C" {
JNIEXPORT jstring JNICALL
Java_io_alcatraz_mathematics_PiApi_nativeBBPDecimal(
        JNIEnv *env,
        jobject /* this */,
        jint precision) {
    int c_precision = precision;
    double bbp_result = BBP::calcBBP(c_precision);
    char result_str[256];
    sprintf(result_str,"%f",bbp_result);
    return env->NewStringUTF(result_str);
}

JNIEXPORT jstring JNICALL
Java_io_alcatraz_mathematics_PiApi_nativeBBPHex(
        JNIEnv *env,
        jobject /* this */,
        jint precision) {
    int c_precision = precision;
    double bbp_result = BBP::calcBBP(c_precision);
    char result_str[256];
    sprintf(result_str,"%a",bbp_result);
    return env->NewStringUTF(result_str);
}

}




